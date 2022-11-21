
<%@ page import="personalwiki.UnsubscribeRequest" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'unsubscribeRequest.label', default: 'UnsubscribeRequest')}" />
        <title><g:message code="custom.title.unsubscribe.success" default="Unsubscribe Success - TypeLink" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="custom.unsubscribe.success.label" default="Unsubscribe" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <p><g:message code="custom.unsubscribe.success.message" default="You have been unsubscribed. You will not receive any more e-mails from TypeLink at this address, effective immediately." /></p>
        </div>
    </body>
</html>
