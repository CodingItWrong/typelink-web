var contentChanged = false
var navSafe = false
var saving = false

//new function(){
	
	var g = YAHOO.util.Dom.get
	var dom = YAHOO.util.Dom
	var evt = YAHOO.util.Event
	var anim = YAHOO.util.Anim
	var json = YAHOO.lang.JSON
	var myPanel
	
	function getSel() {
		var textarea = g("content")
		setIESelection(textarea)
		return textarea.value.substring( textarea.selectionStart, textarea.selectionEnd )
	}
	
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
	
	function resizeHandler() {
		scrollTo(0,0)
		var y = window.innerHeight ? window.innerHeight :
			document.documentElement.clientHeight
		// for better window height, see
		// <URL: http://jibbering.com/faq/#FAQ4_9 >
		var height = Math.max(100,y-260) + "px"
		g("content").style.height = height
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
		var go = true;
		checkForCloseWhileSaving()
		if(!saving) {
			if(!contentChanged) {
				navigateToViewScreen()
			} else {
				var keyListeners = new Array()
				
				myPanel = new YAHOO.widget.Panel("myPanel",
				{
					close: false,
					draggable: false,
					modal: true,
					fixedcenter: true
				})
				myPanel.setHeader("Unsaved Changes")
				myPanel.setBody("You have unsaved changes. Do you want to save them?")
				
				var buttons = document.createElement("div")
				buttons.className = "panel-buttons"
				var button
				
				button = document.createElement("button")
				button.appendChild(document.createTextNode("Save"))
				evt.addListener(button,"click",closeSaveHandler)
				//keyListeners.push( new YAHOO.util.KeyListener( document, {keys:13/*enter*/}, {fn:closeSaveHandler} ) )
				buttons.appendChild(button)
				buttons.appendChild(document.createTextNode("\u00a0"))
				var saveButton = button
				
				button = document.createElement("button")
				button.appendChild(document.createTextNode("Discard"))
				evt.addListener(button,"click",closeDiscardHandler)
				buttons.appendChild(button)
				buttons.appendChild(document.createTextNode("\u00a0"))
				
				button = document.createElement("button")
				button.appendChild(document.createTextNode("Cancel"))
				evt.addListener(button,"click",closeCancelHandler)
				keyListeners.push( new YAHOO.util.KeyListener( document, {keys:27/*escape*/}, {fn:closeCancelHandler} ) )
				buttons.appendChild(button)
				buttons.appendChild(document.createTextNode("\u00a0"))
				
				myPanel.setFooter(buttons)
				myPanel.cfg.queueProperty("keylisteners",keyListeners)
				myPanel.render(document.body)
				
				saveButton.focus()
			}
		}
	}
	
	function closeSaveHandler() {
		myPanel.hide()
		ajaxSave( function() { navigateToViewScreen() } )
	}
	
	function closeDiscardHandler() {
		myPanel.hide()
		navigateToViewScreen()
	}
	
	function closeCancelHandler() {
		myPanel.hide()
	}
	
	function navigateToViewScreen() {
		navSafe = true; // do not re-prompt
		window.location = appRoot + "/" + pageUser.login + "/" + myEncodeURI(pageInstance.title)
	}
	
	function insertAtCursor(myField, myValue) {
		  if (document.selection) {
		    myField.focus();
		    sel = document.selection.createRange();
		    sel.text = myValue;
		  }
		  else if (myField.selectionStart || myField.selectionStart == '0') {
		    var startPos = myField.selectionStart;
		    var endPos = myField.selectionEnd;
		    //alert( startPos )
		    myField.value = myField.value.substring(0, startPos)
		                  + myValue
		                  + myField.value.substring(endPos, myField.value.length);
		    // set new cursor point
		    //alert( startPos + myValue.length )
		    var pos = startPos + myValue.length
		    myField.setSelectionRange( pos, pos )
		    setTimeout( function() { myField.focus() }, 10 ) // needs delay after setSelectionRange
		  } else {
		    // for my purposes, don't do anything if can't do those
		  }
	}
	
	function checkTab(e) {
		var tabKeyCode = 9
		if( e.keyCode == tabKeyCode ) {
			insertAtCursor( g("content"), String.fromCharCode(tabKeyCode) )
	        // do not go to the next field/link
	        e.returnValue = false
		}
	}
	
	function updateSavedIndicator( overwriteSaving ) {
		// do not change if currently saving
		if( !overwriteSaving
				&& "Saving..." == getText("savedIndicator") )
		{
			return;
		}
		
		// changed to either saved or not saved
		if( g("content").value == pageInstance.content )
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
		// set up url from page title
		var oldTitle = pageInstance.title
		var url = appRoot + "/rest/" + pageUser.login + "/" + myEncodeURI(oldTitle)
		
		// update request object and local data object
		var p = {}
		p.content = g("content").value
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
				alert("Could not save: " + o.responseText)
				setText( "savedIndicator", "Not saved" )
			},
			success:function(o){
				saving = false
				// store new version in field and page instance variable
				var pageReturned = eval('('+o.responseText+')')
				g("version").value = pageInstance.version = pageReturned.version
				
				// store fields in page instance variable
				pageInstance.content = pageReturned.content
				
				// update saved indicator
				updateSavedIndicator(true)
				
				// callback
				callback()
			}
		}, jsonString )
	}

	function newHandler() {
		var selection = getSel()
		if( null != selection && "" != selection ) {
			window.location = "../page/create?title="+encodeURIComponent(selection)
		} else {
			window.location = "../page/create"
		}
			/*
			myPanel = new YAHOO.widget.Panel("myPanel",
			{
				close: false,
				draggable: false,
				modal: true,
				fixedcenter: true,
				width: "300px"
			})
			myPanel.setHeader("New Page")
			myPanel.setBody("To create a new page, type its name on this page, then select it and click \"New\". For more info, click \"Help me\".")
			
			var buttons = document.createElement("div")
			buttons.className = "panel-buttons"
			var button
			
			button = document.createElement("button")
			button.appendChild(document.createTextNode("OK"))
			evt.addListener(button,"click",closeOKHandler)
			buttons.appendChild(button)
			buttons.appendChild(document.createTextNode("\u00a0"))
			var saveButton = button
			
			button = document.createElement("button")
			button.appendChild(document.createTextNode("Help Me"))
			evt.addListener(button,"click",closeHelpHandler)
			buttons.appendChild(button)
			buttons.appendChild(document.createTextNode("\u00a0"))
			
			myPanel.setFooter(buttons)
			myPanel.render(document.body)
			
			saveButton.focus()
		}
		*/
	}
	
	function closeOKHandler() {
		myPanel.hide()
	}
	
	function closeHelpHandler() {
		myPanel.hide()
		window.location = appRoot + "/help-web/Creating+a+page"
	}
	
	// cursor to end of content field, and scroll to bottom
	evt.onDOMReady( function() { selectEnd( g("content") ) } )
    
	// bind resize event
	if( !iPhone() && !android() ) {
		resizeHandler()
		window.onresize = resizeHandler
	}
	
	// bind keypress events
	evt.addListener("content","keydown", checkTab )
	evt.addListener("content","keyup", function() { updateSavedIndicator(false) } )
	
	// bind buttons
	evt.addListener("ajax-save","click", saveHandler)
	evt.addListener("ajax-close","click", closeHandler)
	
	// bind keyboard shortcuts
	shortcut.add("Meta+S", saveHandler )
	shortcut.add("Ctrl+S", saveHandler )
	shortcut.add("Meta+W", closeHandler )
	shortcut.add("Ctrl+W", closeHandler )
	shortcut.add("Meta+N", newHandler )
	shortcut.add("Ctrl+N", newHandler )
	
	// bind test selection method
	var createLink = g("createLink").firstChild
	createLink.href="javascript:void(0)"
	evt.addListener( createLink, "click", newHandler )
	
    // prevent accidental navigation
    window.onbeforeunload = function() { if( contentChanged && !navSafe ) { return "Your changes will not be saved." } }
    
//}()
