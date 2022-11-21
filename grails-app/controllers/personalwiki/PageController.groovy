package personalwiki

import grails.converters.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import java.io.*
import java.util.zip.*
import javax.servlet.ServletOutputStream
import java.util.regex.*

class PageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def index = {
		if( session.user ) {
			redirect(uri:"/"+session.user.login+"/"+Page.homePageTitle.encodeAsURL())
		} else {
			redirect(controller:"session", action:"login")
		}
	}
	
    def restDelay = {
		def restDelay = ConfigurationHolder.config.personalwiki.rest.delay
		if( restDelay > 0 ) {
			Thread.sleep(restDelay*1000);
		}
    }
	
    def restPost = {
		restDelay()
		
		// make sure can create pages
		if( (!ConfigurationHolder.config.personalwiki.basic.disabled) && !( session?.user?.canCreatePage() ) ) {
			response.status = 400
			render "${message(code: 'custom.page.post.too.many.ajax')}"
			return false 
		}
		
		if( !params.userName.equalsIgnoreCase(session?.user?.login) ) {
			response.status = 403
			render "${message(code: 'custom.page.post.unauthorized')}"
			return false
		}
		def page = new Page()
		page.user = User.findByLogin(params.userName)
		def j = request.JSON
		page.title = j.title
		page.publiclyVisible = j.publiclyVisible
		if( request.JSON.content ) {
			page.content = request.JSON.content
		} else {
			page.content = ""
		}
		
		def existingPage = Page.findByUserAndTitle( params.userName, page.title )
		if(existingPage)
		{
			response.status = 400
			render "${message(code: 'custom.page.post.already.exists')}"
		} else if( page.save() ) {
			new ActivityLog(user:session.user.login, page:page.title, owner:page.user.login, activity:"create",
					app:request.getHeader("TypeLink-App"), appVersion:request.getHeader("TypeLink-Version") ).save()
			response.status = 201
			render page as JSON
		} else {
			response.status = 500
			render "${message(code: 'custom.page.post.error')}\n${page.errors}"
		}
    }
	
	def restGet = {
		restDelay()

		if( params.userName ) {
			if( params.pageTitle ) {
				def page = Page.findByUserAndTitle( params.userName, params.pageTitle.decodeURL())
				if( !page ) {
					response.status = 404
					render "${message(code: 'custom.page.get.not.found')}"
					return false
				} else if( !page.viewableBy(session.user) ) {
					response.status = 403
					render "${message(code: 'custom.page.get.unauthorized')}"
					return false
				} else {
					// log.error "teststring is "+session.teststring
					// log.error "session is "+session.id
			    	// log.error "session user is: "+session.user
					// log.error "request user is "+request.user
					new ActivityLog(user:session.user.login, page:page.title, owner:page.user.login, activity:"read",
							app:request.getHeader("TypeLink-App"), appVersion:request.getHeader("TypeLink-Version") ).save()
					def user = session.user ? session.user.login : null
					def map = [
						id:page.id,
						user:params.userName,
						title:page.title,
						publiclyVisible:page.publiclyVisible,
						sharedTo:page.sharedTo.collect{ it.user.login },
						aliases:page.aliases.collect{ it.name },
						content:page.content,
						wikiContent:g.wikify(myPage:page,user:user),
						font:page.font
					]
					render map as JSON
				}
			}
			else {
				def result = Page.findByUser(User.findByLogin(params.userName),session.user,"upper(p.title) asc",-1,-1)
				def list = result.collect {
					[ id:it.id, title:it.title, publiclyVisible:it.publiclyVisible,
						shared:(it.sharedTo && 0 < it.sharedTo.size() ) ]
				}
				render list as JSON
			}
		}
    }
	
	def restGetShared = {
		restDelay()
		
		if( !params.userName.equalsIgnoreCase(session.user.login) ) {
			response.status = 403
			render "${message(code: 'custom.page.get.shared.unauthorized')}"
			return false
		}
		
		def owner = session.user
		def result = Page.findBySharedTo( owner, -1, -1 )
		def list = result.collect {
			[ id:it.id, title:it.title, user:it.user.login, publiclyVisible:it.publiclyVisible,
				shared:(it.sharedTo && 0 < it.sharedTo.size() )]
		}
		render list as JSON
	}
	
	def restPut = {
		restDelay()

		def page = Page.findByUserAndTitle( params.userName, params.pageTitle.decodeURL() )
		if( !page ) {
			response.status = 404
			render "${message(code: 'custom.page.put.not.found')}"
			return false
		} else if( !page.editableBy(session.user) ) {
			response.status = 403
			render "${message(code: 'custom.page.put.unauthorized')}"
			return false
		}
				
		def existingPage = Page.findOtherByUserAndTitle( page.user.login, request.JSON.title, page.id )
		if( existingPage ) {
			response.status = 400 //Bad Request
			render "${message(code: 'custom.page.put.already.exists')}"
			return false
  		} else {
			def j = request.JSON;
			if( j.title != null ) { page.title = j.title } 
			if( j.content != null ) { page.content = j.content }
			if( j.publiclyVisible != null ) { page.publiclyVisible = j.publiclyVisible } 
			if( j.font != null ) {
				if( null != j.font.class
						&& 'org.codehaus.groovy.grails.web.json.JSONObject$Null'.equals(j.font.class.name) )
				{
					page.font = null
				} else {
					page.font = Font.get(j.font.id)
				}
			}

			if( page.save(flush: true) ) {
				new ActivityLog(user:session.user.login, page:page.title, owner:page.user.login, activity:"update",
					app:request.getHeader("TypeLink-App"), appVersion:request.getHeader("TypeLink-Version") ).save()
				response.status = 201
				def user = session.user ? session.user.login : null
				def newVersionPage = Page.get(page.id)
				def map = [
					id:page.id,
					version:newVersionPage.version,
					user:params.userName,
					title:page.title,
					publiclyVisible:page.publiclyVisible,
					content:page.content,
					wikiContent:g.wikify(myPage:page,user:user),
					font:page.font
				]
				render map as JSON
			} else {
				response.status = 500
				render "${message(code: 'custom.page.put.error')}\n${page.errors}"
			}
		}
    }
	
	def restDelete = {
		restDelay()

		if(params.userName && params.pageTitle) {
			def pageTitle = params.pageTitle.decodeURL()
			def page = Page.findByUserAndTitle( params.userName, pageTitle)
			if(!page) {
				response.status = 404
				render "${message(code: 'custom.page.delete.not.found')}"
				return false
			} else if( !page.editableBy(session.user) ) {
				response.status = 403
				render "${message(code: 'custom.page.delete.unauthorized')}"
				return false
			} else if(page.title.equalsIgnoreCase(Page.homePageTitle)) {
				response.status 402
                render "${message(code: 'custom.page.delete.home.page', args:[Page.homePageTitle])}"
			} else {
				new ActivityLog(user:session.user.login, page:page.title, owner:page.user.login, activity:"delete",
					app:request.getHeader("TypeLink-App"), appVersion:request.getHeader("TypeLink-Version") ).save()
				page.delete()
				render "${message(code: 'custom.page.delete.success')}"
			}
		} else {
			response.status = 400
			render "${message(code: 'custom.page.delete.bad.request')}"
		}
    }

    def create = {
        def pageInstance = new Page()
        pageInstance.properties = params
		if( pageInstance.title ) {
			pageInstance.content = "${message(code: 'custom.page.default.content', args:[pageInstance.title])}"
		}
        return [pageInstance: pageInstance]
    }

    def save = {
        def pageInstance = new Page(params)
		def user = session.user
		pageInstance.user = user
		pageInstance.title = pageInstance.title.trim()
		pageInstance.content = "Edit this page to describe ${pageInstance.title} here."
		
		if( (!ConfigurationHolder.config.personalwiki.basic.disabled) && !( user?.canCreatePage() ) ) {
			flash.message = "${message(code: 'custom.page.post.too.many')}"
			render(view: "create", model: [pageInstance: pageInstance])
			return
		}
		
		// check for existing page with that title
		def existingPage = Page.findByUserAndTitle( user.login, pageInstance.title )
		if(existingPage)
		{
            flash.message = "${message(code: 'custom.page.exists.message', args: [pageInstance.title])}"
            render(view: "create", model: [pageInstance: pageInstance])
		}
		else
		{
	        if (pageInstance.save(flush: true)) {
				new ActivityLog(user:session.user.login, page:pageInstance.title, owner:pageInstance.user.login, activity:"create", app:"web" ).save()
	            flash.message = "${message(code: 'default.created.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.title.encodeAsHTML()])}"
	            redirect(uri:"/"+session.user.login+"/"+pageInstance.title.encodeAsURL())
	        }
	        else {
	            render(view: "create", model: [pageInstance: pageInstance])
	        }
		}
    }

    def listByUser = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.offset ? params.int('offset') : 0
		def owner = User.findByLoginIlike(params.userName)
		if( null == owner ) {
			flash.message = "${message(code: 'custom.no.pages.found.message', args: [params.userName])}"
			return [pageInstanceList: null, pageInstanceTotal: 0];
		}
		def viewer = session.user
		def results = Page.findByUser( owner, viewer, "upper(p.title) asc", params.max, params.offset )
		if( results.size() == 0 ) {
			flash.message = "${message(code: 'custom.no.pages.found.message', args: [params.userName])}"
		}
		//def count = Page.executeQuery("select count(*) as n from Page as p where p.user = :owner", [owner:User.findByLogin(params.userName)])
		def count = Page.countByUser( owner, viewer )
        [pageInstanceList: results, pageInstanceTotal: count]
	}
	
	def listBySharedUser = {
		if( !params.userName.equalsIgnoreCase(session.user.login) ) {
			response.sendError(403)
			return false
		}
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.offset ? params.int('offset') : 0
		def owner = User.findByLoginIlike(params.userName)
		def results = Page.findBySharedTo( owner, params.max, params.offset )
		def count = Page.countBySharedTo( owner )
		if( results.size() == 0 ) {
			flash.message = "${message(code: 'custom.no.pages.found.message', args: [params.userName])}"
		}
        [pageInstanceList: results, pageInstanceTotal: count]
	}

    def showByUserAndTitle = {
		def pageInstance = Page.findByUserAndTitle( params.userName, params.pageTitle.decodeURL() )
        if (!pageInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.pageTitle.decodeURL()])}"
			if( session.user ) {
				redirect(uri:"/"+session.user.login+"/Home")
			} else {
				redirect(uri:"/session/login")
			}
        }
        else {
        	if( pageInstance.viewableBy( session.user ) )
			{
				new ActivityLog(user:session?.user?.login, page:pageInstance.title, owner:pageInstance.user.login, activity:"read", app:"web" ).save()
        		[pageInstance: pageInstance, alertList:Alert.findByActive(true)]
			}
			else
			{
				flash.dest = "/"+params.userName+"/"+params.pageTitle
				redirect(uri:"/session/login")
				//redirect(controller: "session", action: "login")
				return false
			}
        }
    }

    def edit = {
		def user = session.user
        def pageInstance = Page.get(params.id)
        if (!pageInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.id])}"
            redirect(action: "list")
        }
		else if( !pageInstance.editableBy(user) ) {
			response.sendError(403)
			return false
		}
        else {
            return [pageInstance: pageInstance, originalTitle:pageInstance.title, user:user]
        }
    }

    def update = {
        def pageInstance = Page.get(params.id)
		def originalTitle = pageInstance.title
        if (pageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (pageInstance.version > version) {
                    
                    pageInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'page.label', default: 'Page')] as Object[], "Another user has updated this Page while you were editing")
                    render(view: "edit", model: [pageInstance: pageInstance, originalTitle: originalTitle])
                    return
                }
            }
            pageInstance.properties = params
            pageInstance.title = pageInstance.title.trim()
			// check for existing page with that title
			def existingPage = Page.findOtherByUserAndTitle( pageInstance.user.login, pageInstance.title, pageInstance.id )
			if(existingPage)
			{
				println pageInstance.sharedTo // to lazily load
				pageInstance.discard() // to prevent auto-saving
	            flash.message = "${message(code: 'custom.user.page.exists.message', args: [pageInstance.user.login, pageInstance.title.encodeAsHTML()])}"
	            render(view: "edit", model: [pageInstance: pageInstance, originalTitle: originalTitle])
				return false
			}
			else if( !pageInstance.editableBy(session.user) ) {
				response.sendError(403)
				return false
			}
			else
			{
	            if (!pageInstance.hasErrors() && pageInstance.save(flush: true)) {
	            	new ActivityLog(user:session.user.login, page:pageInstance.title, owner:pageInstance.user.login, activity:"update", app:"web" ).save()
	                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.title.encodeAsHTML()])}"
	                redirect(uri:"/"+pageInstance.user.login+"/"+pageInstance.title.encodeAsURL())
	            }
	            else {
	                render(view: "edit", model: [pageInstance: pageInstance, originalTitle: originalTitle])
	            }
			}
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.id])}"
            redirect(uri:"/"+params.userName)
        }
    }

    def editSettings = {
		def user = session.user
        def pageInstance = Page.get(params.id)
		def appRoot = ConfigurationHolder.config.personalwiki.app.root
        if (!pageInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.id])}"
            redirect(action: "list")
        }
		else if( !pageInstance.editableBy(user) ) {
			response.sendError(403)
			return false
		}
        else {
            return [pageInstance: pageInstance, originalTitle:pageInstance.title, user:user, appRoot:appRoot]
        }
    }

    def updateSettings = {
        def pageInstance = Page.get(params.id)
		def originalTitle = pageInstance.title
        if (pageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (pageInstance.version > version) {
                    
                    pageInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'page.label', default: 'Page')] as Object[], "Another user has updated this Page while you were editing")
                    render(view: "edit", model: [pageInstance: pageInstance, originalTitle: originalTitle])
                    return
                }
            }
            pageInstance.properties = params
            pageInstance.title = pageInstance.title.trim()
			// check for existing page with that title
			def existingPage = Page.findOtherByUserAndTitle( pageInstance.user.login, pageInstance.title, pageInstance.id )
			if(existingPage)
			{
				println pageInstance.sharedTo // to lazily load
				pageInstance.discard() // to prevent auto-saving
	            flash.message = "${message(code: 'custom.user.page.exists.message', args: [pageInstance.user.login, pageInstance.title.encodeAsHTML()])}"
	            render(view: "edit", model: [pageInstance: pageInstance, originalTitle: originalTitle])
				return false
			}
			else if( pageInstance.user != session.user ) {
				response.sendError(403)
				return false
			}
			else
			{
	            if (!pageInstance.hasErrors() && pageInstance.save(flush: true)) {
	                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.title.encodeAsHTML()])}"
	                redirect(uri:"/"+pageInstance.user.login+"/"+pageInstance.title.encodeAsURL())
	            }
	            else {
	                render(view: "edit", model: [pageInstance: pageInstance, originalTitle: originalTitle])
	            }
			}
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.id])}"
            redirect(uri:"/"+params.userName)
        }
    }

    def delete = {
        def pageInstance = Page.get(params.id)
        if (pageInstance) {
			if(pageInstance.title.equalsIgnoreCase(Page.homePageTitle))
			{
                flash.message = "You cannot delete your ${Page.homePageTitle} page"
                redirect(uri:"/"+session.user.login+"/"+pageInstance.title.encodeAsURL())
				return
			}
            try {
				new ActivityLog(user:session.user.login, page:pageInstance.title, owner:pageInstance.user.login, activity:"delete", app:"web" ).save()
                pageInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.title.encodeAsHTML()])}"
                redirect(uri:"/"+session.user.login)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.title.encodeAsHTML()])}"
                redirect(uri:"/"+session.user.login+"/"+pageInstance.title.encodeAsURL())
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), params.id])}"
            redirect(uri:"/"+session.user.login)
        }
    }
	
	def exportByUser = {
		if( !params.userName.equalsIgnoreCase(session.user.login) ) {
			response.sendError(403)
			return false
		}
		def pages = Page.findByUser( session.user, session.user, "p.title asc", -1, -1 );
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream()
		ZipOutputStream out = new ZipOutputStream(byteOut)
		PrintWriter writer = new PrintWriter(out)
		
		byte[] buf = new byte[1024]
		int len
		pages.each {
			//println "creating entry " + it.title + ".txt"
			out.putNextEntry( new ZipEntry( it.title + ".txt" ) )
			writer.write( it.content )
			
			writer.flush()
			out.closeEntry()
		}
		
		out.close()
		
		response.setContentType "application/zip"
		response.setHeader "Content-disposition", "attachment; filename=${session.user.login}-TypeLink.zip"
		ServletOutputStream servletOut = response.getOutputStream()
	    servletOut.write(byteOut.toByteArray())
	    servletOut.close()
    }
	
	def importByUser = { }
	
	def importPost = {
		
		def results = []
		
		ZipInputStream zis = new ZipInputStream(request.getFile("file").inputStream)
        ZipEntry ze
		String name
		String title
		String content
		Page page
		Matcher m
		
        while((ze=zis.getNextEntry())!=null){
			name = ze.getName()
        	println name
			if( !( name =~ /^__MACOSX/ )
				&& name =~ /\.txt$/ )
			{
				content = convertInputStreamToString(zis)
				//println content
				
				m = ( name =~ /([^\/]+)\.txt$/ )
				if( m ) {
					title = m.group(1)
					//println "title: ${title}"
					
					if( Page.findByUserAndTitle( session.user.login, title ) ) {
						results.add( [ name: name, success: false, result: "already exists" ] )
					} else {
						page = new Page( user:session.user,
										 title:title,
										 content:content )
						if( page.save() ){
							results.add( [ name: name, success: true, result: "imported" ] )
						} else {
							results.add( [ name: name, success: false, result: "error: " + page.errors ] )
						}
					}
				} else {
					println "title not matched"
				}
			}
        }

        zis.close()
		
        return [results:results]
	}
	
	def convertInputStreamToString = { is ->
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.closeEntry(); // for zip input streams only
            }
            return writer.toString();
        } else {        
            return "";
        }
    }
	
}
