package personalwiki

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class ShareController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    def restDelay = {
		def restDelay = ConfigurationHolder.config.personalwiki.rest.delay
		if( restDelay > 0 ) {
			Thread.sleep(restDelay*1000);
		}
    }
	
    def restPost = {
		restDelay()
		
		if( !params.userName.equalsIgnoreCase(session?.user?.login) ) {
			response.sendError(403)
			return false
		} else if( !params.pageTitle ) {
			response.sendError(402)
			return false
		} else {
			def page = Page.findByUserAndTitle( params.userName, params.pageTitle.decodeURL())
			if( !page ) {
				response.sendError(404)
				return false
			} else {
				def user = User.findByLogin(params.shareUserName)
				if( null == user ) {
					response.status = 402
					render "Could not find a user with that name"
					return false;
				}
				def shareInstance = new Share(page:page,user:user)
		        if (shareInstance.save(flush: true)) {
					render "Share added"
		        }
		        else {
					response.status = 500
					render "Could not save share due to errors:\n${shareInstance.errors}"
					return false
		        }
			}
		}
    }
	
    def restDelete = {
		restDelay()
		
		if( !params.userName.equalsIgnoreCase(session?.user?.login) ) {
			response.sendError(403)
			return false
		} else if( !params.pageTitle ) {
			response.sendError(402)
			return false
		} else {
			def page = Page.findByUserAndTitle( params.userName, params.pageTitle.decodeURL() )
			def user = User.findByLogin( params.shareUserName )
			def share = Share.findByPageAndUser( page, user )
			share.delete()
			render "Successfully deleted"
		}
    }
	
    def create = {
        def shareInstance = new Share()
        shareInstance.properties = params
        return [shareInstance: shareInstance]
    }

    def save = {
        def shareInstance = new Share(params)
		
		def user = User.findByLogin(params.username)
		
		if( null == user ) {
            flash.message = "${message(code: 'custom.share.user.not.found.message', args: [params.username])}"
            render(view: "create", model: [shareInstance: shareInstance])
			return
		} else {
			shareInstance.user = user
		}
		
		if( shareInstance.page.user != session.user )
		{
			response.sendError(403)
			return false
		}
        if (shareInstance.save(flush: true)) {
            flash.message = "${message(code: 'custom.shared.to.message', args: [shareInstance?.user?.login])}"
            redirect(controller:"page", action:"editSettings", id:shareInstance?.page?.id)
        }
        else {
            render(view: "create", model: [shareInstance: shareInstance])
        }
    }

    def show = {
        def shareInstance = Share.get(params.id)
        if (!shareInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'share.label', default: 'Share'), params.id])}"
            redirect(action: "list")
        }
        else {
            [shareInstance: shareInstance]
        }
    }

    def delete = {
        def shareInstance = Share.get(params.id)
        if (shareInstance) {
			if( shareInstance.page.user != session.user )
			{
				response.sendError(403)
				return false
			}
			def pageId = shareInstance?.page?.id
			def userName = shareInstance?.user?.login
            try {
                shareInstance.delete(flush: true)
                flash.message = "${message(code: 'custom.unshared.message', args: [userName])}"
                redirect(controller:"page", action:"editSettings", id:pageId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'share.label', default: 'Share'), params.id])}"
                redirect(controller:"page", action:"edit", id:pageId)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'share.label', default: 'Share'), params.id])}"
            redirect(action: "list")
        }
    }
}
