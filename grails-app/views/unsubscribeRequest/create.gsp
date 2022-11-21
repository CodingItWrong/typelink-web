

<%@ page import="personalwiki.UnsubscribeRequest" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'unsubscribeRequest.label', default: 'UnsubscribeRequest')}" />
        <title><g:message code="custom.title.unsubscribe" default="Unsubscribe - TypeLink" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="custom.unsubscribe.label" default="Unsubscribe" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${unsubscribeRequestInstance}">
            <div class="errors">
                <g:renderErrors bean="${unsubscribeRequestInstance}" as="list" />
            </div>
            </g:hasErrors>
            <p><g:message code="custom.unsubscribe.message" default="Please enter the e-mail address to unsubscribe from TypeLink e-mails." /></p>
            <g:form action="save" >
                <div class="dialog left-fields">
                	<div class="value ${hasErrors(bean: unsubscribeRequestInstance, field: 'email', 'errors')}">
                        <label for="email" class="field"><g:message code="unsubscribeRequest.email.label" default="Email" /></label>
                        <g:emailField name="email" value="${unsubscribeRequestInstance?.email}" />
                	</div>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'custom.button.unsubscribe.label', default: 'Unsubscribe')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
