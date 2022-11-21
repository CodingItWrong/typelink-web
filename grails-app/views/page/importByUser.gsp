

<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
        <title><g:message code="custom.title.import" args="[entityName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="custom.header.import" args="[entityName]" /></h1>
        	<div id="flash-message-container">
	            <g:if test="${flash.message}">
		            <div class="message">${flash.message}</div>
	            </g:if>
            </div>
            <g:hasErrors bean="${pageInstance}">
            <div class="errors">
                <g:renderErrors bean="${pageInstance}" as="list" />
            </div>
            </g:hasErrors>
            <p class="file-input-not-supported">
             	<g:message code="custom.file.input.not.supported" default="File uploads are not supported on this device. Please use your desktop or laptop."/>
            </p>
            <p class="file-input-control">
            	<g:message code="custom.import.instructions" default="Upload a zip file containing .txt files. Each text file will be converted into a page whose title is the same as the file name."/>
            </p>
            <g:form action="importPost" method="post" enctype="multipart/form-data" class="file-input-control">
                <div class="dialog">
                	<div class="value ${hasErrors(bean: pageInstance, field: 'title', 'errors')}">
                        <label for="file" class="file"><g:message code="page.import.file.label" default="Zip File" /></label>
                        <input type="file" name="file" class="file"/>
                	</div>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'custom.button.import.label', default: 'Import')}"/></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
