
<%@ page import="personalwiki.Page" %>
<%@ page import="personalwiki.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:if test="${session.user && session.user.equals(pageInstance?.user)}">
        	<g:set var="userName" value="" />
        </g:if>
        <g:else>
        	<g:set var="userName" value="${pageInstance?.user?.login} /" />
        </g:else>
        <g:set var="pageTitle" value="${pageInstance?.title}" />
        <title><g:message code="custom.title.show" args="[userName,pageTitle]" /></title>
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
        <div class="body show-page">
            <g:each in="${alertList}" status="i" var="alert">
            	<div class="alert clearfix">
            		${alert.text}
            		<a href="javascript:void(0)">[<g:message code="custom.alert.close" />]</a>
            	</div>
            </g:each>
            
            <div>
        		<g:pageTitle myPage="${pageInstance}">
			        <g:if test="${pageInstance.publiclyVisible}">
			            <div id="sidebar-button">
			            	<a href="javascript:void(0)" id="sidebar-expand-link">&lt;</a>
			            </div>
				        <div id="sidebar">
				        	<h2>
				        		<a href="javascript:void(0)" id="sidebar-contract-link">
					        		&gt;
					        		<g:message code="custom.page.sidebar.sharing"/>
				        		</a>
				        	</h2>
				        	<input type="text" class="text" id="copyLink" value="${grailsApplication.config.personalwiki.app.domain}${grailsApplication.config.personalwiki.app.root}/${pageInstance.user.login}/${pageInstance.title.encodeAsURL()}"/>
							<span class="st_facebook"></span>
							<span class="st_twitter"></span>
							<span class="st_email"></span>
							<span class="st_delicious"></span>
							<span class="st_reddit"></span>
					    </div>
			        </g:if>
	        	</g:pageTitle>
			</div>
	        
        	<div id="flash-message-container">
	            <g:if test="${flash.message}">
	            	<div class="message" id="flash-message">${flash.message}</div>
	            </g:if>
	        </div>
	        
            <div class="dialog">
            	<div id="page-content"><g:wikify myPage="${pageInstance}" user="${session.user?.login}"/></div>
            </div>
            <g:if test="${pageInstance.editableBy(session.user)}">
	            <div class="buttons">
	                <g:form>
	                    <g:hiddenField name="id" value="${pageInstance?.id}" />
	                    <span class="button"><g:actionSubmit class="edit" id="edit-btn" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" title="${message(code: 'custom.shortcut.edit', default: 'Shortcut: Enter')}"/></span>
	                    <g:if test="${pageInstance?.user?.login?.equals(session.user.login)}">
	                    	<span class="button"><g:actionSubmit class="edit" id="settings-btn" action="editSettings" value="${message(code: 'custom.button.settings.label', default: 'Settings')}" title="${message(code: 'custom.shortcut.settings', default: 'Shortcut: Cmd-I or Ctrl-I')}"/></span>
	                    </g:if>
	                    <g:if test="${pageInstance?.user?.login?.equals(session.user.login) && !Page.homePageTitle.equalsIgnoreCase(pageInstance?.title)}">
	                    	<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
	                    </g:if>
	                </g:form>
	            </div>
            </g:if>
        </div>
        <g:javascript library="yui/selector-min" />
        <g:javascript library="yui/container-min" />
        <g:javascript library="shortcut" />
       	<g:if test="${'Courier New'.equals(pageInstance.font?.name) || (null == pageInstance.font && 'Courier New'.equals(session.user?.defaultFont?.name))}">
       		<g:javascript library="ios-monospace-fix" />
       	</g:if>
        <g:javascript library="pages/page/show" />
        <script language="javascript">

        var appRoot = "${grailsApplication.config.personalwiki.app.root}";
        
        </script>
    </body>
</html>
