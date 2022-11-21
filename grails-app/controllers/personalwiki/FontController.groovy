package personalwiki

import grails.converters.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class FontController {

    def restDelay = {
		def restDelay = ConfigurationHolder.config.personalwiki.rest.delay
		if( restDelay > 0 ) {
			Thread.sleep(restDelay*1000);
		}
    }
	
    def restGet = {
		restDelay()
		render Font.list(sort:"name") as JSON
	}
	
}
