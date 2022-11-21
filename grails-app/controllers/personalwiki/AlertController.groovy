package personalwiki

import grails.converters.*

class AlertController {

    def index = { }
	
	def restGet = {
		render Alert.findByActive(true).collect {
			it.text
		} as JSON
	}
}
