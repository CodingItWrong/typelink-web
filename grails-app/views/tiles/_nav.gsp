<%@ page import="personalwiki.Page" %>
<div class="nav clearfix">
	<auth:ifLoggedIn>
    	<span class="menuButton"><a class="home" href="${createLink(uri: '/'+session.user.login+'/'+Page.homePageTitle)}"><g:message code="default.home.label"/></a></span>
	    <span class="menuButton"><a class="list" href="${createLink(uri: '/'+session.user.login)}"><g:message code="default.list.label" args="[entityName]" /></a></span>
    	<span class="menuButton" id="createLink"><g:link class="create" controller="page" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
	</auth:ifLoggedIn>
</div>
