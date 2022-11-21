

<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
        <g:if test="${session.user && session.user.login.equals(pageInstance?.user?.login)}">
        	<g:set var="userName" value="" />
        </g:if>
        <g:else>
        	<g:set var="userName" value="${pageInstance?.user?.login} /" />
        </g:else>
        <g:set var="pageTitle" value="${pageInstance?.title}" />
        <title><g:message code="custom.title.edit" args="[userName,pageTitle]" /></title>
        <!-- page dynamic styles -->
        <style type="text/css">
        <!--
        	<g:if test="${null != pageInstance?.font}">
        		#page-content,
        		textarea#content {
        			font-family: ${pageInstance.font.cssCode}
        		}
        	</g:if>
        // -->
        </style>
        <!-- end page dynamic styles -->
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
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
            <g:form method="post" >
                <g:hiddenField name="id" value="${pageInstance?.id}" />
                <g:hiddenField name="version" value="${pageInstance?.version}" />
                <div class="dialog">
                	<div class="message" id="savedIndicator">Saved</div>
                	<div id="pageTitleContainer" class="value">
               			<g:pageTitle myPage="${pageInstance}"/>
                	</div>
                	<div  class="value ${hasErrors(bean: pageInstance, field: 'content', 'errors')}">
                        <g:textArea name="content" value="\n${pageInstance?.content}" />
                	</div>
                </div>
                <div class="buttons">
                    <%--
					<span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'custom.button.update.label', default: 'Save and Close')}" onclick="navSafe=true"/></span>
					--%>
                    <span class="button"><input type="button" class="save" id="ajax-save" value="${message(code: 'custom.button.update.in.place.label', default: 'Save')}" title="${message(code: 'custom.shortcut.save', default: 'Shortcut: Cmd-S or Ctrl-S')}"/></span>
                    <span class="button"><input type="button" class="cancel" id="ajax-close" value="${message(code: 'custom.button.cancel.label', default: 'Close')}" title="${message(code: 'custom.shortcut.close', default: 'Shortcut: Cmd-W or Ctrl-W')}"/></span>
                </div>
            </g:form>
        </div>
        <g:javascript library="prototype" />
        <g:javascript library="prototype/effects" />
        <g:javascript library="yui/connection_core-min" />
        <g:javascript library="yui/json-min" />
        <g:javascript library="yui/container-min" />
        <g:javascript library="shortcut" />
        <g:javascript library="pages/page/edit" />
        <script language="javascript">

        var pageInstance = <g:json object="${pageInstance}"/>
        var pageUser = <g:json object="${pageInstance.user}"/>
        var appRoot = "${grailsApplication.config.personalwiki.app.root}"
        var disableUnload = ${grailsApplication.config.personalwiki.unload.disabled ? 'true' : 'false'};
        
        </script>
    </body>
</html>
