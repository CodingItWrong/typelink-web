package personalwiki

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AliasController {

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
				def aliasInstance = new Alias(page:page,name:params.alias.decodeURL())
		        if (aliasInstance.save(flush: true)) {
					render "Alias added"
		        }
		        else {
					response.status = 500
					render "Could not save alias due to errors:\n${aliasInstance.errors}"
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
			def alias = Alias.findByPageAndName( page, params.alias.decodeURL() )
			alias.delete()
			render "Successfully deleted"
		}
    }
	
    def create = {
        def aliasInstance = new Alias()
        aliasInstance.properties = params
        return [aliasInstance: aliasInstance]
    }

    def save = {
        def aliasInstance = new Alias(params)
		
		if( aliasInstance.page.user != session.user )
		{
			response.sendError(403)
			return false
		}
        if (aliasInstance.save(flush: true)) {
            flash.message = "${message(code: 'custom.alias.created.message', args: [aliasInstance?.name])}"
            redirect(controller:"page", action:"editSettings", id:aliasInstance?.page?.id)
        }
        else {
            render(view: "create", model: [aliasInstance: aliasInstance])
        }
    }

    def show = {
        def aliasInstance = Alias.get(params.id)
        if (!aliasInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'alias.label', default: 'Alias'), params.id])}"
            redirect(action: "list")
        }
        else {
            [aliasInstance: aliasInstance]
        }
    }

    def delete = {
        def aliasInstance = Alias.get(params.id)
        if (aliasInstance) {
			if( aliasInstance.page.user != session.user )
			{
				response.sendError(403)
				return false
			}
			def pageId = aliasInstance?.page?.id
			def alias = aliasInstance?.name
            try {
                aliasInstance.delete(flush: true)
                flash.message = "${message(code: 'custom.alias.deleted.message', args: [alias])}"
                redirect(controller:"page", action:"editSettings", id:pageId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'alias.label', default: 'Alias'), params.id])}"
                redirect(controller:"page", action:"edit", id:pageId)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'alias.label', default: 'Alias'), params.id])}"
            redirect(action: "list")
        }
    }
}
