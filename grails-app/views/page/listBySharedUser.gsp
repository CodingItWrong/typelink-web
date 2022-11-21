
<%@ page import="personalwiki.Page" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="userName" value="${params.userName}" />
        <title><g:message code="custom.title.shared" args="[userName]" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1>Shared to ${params.userName}</h1>
            <div class="sub-menu">
            	<span><a class="list" href="${createLink(uri: '/'+params.userName)}">${params.userName}'s Pages</a></span>
            </div>
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
                        
                            <th>${message(code: 'custom.page.user.label', default: 'User')}</th>
                        
                            <th class="content">${message(code: 'page.content.label', default: 'Content')}</th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${pageInstanceList}" status="i" var="pageInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>
                            	<span class="perm-icon ${pageInstance.publiclyVisible?'public':''}">
                            	<span class="perm-icon ${pageInstance.sharedTo.size()>0?'shared':''}">
                            	<g:pageLink page="${pageInstance}" user="${session.user.login}"/>
                            	</span>
                            	</span>
                            </td>
                            
                            <td>${pageInstance.user.login}</td>
                        
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
