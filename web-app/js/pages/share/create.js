new function(){
	
	var g = YAHOO.util.Dom.get
	var evt = YAHOO.util.Event
	
	// cursor to end of content field, and scroll to bottom
	evt.onDOMReady( function() {
		var contentField = g("username") 
		contentField.focus()
		contentField.select()
	} )
    
}()
