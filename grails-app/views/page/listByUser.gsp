
<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="userName" value="${params.userName}" />
        <title><g:message code="custom.title.list" args="[userName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1>${params.userName}'s Pages</h1>
            <g:if test="${params.userName.equalsIgnoreCase(session.user?.login)}">
            	<div class="sub-menu">
            		<span><a class="list" href="${createLink(uri: '/'+params.userName+'/shared-to')}">${message(code: 'custom.link.shared.to.user', args:[params.userName], default: 'Shared To '+params.userName)}</a></span>
            		|
            		<span class="export"><a class="list" href="${createLink(uri: '/'+params.userName+'/export')}">${message(code: 'custom.link.export', default: 'Export')}</a></span>
            		|
            		<span class="import"><a class="list" href="${createLink(uri: '/'+params.userName+'/import')}">${message(code: 'custom.link.import', default: 'Import')}</a></span>
            	</div>
            </g:if>
        	<div id="flash-message-container">
	            <g:if test="${flash.message}">
	            	<div class="message" id="flash-message">${flash.message}</div>
	            </g:if>
	        </div>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <th>${message(code: 'page.title.label', default: 'Title')}</th>
                        
                            <th class="content">${message(code: 'page.content.label', default: 'Content')}</th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${pageInstanceList}" status="i" var="pageInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>
                            	<span class="perm-icon ${pageInstance.publiclyVisible?'public':''}">
                            	<span class="perm-icon ${pageInstance.sharedTo.size()>0?'shared':''}">
                            	<g:pageLink page="${pageInstance}" user="${session.user}"/>
                            	</span>
                            	</span>
                            </td>
                        
                            <td class="content"><g:maxlength length="100" string="${pageInstance?.content.encodeAsHTML()}"/></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate params="${params}" total="${pageInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
