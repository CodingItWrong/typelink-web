
<%@ page import="personalwiki.Page" %>
<%@ page import="personalwiki.Share" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'share.label', default: 'Share')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
        	<g:pageTitle myPage="${shareInstance.page}"/>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:message code="custom.share.unshare.warning" default="Are you sure you want to unshare this page from this user?"/>
            <div class="dialog">
               	<div class="value">
                   <a href="${createLink(uri: '/'+shareInstance?.page?.user?.login)}" class="shared">${shareInstance?.user?.encodeAsHTML()}</a>
                </div>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${shareInstance?.id}" />
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'custom.button.unshare.label', default: 'Unshare')}" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
