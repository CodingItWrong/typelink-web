new function(){
	
	var evt = YAHOO.util.Event
	var q = YAHOO.util.Selector.query
	var g = YAHOO.util.Dom.get
	
	function selectLoginBox() {
		var input = g("loginLogin")
		input.focus()
	}
	
	function copyLogin() {
		g("forgotLogin").value = g("loginLogin").value
	}
	
	function goToSignup() {
		window.location = appRoot + "/session/signup"
	}
	
	function goToAppStore() {
		window.open( "http://itunes.apple.com/us/app/typelink/id412347169?mt=8" )
	}
	
	// cursor to end of content field, and scroll to bottom
	evt.onDOMReady( selectLoginBox )
	evt.addListener( "forgotPasswordButton", "click", copyLogin )
	evt.addListener( "signUpButton", "click", goToSignup )
	evt.addListener( "appStoreButton", "click", goToAppStore )
    
}()
