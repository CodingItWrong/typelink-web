import grails.util.GrailsUtil

import personalwiki.User;

class AuthenticationFilters {
	def authenticationService

	def filters = {
		sessionUserFilter(controller:"*", action:"rest.*", invert:"true") {
			before = {
				if(authenticationService.isLoggedIn(request)) {
					def login = authenticationService.getSessionUser().login
					// log.error "AuthenticationFilters setting session user"
					session.user = User.findByLoginIlike(login)
				} else {
					// log.error "AuthenticationFilters setting session user to null"
					session.user = null
				}
				return true;
			}
		}
		pageFilter(controller:"page", action:"(create|save|edit|update|delete|listBySharedUser|importByUser|exportByUser)") {
			before = {
				if(!authenticationService.isLoggedIn(request)) {
					redirect(controller:"session",action:"login")
					return false
				} else return true
			}
		}
	}
}
