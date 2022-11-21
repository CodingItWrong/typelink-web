package personalwiki

class SignupForm implements Serializable {
	String login
	String email
	String password
	String passwordConfirm
	
	boolean rememberMe
	
	static constraints = {
		login(size:3..15, blank:false, nullable: false)
		email(email:true, size:6..200, blank:false, nullable: false)
		password(size:6..40, password:true, blank:false, nullable: false)
		passwordConfirm(password:true, validator: { val, obj -> if( obj.password != val ) { return "error.password.confirm" } })
	}
}
