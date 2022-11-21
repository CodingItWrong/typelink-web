
<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
        <title><g:message code="custom.title.sign.up" default="Sign Up - TypeLink"/></title>
    </head>
    <body>
        <div class="nav">
        </div>
        <div class="body">
			<div class="signup left-fields">
	            <h1><a name="signup"><g:message code="custom.home.sign.up.title" default="Sign up"/></a></h1>
	            
				<g:authForm authAction="doSignup" success="[controller:'page', action:'index']" error="[controller:'session', action:'signup']">
		            <g:if test="${flash.authenticationFailure}">
		            	<div class="errors">
							Login failed: ${message(code:"authentication.failure."+flash.authenticationFailure.result).encodeAsHTML()}
						</div>
					</g:if>
					<g:hasErrors bean="${flash.signupFormErrors}" field="login">
						<div class="errors">
							<g:renderErrors bean="${flash.signupFormErrors}" as="list" field="login"/>
						</div>
					</g:hasErrors>
					
				    <label><g:message code="custom.home.sign.up.user" default="User ID"/></label>
				    <g:textField name="login" value="${flash?.signupForm?.login}" id="signupLogin" autocapitalize="off" autocorrect="off" autocomplete="off"/><br/>
				    
				    <label><g:message code="custom.home.sign.up.email" default="E-mail"/></label>
				    <g:emailField name="email" value="${flash?.signupForm?.email}" id="signupEmail" autocapitalize="off" autocorrect="off" autocomplete="off"/><br/>
					<g:hasErrors bean="${flash.signupFormErrors}" field="email">
						<div class="errors">
							<g:renderErrors bean="${flash.signupFormErrors}" as="list" field="email"/>
						</div>
					</g:hasErrors>
					
				    <label><g:message code="custom.home.sign.up.password" default="Password"/></label>
				    <input type="password" name="password" id="signupPassword" autocomplete="off" maxlength="32"/><br/>
					<g:hasErrors bean="${flash.signupFormErrors}" field="password">
						<div class="errors">
							<g:renderErrors bean="${flash.signupFormErrors}" as="list" field="password"/>
						</div>
					</g:hasErrors>
					
				    <label><g:message code="custom.home.sign.up.confirm.password" default="Confirm Password"/></label>
				    <input type="password" name="passwordConfirm" id="signupConfirmPassword" autocomplete="off" maxlength="32"/><br/>
					<g:hasErrors bean="${flash.signupFormErrors}" field="passwordConfirm">
						<div class="errors">
							<g:renderErrors bean="${flash.signupFormErrors}" as="list" field="passwordConfirm"/>
						</div>
					</g:hasErrors>
					
				    <input type="submit" value="<g:message code="custom.home.sign.up.submit" default="Create account"/>"/>
				</g:authForm>
			</div>
        </div>
        <g:javascript library="yui/selector-min" />
        <g:javascript library="pages/session/login" />
    </body>
</html>
