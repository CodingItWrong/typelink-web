package personalwiki

import grails.converters.*

class UrlShortenerService {

    static transactional = false

    def shorten( String longUrl ) {
		def login = ConfigurationHolder.config.personalwiki.bit.ly.login
		def apiKey = ConfigurationHolder.config.personalwiki.bit.ly.key
    	def base = "http://api.bit.ly/v3/shorten?"
		def qs = []
		qs << "login=" + login.encodeAsURL()
		qs << "apiKey=" + apiKey.encodeAsURL()
		qs << "longUrl=" + longUrl.encodeAsURL()
		qs << "format=json"
		def url = new URL( base + qs.join('&') )
		
		if(connection.responseCode == 200) {
	        def result = connection.content as JSON
			return result.data.url
	    } else {
	        log.error("UrlShortenerService.shorten FAILED")
	        log.error(url)
	        log.error(connection.responseCode)
	        log.error(connection.responseMessage)
			
			return longUrl // if can't get short one
	    }
    }
}
