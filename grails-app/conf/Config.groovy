// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	appenders {
		production {
			rollingFile name:"stacktrace", maxFileSize: 1024,
						file: "/var/log/tomcat6/typelink.log"
		}
	}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

// auth
authenticationUserClass = personalwiki.User

environments {
	development {
		personalwiki.app.domain = "http://localhost:8080"
		personalwiki.app.root = "/typelink"
		personalwiki.env.label = "Dev"
		personalwiki.rest.delay = 1
		personalwiki.analytics.disabled = true
		personalwiki.social.disabled = true
		personalwiki.unload.disabled = true
		personalwiki.basic.disabled = false
		personalwiki.basic.pages = 2
		personalwiki.paypal.sandbox = true
		personalwiki.paypal.pdt.server = "https://www.sandbox.paypal.com/cgi-bin/webscr"
		personalwiki.paypal.pdt.path = ""
		personalwiki.paypal.pdt.id = "YOUR_PAYPAL_ID"
		personalwiki.paypal.account.url = "http://www.sandbox.paypal.com/us"
	}
	test {
		personalwiki.app.domain = "http://localhost:8080"
		personalwiki.app.root = "/typelink"
		personalwiki.env.label = "Test"
		personalwiki.rest.delay = 1
		personalwiki.analytics.disabled = true
		personalwiki.social.disabled = true
		personalwiki.unload.disabled = true
		personalwiki.basic.disabled = false
		personalwiki.basic.pages = 10
		personalwiki.paypal.sandbox = true
		personalwiki.paypal.pdt.server = "https://www.sandbox.paypal.com/cgi-bin/webscr"
		personalwiki.paypal.pdt.path = ""
		personalwiki.paypal.pdt.id = "YOUR_PAYPAL_ID"
		personalwiki.paypal.account.url = "http://www.sandbox.paypal.com/us"
	}
	beta {
		personalwiki.app.domain = "http://beta.typelink.net"
		personalwiki.app.root = ""
		personalwiki.env.label = "Beta"
		personalwiki.rest.delay = 0
		personalwiki.analytics.disabled = false
		personalwiki.social.disabled = false
		personalwiki.unload.disabled = false
		personalwiki.basic.disabled = false
		personalwiki.basic.pages = 10
		personalwiki.paypal.sandbox = false
		personalwiki.paypal.pdt.server = "https://www.paypal.com/"
		personalwiki.paypal.pdt.path = "/cgi-bin/webscr"
		personalwiki.paypal.pdt.id = "YOUR_PAYPAL_ID"
		personalwiki.paypal.account.url = "http://www.paypal.com/us"
	}
	production {
		personalwiki.app.domain = "http://typelink.net"
		personalwiki.app.root = ""
		personalwiki.env.label = ""
		personalwiki.rest.delay = 0
		personalwiki.analytics.disabled = false
		personalwiki.social.disabled = false
		personalwiki.unload.disabled = false
		personalwiki.basic.disabled = false
		personalwiki.basic.pages = 10
		personalwiki.paypal.sandbox = false
		personalwiki.paypal.pdt.server = "https://www.paypal.com/"
		personalwiki.paypal.pdt.path = "/cgi-bin/webscr"
		personalwiki.paypal.pdt.id = "YOUR_PAYPAL_ID"
		personalwiki.paypal.account.url = "http://www.paypal.com/us"
	}
}

grails.mail.host = "smtp.example.com"
grails.mail.username = "YOUR_MAIL_USERNAME"
grails.mail.password = "YOUR_MAIL_PASSWORD"

personalwiki.bit.ly.login = "YOUR_BITLY_LOGIN"
personalwiki.bit.ly.key = "YOUR_BITLY_KEY"
