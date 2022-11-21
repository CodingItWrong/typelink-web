package personalwiki

class Share {

	User user

	static belongsTo = [ page: Page ]

    static constraints = {
		page()
		user(nullable:false)
    }
	
	String toString() {
		user
	}
}
