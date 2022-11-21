package personalwiki

class Subscription {

	User user
	String payerEmail
	String transactionId
	Date purchaseDate
	Date startDate
	Date endDate

    static constraints = {
		user(nullable:false)
		payerEmail(blank:false,maxSize:200)
		transactionId(blank:false,unique:true,maxSize:50)
		purchaseDate(nullable:false)
		startDate(nullable:false)
		endDate(nullable:false)
    }
	
	static getLastByUser = { user ->
		Subscription.findAll(
			"from Subscription as s where s.user = ? and s.endDate =" +
			"(select max(s2.endDate) from Subscription as s2 where s2.user = ?)",
			[user,user])[0]
	}
}
