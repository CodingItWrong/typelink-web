package personalwiki

import cr.co.arquetipos.password.PasswordTools
import com.grailsrocks.authentication.AuthenticatedUser

class SessionController {
	def authenticationService

	def index = {
		if( session.user ) {
			redirect(uri:"/"+session.user.login+"/"+Page.homePageTitle.encodeAsURL())
		} else {
			redirect(action:"login")
		}
	}
	
	def login = { }
	
	def signup = { }
	
	def forgotPassword = {
		def user
		if( !params.login ) {
			flash.message = "${message(code: 'custom.user.forgot.password.login.missing')}"
		} else if( !( user = User.findByLogin(params.login) ) ) {
			flash.message = "${message(code: 'custom.user.forgot.password.not.found')}"
		} else {
			// reset password
			def passwordClear = PasswordTools.generateRandomPassword(12)
			user.password = passwordClear.encodeAsSHA1()
			if( !user.save() ) {
				flash.message = "${message(code: 'custom.user.forgot.password.error')}\n${user.errors}"
			} else {
				// send password
				sendMail {
					from "noreply@typelink.net"
					to user.email
					subject "Typelink.net Password Reset"
					body g.render(template:"mail/passwordReset",model:[password:passwordClear])
				}
				flash.message = "${message(code: 'custom.user.forgot.password.success')}"
			}
		}
		redirect(action:"login")
	}
	
	/* copied from AuthenticationService */
    /**
     * Extract success_* and error_* into maps that can be passed to redirect(),
     * but forbidding the use of "url" which could lead to XSS attacks or phishing
     */
    def extractParams() {
        def redirectParams = [success:[:], error:[:]]
        params.keySet().each() { name -> 
            if (name.startsWith("success_") || name.startsWith('error_')) {
                def underscore = name.indexOf('_')
                if (underscore >= name.size()-1) return
                def prefix = name[0..underscore-1]
                def urlParam = name[underscore+1..-1]
                if (urlParam != 'url') {
                    redirectParams[prefix][urlParam] = params[name]
                }
            }
        }
        return redirectParams
    }

	def doLogin = { LoginForm form ->
	    def urls = extractParams()
		if (!form.hasErrors()) {
			def loginResult = authenticationService.login( form.login, form.password)
			if (loginResult.result == 0) {
				new ActivityLog(user:form.login, activity:"login", app:"web" ).save()
				
				flash.loginForm = form
				if (log.debugEnabled) log.debug("Login succeeded for [${form.login}]")
				redirect( form.dest ? [uri:form.dest] : ( flash.authSuccessURL ? flash.authSuccessURL : urls.success) )
			} else {                  
				flash.loginForm = form
				flash.authenticationFailure = loginResult
				if (log.debugEnabled) log.debug("Login failed for [${form.login}] - reason: ${loginResult.result}")
				redirect(flash.authFailureURL ? flash.authFailureURL : urls.error)
			}
		} else {
			flash.loginForm = form
			flash.loginFormErrors = form.errors // Workaround for grails bug 
			if (log.debugEnabled) log.debug("Login failed for [${form.login}] - form invalid: ${form.errors}")
			redirect(flash.authErrorURL ? flash.authErrorURL : urls.error)
		}
	}

	def doSignup = { SignupForm form ->
	    def urls = extractParams()
		if (!form.hasErrors()) {
			def signupResult = authenticationService.signup( login:form.login, 
				password:form.password, email:form.email, immediate:true, extraParams:params)
			if ((signupResult.result == 0) || (signupResult.result == AuthenticatedUser.AWAITING_CONFIRMATION)) {
				new ActivityLog(user:form.login, activity:"signup", app:"web" ).save()
				
				if (log.debugEnabled) {
					if (signupResult == AuthenticatedUser.AWAITING_CONFIRMATION) {
						log.debug("Signup succeeded pending email confirmation for [${form.login}] / [${form.email}]")
					} else {
						log.debug("Signup succeeded for [${form.login}]")
					}
				}
				redirect(flash.authSuccessURL ? flash.authSuccessURL : urls.success)
			} else {
				flash.authenticationFailure = signupResult
				flash.signupForm = form
				if (log.debugEnabled) log.debug("Signup failed for [${form.login}] reason ${signupResult.result}")
				redirect(flash.authErrorURL ? flash.authErrorURL : urls.error)
			}
		} else {
			flash.signupForm = form
			flash.signupFormErrors = form.errors // Workaround for grails bug in 0.5.6
			if (log.debugEnabled) log.debug("Signup failed for [${form.login}] - form invalid: ${form.errors}")
			redirect(flash.authErrorURL ? flash.authErrorURL : urls.error)
		}		
	}
	/* end copied from AuthenticationService */
}
