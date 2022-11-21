

<%@ page import="personalwiki.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="custom.account.title" default="Account - TypeLink" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="custom.user.title" /></h1>
        	<div id="flash-message-container">
	            <g:if test="${flash.message}">
		            <div class="message" id="flash-message">${flash.message}</div>
	            </g:if>
            </div>
            <g:hasErrors bean="${userInstance}">
            <div class="errors">
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="version" value="${userInstance?.version}" />
                <div class="dialog left-fields account">
                        
                	<div class="value clearfix">
                        <label for="login" class="field"><g:message code="user.login.label" default="Login" /></label>
                        <span class="value">${userInstance?.login}</span>
                	</div>
                        
                    <g:if test="${!grailsApplication.config.personalwiki.basic.disabled}">
	                	<div class="value clearfix">
	                        <label for="login" class="field"><g:message code="custom.user.account.type.label" default="Account Type" /></label>
	                        <span class="value">
		                        <g:message code="custom.user.account.type.${accountType}"/>
		                        <g:if test="${'basic'==accountType || 'pro'==accountType}">
		                        	<%--<a href="${createLink(uri: '/account/subscribe')}"><g:message code="custom.user.upgrade.${accountType}"/></a>--%>
		                        	<input type="button" id="subscribeBtn" value="<g:message code="custom.user.upgrade.${accountType}"/>"/>
		                        </g:if>
		                        <g:link controller="subscription" action="list" class="history"><g:message code="custom.subscription.list.link" default="History"/></g:link>
	                        </span>
	                	</div>
                	</g:if>
                        
                	<div class="value ${hasErrors(bean: userInstance, field: 'email', 'errors')}">
                        <label for="email" class="field"><g:message code="user.email.label" default="Email" /></label>
                        <g:emailField name="email" value="${userInstance?.email}" />
                	</div>
                        
                	<div class="value ${hasErrors(bean: userInstance, field: 'password', 'errors')}">
                        <label for="password" class="field"><g:message code="custom.user.change.password.label" default="Change Password" /></label>
                        <g:passwordField name="password" value="" />
                	</div>
                        
                	<div class="value">
                        <label for="confirmPassword" class="field"><g:message code="custom.user.confirm.password.label" default="Confirm Password" /></label>
                        <g:passwordField name="confirmPassword" value="" />
                	</div>
                        
                	<div class="value ${hasErrors(bean: userInstance, field: 'defaultFont', 'errors')}">
                        <label for="defaultFont" class="field"><g:message code="user.defaultFont.label" default="Default Font" /></label>
                        <g:select name="defaultFont.id" from="${personalwiki.Font.list(sort:'name')}" optionKey="id" value="${userInstance?.defaultFont?.id}"  />
                	</div>
                	
                	<div class="value ${hasErrors(bean: userInstance, field: 'sendEmails', 'errors')}">
                        <label for="sendEmails" class="field"><g:message code="user.sendEmails.label" default="Send Emails" /></label>
                        <g:checkBox name="sendEmails" class="checkbox" value="${userInstance?.sendEmails}" />
                    </div>
                        
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                </div>
            </g:form>
        </div>
        <g:javascript library="prototype" />
        <g:javascript library="prototype/effects" />
        <g:javascript library="scriptaculous" />
        <g:javascript library="iphone-style-checkboxes" />
        <g:javascript library="pages/user/edit" />
        <script language="javascript">

        var appRoot = "${grailsApplication.config.personalwiki.app.root}";
        
        </script>
    </body>
</html>
