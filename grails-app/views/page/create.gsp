

<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
        <title><g:message code="custom.title.create" args="[entityName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
        	<div id="flash-message-container">
	            <g:if test="${flash.message}">
		            <div class="message" id="flash-message">${flash.message}</div>
	            </g:if>
            </div>
            <g:hasErrors bean="${pageInstance}">
            <div class="errors">
                <g:renderErrors bean="${pageInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:if test="${null == pageInstance?.title}">
            	<p class="tip"><g:message code="custom.new.select.message" default="Tip: to create new pages easier, select some text on a page, then click New. A new page will be created with the selected text as its title." /></p>
            </g:if>
            <g:form action="save" >
                <div class="dialog">
                	<div class="value ${hasErrors(bean: pageInstance, field: 'title', 'errors')}">
                        <label for="title"><g:message code="page.title.label" default="Title" /></label>
                        <g:textField name="title" value="${pageInstance?.title}" class="create"/>
                	</div>
                	<div class="value clearfix ${hasErrors(bean: pageInstance, field: 'publiclyVisible', 'errors')}">
                        <label for="publiclyVisible" class="checkbox public"><g:message code="page.publiclyVisible.label" default="Publicly Visible" /></label>
                        <g:checkBox name="publiclyVisible" value="${pageInstance?.publiclyVisible}" />
                	</div>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" onclick="formSubmitting=true"/></span>
                </div>
            </g:form>
        </div>
        <g:javascript library="prototype" />
        <g:javascript library="prototype/effects" />
        <g:javascript library="scriptaculous" />
        <g:javascript library="iphone-style-checkboxes" />
        <g:javascript library="pages/page/create" />
    </body>
</html>
