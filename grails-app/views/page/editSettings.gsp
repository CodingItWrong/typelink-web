

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
                	<div id="pageTitleContainer" class="value ${hasErrors(bean: pageInstance, field: 'title', 'errors')}">
                		<g:if test='${Page.homePageTitle.equals(originalTitle)}'>
                			<g:pageTitle myPage="${pageInstance}"/>
                		</g:if>
                		<g:else>
            		        <g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
                				<g:textField name="title" value="${pageInstance?.title}" />
                			</g:if>
                			<g:else>
                				<g:pageTitle myPage="${pageInstance}"/>
                			</g:else>
                		</g:else>
                	</div>
                	<div class="message" id="savedIndicator">Saved</div>
                	<div class="value ${hasErrors(bean: userInstance, field: 'font', 'errors')} clearfix">
                        <label for="font" class="select"><g:message code="custom.page.font.label" default="Font" /></label>
                        <g:select name="font.id" class="fontId" from="${personalwiki.Font.list(sort:'name')}" optionKey="id" value="${pageInstance?.font?.id}"
                        	noSelection="['':'(Use default)']"/>
                	</div>
                	<div class="clearfix value ${hasErrors(bean: pageInstance, field: 'publiclyVisible', 'errors')}">
                        <label for="publiclyVisible" class="checkbox public"><g:message code="page.publiclyVisible.label" default="Publicly Visible" /></label>
                		<g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
                        	<g:checkBox name="publiclyVisible" class="checkbox" value="${pageInstance?.publiclyVisible}" />
                        </g:if>
                        <g:else>
                        	<span class="public-checkbox-value">
	                        	<g:if test="${pageInstance?.publiclyVisible}">
	                        		<g:message code="custom.publicly.visible.yes" default="Yes" />
	                        	</g:if>
	                        	<g:else>
	                        		<g:message code="custom.publicly.visible.no" default="No" />
	                        	</g:else>
                        	</span>
                        </g:else>
                	</div>
                	<div class="value ${hasErrors(bean: pageInstance, field: 'sharedTo', 'errors')}">
                        <label for="sharedTo" class="sharedTo">
                        	<g:message code="page.sharedTo.label" default="Shared To" />
                        </label>
						<g:each in="${pageInstance?.sharedTo?}" var="s">
		                	<g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
							    <g:link controller="share" action="show" id="${s.id}" class="shared">${s?.encodeAsHTML()}</g:link>
						    </g:if>
						    <g:else>
							    ${s?.encodeAsHTML()}
						    </g:else>
						</g:each>
						<g:if test="${0==pageInstance?.sharedTo?.size()}">
							<g:message code="custom.shared.to.none"/>
						</g:if>
	                	<g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
							<g:link class="add shared" controller="share" action="create" params="['page.id': pageInstance?.id]">${message(code: 'custom.add.label')}</g:link>
						</g:if>
					</div>
                	<div class="value ${hasErrors(bean: pageInstance, field: 'aliases', 'errors')}">
                        <label for="aliases" class="aliases">
                        	<g:message code="page.aliases.label" default="Aliases" />
                        </label>
						<g:each in="${pageInstance?.aliases?}" var="a">
		                	<g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
							    <g:link controller="alias" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link>
						    </g:if>
						    <g:else>
							    ${a?.encodeAsHTML()}
						    </g:else>
						</g:each>
						<g:if test="${0==pageInstance?.aliases?.size()}">
							<g:message code="custom.aliases.none"/>
						</g:if>
	                	<g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
							<g:link class="add" controller="alias" action="create" params="['page.id': pageInstance?.id]">${message(code: 'custom.add.label')}</g:link>
						</g:if>
					</div>
                </div>
                <div class="buttons">
                    <span class="button"><input type="button" class="save" id="ajax-save" value="${message(code: 'custom.button.update.in.place.label', default: 'Save')}" title="${message(code: 'custom.shortcut.save', default: 'Shortcut: Cmd-S or Ctrl-S')}"/></span>
                    <span class="button"><input type="button" class="cancel" id="ajax-close" value="${message(code: 'custom.button.cancel.label', default: 'Close')}" title="${message(code: 'custom.shortcut.close', default: 'Shortcut: Cmd-W or Ctrl-W')}"/></span>
                </div>
            </g:form>
        </div>
        <g:javascript library="prototype" />
        <g:javascript library="prototype/effects" />
        <g:javascript library="scriptaculous" />
        <g:javascript library="iphone-style-checkboxes" />
        <g:javascript library="yui/connection_core-min" />
        <g:javascript library="yui/selector-min" />
        <g:javascript library="yui/json-min" />
        <g:javascript library="shortcut" />
        <g:javascript library="pages/page/editSettings" />
        <script language="javascript">

        var pageInstance = <g:json object="${pageInstance}"/>
        var pageUser = <g:json object="${pageInstance.user}"/>
        //var user = <g:json object="${user}"/>
        var appRoot = "${appRoot}";
        
        </script>
    </body>
</html>
