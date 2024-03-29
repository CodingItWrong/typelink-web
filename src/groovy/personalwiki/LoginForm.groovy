package personalwiki

class LoginForm implements Serializable {
	String login
	String password
	String dest
	
	boolean rememberMe
	
	static constraints = {
		login(size:3..40, nullable: false, blank:false)
		password(size:6..40, password:true, nullable: false, blank:false)
	}
}
