

<%@ page import="personalwiki.Page" %>
<%@ page import="personalwiki.Share" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'share.label', default: 'Share')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1 class="shared"><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${shareInstance}">
            <div class="errors">
                <g:renderErrors bean="${shareInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                	<div class="value ${hasErrors(bean: shareInstance, field: 'page', 'errors')}">
			        	<g:pageTitle myPage="${shareInstance.page}"/>
                       	<input type="hidden" name="page.id" value="${shareInstance?.page?.id}"/>
                    </div>
                	<div class="value ${hasErrors(bean: shareInstance, field: 'user', 'errors')}">
                        <label for="user"><g:message code="share.user.label" default="User" /></label>
                        <%--<g:select name="user.id" from="${personalwiki.User.list()}" optionKey="id" value="${shareInstance?.user?.id}"  /> --%>
                        <g:textField name="username" value="${shareInstance?.user?.login}" autocapitalize="off" autocorrect="off"/>
                    </div>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'custom.button.share.label', default: 'Share')}" /></span>
                </div>
            </g:form>
        </div>
        <g:javascript library="pages/share/create" />
    </body>
</html>
