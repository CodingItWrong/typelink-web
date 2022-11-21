import java.io.PrintWriter;

import personalwiki.User

class RestAuthenticationFilters {
	def authenticationService

	// check java session authentication
	String checkAuth(request,response,session) {
		// log.error "running checkAuth"
		if( authenticationService.isLoggedIn(request) ) {
			def myUser = authenticationService.getSessionUser().login
			session.user = User.findByLoginIlike( myUser )
			return session.user
		}
		
		// check basic authentication (without base 64
		def authString = request.getHeader("Authorization")
		if( !authString ) {
			return null
		}
		
		/*
		def encodedPair = authString - "Basic "
		def decodedPair = new String(new sun.misc.BASE64Decoder().decodeBuffer(encodedPair))
		def credentials = decodedPair.split(':')
		def username = credentials[0]
		def password = credentials[1]
		*/
		
		def decodedPair = authString
		// log.error "checking "+decodedPair
		//println decodedPair
		def credentials = decodedPair.split(':')
		if( 2 != credentials.length )
		{
			return null
		}
		def username = credentials[0]
		def password = credentials[1].encodeAsSHA1()
		// log.error "password "+password
		
		def user = User.findByLoginAndPassword(username,password)
		// log.error "user "+user
		
		if(user){
			session.user = user
			// log.error "set session.user to "+session.user
			// session.teststring = "mytest"
			// log.error "set teststring to "+session.teststring
			return user
		} else {
			response.status = 403
			PrintWriter w = response.getWriter()
			w.write("Invalid username or password")
			w.flush()
			w.close()
			return false
		}
		
		user
	}
	
	def filters = {
		openAuth(controller:"page", action:"restGet") {
			before = {
				def user = checkAuth(request,response,session);
				request.user = user
				// log.error "session.user is now "+session.user
				// log.error "session is "+session.id
				// log.error "request user is "+request.user
				
				// do not check if logged in
			}
		}
		basicAuth(controller:"(page|share|alias)", action:"(restPut|restPost|restDelete|restGetShared)") {
			before = {
				def user = checkAuth(request,response,session);
				
				if(!user){
					response.sendError(403)
					return false
				}
			}
		}
		basicAuth(controller:"(user)", action:"(restGet|restPut)") {
			before = {
				def user = checkAuth(request,response,session);
				
				if(!user){
					response.sendError(403)
					return false
				}
			}
		}
	}
}
