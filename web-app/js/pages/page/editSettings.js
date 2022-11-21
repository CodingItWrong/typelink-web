var contentChanged = false
var navSafe = false
var saving = false

//new function(){
	
	var g = YAHOO.util.Dom.get
	var q = YAHOO.util.Selector.query
	var dom = YAHOO.util.Dom
	var evt = YAHOO.util.Event
	var anim = YAHOO.util.Anim
	var json = YAHOO.lang.JSON
	
	function myEncodeURI( string ) {
		var result = encodeURIComponent(string)
		return result.replace(/%20/g,"+")
	}
	
	function getText( node ) {
		if( (typeof node) == "string" ) {
			node = g(node)
		}
		return node.firstChild.data
	}
	
	function setText( node, text ) {
		if( (typeof node) == "string" ) {
			node = g(node)
		}
		while( node.firstChild ) { node.removeChild( node.firstChild ) }
		node.appendChild( document.createTextNode(text) )
	}
	
	function selectEnd( myTextArea ) {
		myTextArea.focus()
    	setCaretToEnd(myTextArea)
    	myTextArea.scrollTop = myTextArea.scrollHeight 
	}
	
	function setCaretToEnd(ctrl) {
		if(ctrl.setSelectionRange)	{
			ctrl.setSelectionRange(ctrl.value.length, ctrl.value.length)
		}
		else if (ctrl.createTextRange) {
			var range = ctrl.createTextRange()
			range.moveStart('character', ctrl.value.length)
			range.select()
		}
	}
	
	function setIESelection( element ) {
		if( document.selection ){
			// The current selection
			var range = document.selection.createRange();
			// We'll use this as a 'dummy'
			var stored_range = range.duplicate();
			// Select all text
			stored_range.moveToElementText( element );
			// Now move 'dummy' end point to end point of original range
			stored_range.setEndPoint( 'EndToEnd', range );
			// Now we can calculate start and end points
			element.selectionStart = stored_range.text.length - range.text.length;
			element.selectionEnd = element.selectionStart + range.text.length;
		}
	}
	
	function basicAuth(user, password)
	{
	    var tok = user + ':' + password
	    var hash = Base64.encode(tok)
	    return "Basic " + hash
	}
	
	function saveHandler() {
		ajaxSave( function() {
			// clear message container
			var root = g("flash-message-container")
			while( root.firstChild ) { root.removeChild( root.firstChild ) }
		})
	}
	
	function checkForCloseWhileSaving() {
		if(saving) {
			alert("Your page is still saving. Please wait for it to finish before closing.")
		}
	}
	
	function closeHandler() {
		checkForCloseWhileSaving()
		if(!saving) {
			if(!contentChanged
					|| confirm('You have unsaved changes. Close anyway?'))
			{
				navSafe = true; // do not re-prompt
				window.location = appRoot + "/" + pageUser.login + "/" + myEncodeURI(pageInstance.title)
			}
		}
	}
	
	function updateSavedIndicator( overwriteSaving ) {
		// do not change if currently saving
		if( !overwriteSaving
				&& "Saving..." == getText("savedIndicator") )
		{
			return;
		}
		
		// decide whether saved
		var saved = false;
		if( (null == g("title") || g("title").value == pageInstance.title )
				&& g("publiclyVisible").checked == pageInstance.publiclyVisible
				&& ( g("font.id").options[g("font.id").selectedIndex].value == ( pageInstance.font ? pageInstance.font.id : "" )) )
		{
			saved = true;
		}
		
		// changed indicator to either saved or not saved
		if( saved )
		{
			contentChanged = false
			setText( "savedIndicator", "Saved" )
			dom.replaceClass( "savedIndicator", "errors", "message" )
		} else {
			contentChanged = true
			setText( "savedIndicator", "Not saved" )
			dom.replaceClass( "savedIndicator", "message", "errors" )
		}
	}
	
	function ajaxSave( callback ) {
		// set up url from old page title
		var oldTitle = pageInstance.title
		var url = appRoot + "/rest/" + pageUser.login + "/" + myEncodeURI(oldTitle)
		
		// update request object and local data object
		var p = {}
		if( g("title") ) {
			p.title = g("title").value
		}
		if( g("publiclyVisible") ) {
			p.publiclyVisible = g("publiclyVisible").checked
		}
		p.font = { id: g("font.id").options[g("font.id").selectedIndex].value }
		p.version = g("version").value
		
		var jsonString = json.stringify(p)
		
		// no longer needed - checks java session auth
		//YAHOO.util.Connect.initHeader( "Authorization", basicAuth(user.login, user.password), true )
		setText( "savedIndicator", "Saving..." )
		saving = true
		YAHOO.util.Connect.initHeader("TypeLink-App","web")
		YAHOO.util.Connect.asyncRequest("PUT", url,
		{
			failure:function(o){
				saving = false
				var root = g("flash-message-container")
				while( root.firstChild ) { root.removeChild( root.firstChild ) }
				
				var msg = document.createElement("div")
				msg.id = "flash-message"
				msg.className = "message"
				msg.appendChild( document.createTextNode( "Could not save: " + o.responseText ) )
				root.appendChild(msg)

				setTimeout( function() {
					var a = new anim("flash-message", { opacity: { to: 0 } }, 0.5 )
					a.onComplete.subscribe( function() { g("flash-message").style.display = "none" } )
					a.animate()
				}, 1500 )
				
				setText( "savedIndicator", "Not saved" )
			},
			success:function(o){
				saving = false
				// store new version in field and page instance variable
				var pageReturned = eval('('+o.responseText+')')
				g("version").value = pageInstance.version = pageReturned.version
				
				// store fields in page instance variable
				if( g("title") ) {
					pageInstance.title = pageReturned.title
				}
				if( g("publiclyVisible") ) {
					pageInstance.publiclyVisible = pageReturned.publiclyVisible
				}
				if( g("font.id") ) {
					pageInstance.font = pageReturned.font
				}
				
				// update saved indicator
				updateSavedIndicator(true)
				
				// callback
				callback()
			}
		}, jsonString )
	}

    // switch to iOS checkboxes
	if( iOS() ) {
		evt.onDOMReady( function() { new iPhoneStyle('input[type=checkbox]') } ) 
	}
	
	// bind fields to change tracking
	evt.addListener("title","keyup", function() { updateSavedIndicator(false) } )
	evt.addListener("font.id","change",function() { updateSavedIndicator(false) } )
	evt.addListener("publiclyVisible","click", function() { updateSavedIndicator(false) } )
	iPhoneStyle.defaults.statusChange = function() { updateSavedIndicator(false) }
	
	// bind buttons
	evt.addListener("ajax-save","click", saveHandler)
	evt.addListener("ajax-close","click", closeHandler)
	
	// bind keyboard shortcuts
	shortcut.add("Meta+S", saveHandler )
	shortcut.add("Ctrl+S", saveHandler )
	shortcut.add("Meta+W", closeHandler )
	shortcut.add("Ctrl+W", closeHandler )
	
	// bind test selection method
	var createLink = g("createLink").firstChild
	createLink.href="javascript:void(0)"
	
    // prevent accidental navigation
    window.onbeforeunload = function() { if( contentChanged && !navSafe ) { return "Your changes will not be saved." } }
    
//}()
