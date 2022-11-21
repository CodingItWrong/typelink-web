package personalwiki

class ActivityLog {

	String user
	String page
	String owner
	String activity
	String app
	String appVersion
	Date activityDate = new Date()

    static constraints = {
		user(nullable:true)
		page(nullable:true)
		owner(nullable:true)
		app(nullable:true)
		appVersion(nullable:true)
    }
}
