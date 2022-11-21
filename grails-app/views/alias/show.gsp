
<%@ page import="personalwiki.Page" %>
<%@ page import="personalwiki.Alias" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'alias.label', default: 'Alias')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
        	<g:pageTitle myPage="${aliasInstance.page}"/>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:message code="custom.remove.alias.warning" default="Are you sure you want to remove this alias?"/>
            <div class="dialog">
               	<div class="value">${aliasInstance?.name?.encodeAsHTML()}</div>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${aliasInstance?.id}" />
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'custom.button.remove.alias.label', default: 'Remove')}" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
