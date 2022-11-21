//new function(){
	
	var evt = YAHOO.util.Event
	
    // switch to iOS checkboxes
	if( iOS() ) {
		evt.onDOMReady( function() { new iPhoneStyle('input[type=checkbox]') } ) 
	}
	
	evt.addListener( "subscribeBtn", "click", function() { window.location = appRoot + "/account/subscribe" })
    
//}()
