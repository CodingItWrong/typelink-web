

<%@ page import="personalwiki.Page" %>
<%@ page import="personalwiki.Alias" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'alias.label', default: 'Alias')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1 class="shared"><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${aliasInstance}">
            <div class="errors">
                <g:renderErrors bean="${aliasInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                	<div class="value ${hasErrors(bean: aliasInstance, field: 'page', 'errors')}">
			        	<g:pageTitle myPage="${aliasInstance.page}"/>
                       	<input type="hidden" name="page.id" value="${aliasInstance?.page?.id}"/>
                    </div>
                	<div class="value ${hasErrors(bean: shareInstance, field: 'user', 'errors')}">
                        <label for="name"><g:message code="alias.name.label" default="Alias" /></label>
                        <g:textField name="name" value="${shareInstance?.name}"/>
                    </div>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'custom.button.add.alias.label', default: 'Add')}" /></span>
                </div>
            </g:form>
        </div>
        <g:javascript library="pages/alias/create" />
    </body>
</html>
