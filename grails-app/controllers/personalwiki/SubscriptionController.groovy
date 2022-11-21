package personalwiki

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

class SubscriptionController {

    static allowedMethods = [save: "GET"]

    def index = {
	    def userInstance = session.user
        if (!userInstance) {
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
			flash.dest = "/account/subscribe"
            redirect(uri: "/session/login")
        }
		[accountType: userInstance.accountType()]
    }

    def list = {
		def userInstance = session.user
        if (!userInstance) {
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
			flash.dest = "/account/history"
            redirect(uri: "/session/login")
        }
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def subscriptions = Subscription.findAllByUser(userInstance, [sort:"purchaseDate"])
		def subscriptionCount = Subscription.executeQuery("select count(*) from Subscription as s where s.user = ?",[userInstance])[0]
        [subscriptionInstanceList: subscriptions, subscriptionInstanceTotal: subscriptionCount, accountType: userInstance.accountType()]
    }

    def save = {
		// confirm incoming transaction ID with paypal to make sure it's valid
		def tID = params.tx
		def result = getPDTRecord( tID )
		if( !result || "FAIL" == result['status'] )
		{
			flash.message = "${message(code: 'custom.subscription.transaction.invalid')}"
            render(view: "index")
			return
		}
		
		// check to make sure transaction ID not already used in our records
		if( null != Subscription.findByTransactionId(tID) )
		{
			flash.message = "${message(code: 'custom.subscription.transaction.already.exists')}"
            render(view: "index")
			return
		}
		
		// create subscription
		def now = new Date()
        def subscriptionInstance = new Subscription()
		subscriptionInstance.user = session?.user
		subscriptionInstance.transactionId = tID
		subscriptionInstance.payerEmail = result['payer_email']
		subscriptionInstance.purchaseDate = now
		
		// get start and end dates
		def lastSub = Subscription.getLastByUser(session?.user)
		if( null == lastSub || lastSub.endDate.before(now) ) {
			subscriptionInstance.startDate = now
		} else {
			subscriptionInstance.startDate = lastSub.endDate
		}
		subscriptionInstance.endDate = subscriptionInstance.startDate + 365 // 1.years should work in new grails
		
		// save
        if (subscriptionInstance.save(flush: true)) {
            //flash.message = "${message(code: 'default.created.message', args: [message(code: 'subscription.label', default: 'Subscription'), subscriptionInstance.id])}"
            redirect(action: "show", id: subscriptionInstance.id)
        }
        else {
			flash.message = "${message(code: 'custom.subscription.save.error')}"
            render(view: "index")
        }
    }
	
	def getPDTRecord = { tID ->
		def client = new HTTPBuilder( ConfigurationHolder.config.personalwiki.paypal.pdt.server,
			groovyx.net.http.ContentType.TEXT )
		client.post( path: ConfigurationHolder.config.personalwiki.paypal.pdt.path,
			body: [
				"cmd":"_notify-synch",
				"tx":tID,
				"at":ConfigurationHolder.config.personalwiki.paypal.pdt.id
			],
			requestContentType: groovyx.net.http.ContentType.URLENC )
		{ resp, reader ->
			def responseText = reader.text
			//println responseText
			
			def lines = responseText.split(/[\r\n]+/)
			def responseMap = ["status":lines[0]]
			lines.each{ line ->
				def tokens = line.split(/=/)
				if( tokens.size() >= 2 ) {
					//println "${tokens[0]} = ${tokens[1].decodeURL()}"
					responseMap[tokens[0]] = tokens[1].decodeURL()
				}
			}
			
			return responseMap
		}
	}

    def show = {
        def subscriptionInstance = Subscription.get(params.id)
        if (!subscriptionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'subscription.label', default: 'Subscription'), params.id])}"
            redirect(action: "list")
        }
        else {
            [subscriptionInstance: subscriptionInstance]
        }
    }

}
