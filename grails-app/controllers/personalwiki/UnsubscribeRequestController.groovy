package personalwiki

class UnsubscribeRequestController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def create = {
        def unsubscribeRequestInstance = new UnsubscribeRequest()
        unsubscribeRequestInstance.properties = params
        return [unsubscribeRequestInstance: unsubscribeRequestInstance]
    }

    def save = {
        def unsubscribeRequestInstance = new UnsubscribeRequest(params)
		unsubscribeRequestInstance.requestDate = new Date()
        if (unsubscribeRequestInstance.save(flush: true)) {
            redirect(action: "success")
        }
        else {
            render(view: "create", model: [unsubscribeRequestInstance: unsubscribeRequestInstance])
        }
    }
	
	def success = { }
}
