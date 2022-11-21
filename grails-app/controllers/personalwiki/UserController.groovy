package personalwiki

import grails.converters.*
import org.springframework.context.MessageSourceResolvable
import org.springframework.context.NoSuchMessageException
import org.springframework.web.servlet.support.RequestContextUtils as RCU

class UserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def restPost = {
		def user = new User()
		user.login = params.userName
		def j = request.JSON
		user.email = j.email
		user.password = j.password.encodeAsSHA1()
		user.defaultFont = Font.findByName("Helvetica")
		
		if( user.save() ) {
			def homePage = Page.createHomePage(user) 
			if( homePage.save() ) {
				response.status = 201
				render user as JSON
			} else {
				response.status = 500
				render "${message(code: 'custom.user.post.error')}\n${homePage.errors}"
			}
		} else {
			println user.errors
			response.status = 500
			render user.errors.allErrors.collect { message(error:it,encodeAs:'HTML') } as JSON
		}
	}

    def restGet = {
		// println "user restGet"
		def map = [
			login: session.user.login,
			email: session.user.email,
			defaultFont: session.user.defaultFont,
			sendEmails: session.user.sendEmails
		]
		render map as JSON
	}

	def restPut = {
		def userInstance = session.user
        if (!userInstance) {
			// shouldn't happen
			response.status = 404
			return false
        }

		def j = request.JSON;
		if( j.email != null ) { userInstance.email = j.email }
		if( j.sendEmails != null ) { userInstance.sendEmails = j.sendEmails }
		if( j.defaultFont != null ) {
			if( null != j.defaultFont.class
					&& 'org.codehaus.groovy.grails.web.json.JSONObject$Null'.equals(j.font.class.name) )
			{
				userInstance.defaultFont = null
			} else {
				userInstance.defaultFont = Font.get(j.defaultFont.id)
			}
		}
		if( userInstance.save(flush: true) ) {
			new ActivityLog(user:session.user.login, page:null, owner:null, activity:"update",
				app:request.getHeader("TypeLink-App"), appVersion:request.getHeader("TypeLink-Version") ).save()
			response.status = 201
			def newVersionUser = User.get(userInstance.id)
			def map = [
				login: newVersionUser.login,
				email: newVersionUser.email,
				defaultFont: newVersionUser.defaultFont,
				sendEmails: newVersionUser.sendEmails
			]
			render map as JSON
		} else {
			response.status = 500
			render "${message(code: 'custom.user.put.error')}\n${page.errors}"
		}
	}

    def edit = {
        def userInstance = session.user
        if (!userInstance) {
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
			flash.dest = "/account"
            redirect(uri: "/session/login")
        }
        else {
            return [userInstance: userInstance, accountType: userInstance.accountType()]
        }
    }
	
    def update = {
        def userInstance = session.user
        if (userInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userInstance.version > version) {
                    
                    userInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'user.label', default: 'User')] as Object[], "Another user has updated this User while you were editing")
                    render(view: "edit", model: [userInstance: userInstance])
                    return
                }
            }
            
			// set new values
			userInstance.email = params.email
			userInstance.defaultFont = Font.get(params.defaultFont.id)
			userInstance.sendEmails = (null != params.sendEmails)
			if( !"".equals(params.password) && params.password.equals(params.confirmPassword) ) {
				userInstance.password = params.password.encodeAsSHA1()
			}

            if (userInstance.hasErrors() ) {
                render(view: "edit", model: [userInstance: userInstance])
            } else if( !"".equals(params.password) && !params.password.equals(params.confirmPassword) ) {
            	flash.message = "You did not enter the same password in both fields"
                render(view: "edit", model: [userInstance: userInstance])
            } else if( userInstance.save(flush: true)) {
                flash.message = "${message(code: 'custom.user.updated.message')}"
                redirect(uri:"/account")
            } else {
            	render(view: "edit", model: [userInstance: userInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
    }
}
