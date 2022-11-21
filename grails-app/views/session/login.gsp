
<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
        <title><g:message code="custom.title.login" default="TypeLink"/></title>
    </head>
    <body>
        <div class="nav">
        </div>
        <div class="body splash">
        	<div id="flash-message-container">
	            <g:if test="${flash.message}">
	            	<div class="message" id="flash-message">${flash.message}</div>
	            </g:if>
	        </div>
	        <div class="main-column">
	        	<div class="text">
	        		<h1><g:message code="custom.home.header1" default="Organize your notes with easy, intuitive links."/></h1>
	        		<h2><g:message code="custom.home.header2" default="Then access them from any computer or mobile device."/></h2>
	        	</div>
	        	<div class="clearfix">
		        	<div class="img"></div>
		        	<div class="splash-nav">
		        		<ul>
		        			<li><input type="button" value="${g.message(code:"custom.home.links.sign.up", default:"Sign up now!")}" id="signUpButton"/></li>
		        			<li><a href="${createLink(uri: '/about/TypeLink+Tour')}"><g:message code="custom.home.links.tour" default="Video tour"/></a></li>
		        			<li><input type="button" id="appStoreButton" value="${g.message(code:"custom.home.links.app.store", default:"Available on the App Store")}"/></li>
		        		</ul>
		        	</div>
		        </div>
		        <g:if test="${!grailsApplication.config.personalwiki.analytics.disabled}">
					<iframe src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.facebook.com%2Fpages%2FTypeLink%2F159144830791655&amp;layout=standard&amp;show_faces=false&amp;width=300&amp;action=like&amp;colorscheme=light&amp;height=35" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:300px; height:35px;" allowTransparency="true"></iframe>
					<a href="http://twitter.com/share" class="twitter-share-button" data-url="http://typelink.net" data-count="horizontal">Tweet</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
				</g:if>
	        </div>
	        <div class="right-column">
	        	<div class="login-box">
		            <h1><a name="login"></a><g:message code="custom.home.login.title" default="Log in"/></h1>
		            
		            <g:if test="${flash.authenticationFailure}">
		            	<div class="errors">
							Login failed: ${message(code:"authentication.failure."+flash.authenticationFailure.result).encodeAsHTML()}
						</div>
					</g:if>
					<g:authForm authAction="doLogin" success="[controller:'page', action:'index']" error="[controller:'session', action:'login']">
					    <label class="placeholder-fallback"><g:message code="custom.home.login.user" default="User ID"/></label>
					    <g:textField name="login" id="loginLogin" class="text" autocapitalize="off" autocorrect="off"/><br/>
						<g:hasErrors bean="${flash.loginFormErrors}" field="login">
							<div class="errors">
								<g:renderErrors bean="${flash.loginFormErrors}" as="list" field="login"/>
							</div>
						</g:hasErrors>
						
					    <label class="placeholder-fallback"><g:message code="custom.home.login.password" default="Password"/></label>
					    <input type="password" name="password" id="loginPassword" class="text" /><br/>
						<g:hasErrors bean="${flash.loginFormErrors}" field="password">
							<div class="errors">
								<g:renderErrors bean="${flash.loginFormErrors}" as="list" field="password"/>
							</div>
						</g:hasErrors>
					    <input type="submit" value="${message(code:"custom.home.login.submit", default:"Log In")}"/>
					    <input type="hidden" name="dest" value="${flash.dest}"/>
					</g:authForm>
					<form>
						<input type="hidden" name="login" id="forgotLogin"/>
						<g:actionSubmit action="forgotPassword" value="${message(code:'custom.home.login.forgot.password', default:'Forgot Password')}" id="forgotPasswordButton"/>
					</form>
	        	</div>
	        </div>
        </div>

        <g:javascript library="yui/selector-min" />
        <g:javascript library="pages/session/login" />
        <script language="javascript">

        var appRoot = "${grailsApplication.config.personalwiki.app.root}";
        
        </script>
    </body>
</html>
