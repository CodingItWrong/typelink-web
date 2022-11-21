package personalwiki

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class User {

	static String TYPE_UNLIMITED = "U"

    static constraints = {
		login(blank:false,minSize:3,maxSize:15,matches:"[^/\\\\]+",unique:true)
		password(blank:false) // no min and max size, because hashed values constant length
		email(blank:false,maxSize:200,email:true)
		status()
		defaultFont()
		type(nullable:true,maxSize:1)
		sendEmails(nullable:false)
    }
	
	String login
	String password
	String email
	int status
	Font defaultFont
	String type
	boolean sendEmails = true
	
	String toString() {
		login
	}
	
	def canCreatePage = {
		// unlimited users can always create pages
		if( TYPE_UNLIMITED == type ) {
			return true;
		}
		
		// other users can create up to 10 pages
		def numPages = Page.executeQuery("select count(*) from Page as p where p.user = ?",[this])[0]
		// countBy in newer grails
		if( numPages < ConfigurationHolder.config.personalwiki.basic.pages ) {
			return true;
		}
		
		// if more than 10 pages, they must have a subscription
		def lastSubscription = Subscription.getLastByUser(this)
		def now = new Date();
		return lastSubscription != null && lastSubscription.endDate.after(now)
	}
	
	def accountType = {
		if( TYPE_UNLIMITED == type ) {
			return "pro.unlimited";
		}

		def lastSubscription = Subscription.getLastByUser(this)
		def now = new Date();
		if( lastSubscription != null && lastSubscription.endDate.after(now) ) {
			return "pro";
		} else {
			return "basic";
		}
	}
}
