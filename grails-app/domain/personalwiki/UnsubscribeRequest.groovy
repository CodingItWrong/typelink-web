package personalwiki

class UnsubscribeRequest {
	
	String email
	Date requestDate

    static constraints = {
		email(blank:false)
		requestDate(nullable:false)
    }
}
