<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <g:javascript library="user-agent" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css/yui',file:'container.css')}" />
        <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'iphone-style-checkboxes.css')}" />
        <script language="javascript">
        <!--
			if( iPad() ) {
				document.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"${resource(dir:'css',file:'iOS.css')}\" />");
			}
        // -->
        </script>
        <link rel="stylesheet" type="text/css" media="only screen and (max-device-width: 480px)" href="${resource(dir:'css',file:'iOS.css')}" />
        <link rel="stylesheet" type="text/css" media="only screen and (max-device-width: 480px)" href="${resource(dir:'css',file:'iPhone.css')}" />
        <link rel="stylesheet" type="text/css" media="only screen and (-webkit-min-device-pixel-ratio: 1.5)" href="${resource(dir:'css',file:'nexus.css')}" />
        <link rel="stylesheet" type="text/css" media="only screen and (-webkit-min-device-pixel-ratio: 2)" href="${resource(dir:'css',file:'retina.css')}" />
        <!-- site-wide dynamic styles -->
        <style type="text/css">
        <!--
        	<g:if test="${null != session.user?.defaultFont}">
        		#page-content,
        		textarea#content {
        			font-family: ${session.user.defaultFont.cssCode}
        		}
        	</g:if>
        // -->
        </style>
        <!-- end site-wide dynamic styles -->
        
        <meta name="viewport" content="width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=false;" />
        <link rel="apple-touch-icon" href="${resource(dir:'images',file:'TypeLinkIcon114.png')}"/>
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="yui/yahoo-dom-event" />
        <g:javascript library="yui/animation-min" />
    </head>
    <body class="yui-skin-sam">
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div id="logo"><a href="${createLink(uri: '/')}">Type<span>Link</span> ${grailsApplication.config.personalwiki.env.label}</a></div>
        <div id="login-state">
	        <auth:ifLoggedIn>
	        	<span><g:message code="custom.header.logged.in" default="Logged in as" /></span> ${session.user.login}
	        </auth:ifLoggedIn>
	        <auth:ifNotLoggedIn>
	        	<g:message code="custom.header.logged.out" default="Free personal wiki!" />
	        </auth:ifNotLoggedIn>
        </div>
        <auth:ifLoggedIn>
        	<div id="site-nav">
			   	<form action="${createLink(uri: '/account')}" method="POST"><g:actionSubmit action="edit" value="Account"/></form>
	        	|
	        	<a href="${createLink(uri: '/help/Home')}"><g:message code="custom.header.button.help" default="Help" /></a>
	        	|
	        	<auth:logoutLink success="[controller:'session', action:'login']" error="[controller:'session', action:'error']"><g:message code="custom.header.button.logout" default="Log out" /></auth:logoutLink>
	        </div>
        </auth:ifLoggedIn>
        <auth:ifNotLoggedIn>
        	<div id="site-nav">
	        	<a href="${createLink(uri: '/about/Home')}"><g:message code="custom.header.button.about" default="About" /></a>
	        	|
	        	<g:link controller="session" action="signup"><g:message code="custom.header.button.sign.up" default="Sign up" /></g:link>
	        	|
	        	<g:link controller="session" action="login" fragment="login"><g:message code="custom.header.button.login" default="Log in" /></g:link>
	        </div>
        </auth:ifNotLoggedIn>
        <g:layoutBody />
        <g:javascript library="global" />
        
        <div class="copyright">
        	<g:message code="custom.copyright.content" default="User page content is property of its original creator. All rights reserved." /><br/>
        	Site &copy; 2010-${Calendar.getInstance().get(Calendar.YEAR).toString()}
        	<a href="${createLink(uri: '/needbee/me')}"><g:message code="custom.copyright.name" default="Josh Justice" /></a>
        	| <g:message code="custom.copyright.icons" default="iOS icons by Joseph Wain" /> / <a href="http://glyphish.com">glyphish.com</a>
        </div>
        
        <g:if test="${!grailsApplication.config.personalwiki.analytics.disabled}">
	        <!-- begin google analytics -->
	        <script type="text/javascript">
			
			  var _gaq = _gaq || [];
			  _gaq.push(['_setAccount', 'UA-19397593-1']);
			  _gaq.push(['_trackPageview']);
			
			  (function() {
			    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
			    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
			    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			  })();
			
			</script>
			<!-- end google analytics -->
		</g:if>
      </body>
</html>
