new function(){
	
	var g = YAHOO.util.Dom.get
	var event = YAHOO.util.Event
	var anim = YAHOO.util.Anim
	
	event.onDOMReady( function() {
		setTimeout( function()
		{
			if( iPhone() && 0 == scrollY ) { window.scrollTo(0, 1) }
		}, 100)
    } )
	if( g("flash-message") ) {
		setTimeout( function() {
			var a = new anim("flash-message", { opacity: { to: 0 } }, 0.5 )
			a.onComplete.subscribe( function() { g("flash-message").style.display = "none" } )
			a.animate()
		}, 1500 )
	}
}()
