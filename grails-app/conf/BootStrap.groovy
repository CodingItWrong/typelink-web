import org.codehaus.groovy.tools.shell.commands.SaveCommand;

import grails.util.GrailsUtil
import java.text.SimpleDateFormat

import personalwiki.*

class BootStrap {

	def authenticationService;

    def init = { servletContext ->
		// auth service customization
		authenticationService.events.onFindByLogin = { loginID -> User.findByLoginIlike(loginID) }
		authenticationService.events.onNewUserObject = {
			loginID -> def obj = User.newInstance()
			obj.login = loginID
			obj.defaultFont = Font.findByName("Helvetica")
			return obj
		}
		authenticationService.events.onSignup = { params ->
			Page.createHomePage( params.user ).save()
		}
		
		// mock data
		// run in prod, but only once
		if( 0 == Font.count() ) {
			new Font( name: "Courier New", iOSCode: "CourierNewPSMT", cssCode: "\"Courier New\", Courier, monospace").save()
			new Font( name: "Helvetica", iOSCode: "Helvetica", cssCode: "Helvetica, Arial, sans-serif").save()
			new Font( name: "Baskerville", iOSCode: "Baskerville", cssCode: "Baskerville, \"Times New Roman\", Times, serif").save()
			new Font( name: "Marker Felt", iOSCode: "MarkerFelt-Thin", cssCode: "\"Marker Felt\", \"Comic Sans MS\", sans-serif").save()
			new Font( name: "Times New Roman", iOSCode: "TimesNewRomanPSMT", cssCode: "\"Times New Roman\", Times, serif").save()
		}
		switch(GrailsUtil.environment) {
			case "development":
		
				new Alert( active: true, text:"This is the development version of the site. Do not put real data in here.").save()
				new Alert( active: false, text:"Inactive alert.").save()
			
				def u1 = new User( login:'jjustice', password:"password".encodeAsSHA1(),
					email:'josh@joshjustice.com', status:authenticationService.STATUS_VALID,
					defaultFont: Font.findByName("Helvetica"), type:User.TYPE_UNLIMITED )
				u1.save()
				new Page(user:u1,title:"Home",publiclyVisible:true,content:
					"This is my index page, showing stuff. Hello there!\n" +
					"<hr/>\n" +
					"Josh Public\n" +
					"Josh Shared to Bono\n" +
					"Josh Private\n" +
					"http://grails.org/doc/latest/guide/5.%20Object%20Relational%20Mapping%20(GORM).html\n" +
					"josh@joshjustice.com\n" +
					"@jjustice\n" +
					"http://i52.tinypic.com/b4zqls.jpg\n" +
					"http://www.youtube.com/watch?v=Ji3BYXAQSrk\n" +
					"“smart” quotes").save()
				def jp = new Page(user:u1,publiclyVisible:true,title:"Josh Public",content:"Josh Public").save()
				def j2b = new Page(user:u1,title:"Josh Shared to Bono",content:"Josh Shared to Bono").save()
				new Page(user:u1,title:"Josh Private",content:"Josh Private").save()
				
				def u2 = new User( login:'bono', password:"password".encodeAsSHA1(),
					email:'bono@u2.com', status:authenticationService.STATUS_VALID,
					defaultFont: Font.findByName("Courier New") )
				u2.save()
				new Page(user:u2,title:"Home",content:"This is Bono's index page for fun things but not sermons\n12345678901234567890|\n--------------------|\nabcdefghijklmnopqrst|\n                    |\n????????????????????|\n").save()
				new Page(user:u2,title:"Fun Things",content:"This is Bono's fun things page").save()
				
				def u3 = new User( login:'paid', password:'password'.encodeAsSHA1(),
					email:'paid@paid.com', status:authenticationService.STATUS_VALID,
					defaultFont: Font.findByName("Helvetica") ).save()
				def df = new SimpleDateFormat("MM/dd/yyyy")
				def subscr = new Subscription( user:u3, transactionId:"123", purchaseDate:df.parse("1/1/2011"),
					startDate:df.parse("1/1/2011"), endDate:df.parse("12/31/2011"), payerEmail:"foo@bar.com" )
				if( !subscr.save() ) {
					println subscr.errors
				}
				new Page(user:u3,title:"Home",content:"This is the paid user's page").save()
				
				def u4 = new User( login:'expired', password:'password'.encodeAsSHA1(),
					email:'expired@paid.com', status:authenticationService.STATUS_VALID,
					defaultFont: Font.findByName("Helvetica") ).save()
				subscr = new Subscription( user:u4, transactionId:"234", purchaseDate:df.parse("1/1/2010"),
					startDate:df.parse("1/1/2010"), endDate:df.parse("1/1/2011"), payerEmail:"baz@bar.com" )
				if( !subscr.save() ) {
					println subscr.errors
				}
				new Page(user:u4,title:"Home",content:"This is the expired user's page").save()
				
				new Share(page:j2b,user:u2).save()
				
				new Alias(page:jp,name:"hello").save()
				new Alias(page:jp,name:"there").save()
// disabled because they throw off selenium
//				(1..20).each {
//					println "Filler ${it}"
//					def p = new Page(user:u1,title:"Filler Page ${it}",content:"This is a filler page.").save()
//					new Share(page:p,user:u2).save()
//				}
		}
    }
    def destroy = {
    }
}

