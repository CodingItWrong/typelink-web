//new function(){

	var cookieName = "typelink_hide_alerts"
	
	var dom = YAHOO.util.Dom
	var evt = YAHOO.util.Event
	var anim = YAHOO.util.Anim
	var g = YAHOO.util.Dom.get
	var q = YAHOO.util.Selector.query
	var myPanel
	
	function setCookie(c_name,value,expiredays)
	{
		var exdate=new Date();
		exdate.setDate(exdate.getDate()+expiredays);
		document.cookie=c_name+ "=" +escape(value)+
		((expiredays==null) ? "" : ";expires="+exdate.toUTCString());
	}
	
	function getCookie(c_name)
	{
		if (document.cookie.length>0)
		  {
		  c_start=document.cookie.indexOf(c_name + "=");
		  if (c_start!=-1)
		    {
		    c_start=c_start + c_name.length+1;
		    c_end=document.cookie.indexOf(";",c_start);
		    if (c_end==-1) c_end=document.cookie.length;
		    return unescape(document.cookie.substring(c_start,c_end));
		    }
		  }
		return "";
	}
	
	function getSel(){
		var w=window,d=document,gS='getSelection';
		return (''+(w[gS]?w[gS]():d[gS]?d[gS]():d.selection.createRange().text)).replace(/(^\s+|\s+$)/g,'');
	}
	
	function selectCopyLink() {
		var link = g("copyLink")
		link.focus()
		link.select()
	}
	
	function showSidebar() {
		g("sidebar-button").style.display = "none"
		g("sidebar").style.display = "block"
	}
	
	function hideSidebar() {
		g("sidebar").style.display = "none"
		g("sidebar-button").style.display = "block"
	}
	
	function youtubeUrlsToPlayers() {
		var links = document.getElementsByTagName('a')
		for( var i = 0; i < links.length; i++ ) {
			var match = links[i].href.match(/http:\/\/www\.youtube\.com\/watch\?v=([-\w]+)/)
			if( null != match ) {
				var id = match[1]
				var w, h
				if( iPhone() ) {
					w = 290
					h = 174
				} else {
					w = 640
					h = 385
				}
				var container = document.createElement("div")
				var iframe = document.createElement("iframe")
				iframe.setAttribute("type","text/html")
				iframe.setAttribute("width",w)
				iframe.setAttribute("height",h)
				iframe.setAttribute("src","http://www.youtube.com/embed/"+id)
				iframe.setAttribute("frameborder",0)
				container.appendChild(iframe)
				links[i].parentNode.insertBefore( container, links[i] )
			}
		}
	}
	
	function closeOKHandler() {
		myPanel.hide()
	}
	
	function closeHelpHandler() {
		myPanel.hide()
		window.location = appRoot + "/help-web/Creating+a+page"
	}
	
	function newHandler() {
		var selection = getSel()
		if( null != selection && "" != selection ) {
			window.location = "../page/create?title="+encodeURIComponent(selection)
		} else if( null != lastSel && "" != lastSel ) {
			window.location = "../page/create?title="+encodeURIComponent(lastSel)
		} else {
			window.location = "../page/create"
		}
			/*
		} else {
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
	
	function editHandler() {
		var btn = g("edit-btn") 
		if( btn ) {
			btn.click()
		}
	}
	
	function settingsHandler() {
		var btn = g("settings-btn") 
		if( btn ) {
			btn.click()
		}
	}
	
	lastSel = null
	function handleTouchStart() {
		// saves the selection before iOS deselects it to click link
		lastSel = getSel()
	}
	
	evt.onDOMReady(function() {
		// show alerts if not disabled
		if( "" == getCookie( cookieName ) ) {
			dom.setStyle( q(".alert"), "display", "block" )
		}
		
		// set new page selection name listener
		if( g("createLink") ) {
			var createLink = g("createLink").firstChild
	       	createLink.href="javascript:void(0)"
	   		evt.addListener( createLink, "click", newHandler )
		}
		
		// bind keyboard shortcuts
		shortcut.add("Meta+N", newHandler )
		shortcut.add("Ctrl+N", newHandler )
		shortcut.add("Enter", editHandler )
		shortcut.add("Meta+I", settingsHandler )
		shortcut.add("Ctrl+I", settingsHandler )
		
   		// set copy link listener
   		evt.addListener("copyLink","click", selectCopyLink )
   		
   		// set touchend listener to remember selection
   		//evt.addListener("page-content","touchstart", handleTouchEnd )
   		//evt.addListener("page-content","touchend", handleTouchEnd )
   		evt.addListener("createLink","touchstart", handleTouchStart )
   		
   		// set sidebar link listeners
   		evt.addListener("sidebar-expand-link","click", showSidebar )
   		evt.addListener("sidebar-contract-link","click", hideSidebar )
   		
		// set close link listener
		var alertCloseLinks = q(".alert a")
		evt.addListener( alertCloseLinks, "click", function() {
			// store cookie to prevent showing more alerts
			setCookie( cookieName, "true" )
			
			// hide alert
			var a = new anim( q(".alert"), { opacity: { to: 0 } }, 0.5 )
			a.onComplete.subscribe( function() { dom.setStyle( q(".alert"), "display", "none" ) } )
			a.animate()
		});
		
		// iOS monospace fix
		if( iOS() && ("function" == typeof iOSMonospaceFix) ) {
			iOSMonospaceFix(g("page-content"))
		}
	})
	
	youtubeUrlsToPlayers()
//}()
