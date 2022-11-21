
<%@ page import="personalwiki.Subscription" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'subscription.label', default: 'Subscription')}" />
        <title><g:message code="custom.subscription.list.title" default="Subscription History - TypeLink" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="custom.subscription.list.label" default="Subscription History" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${'basic'==accountType || 'pro'==accountType}">
            	<a href="${createLink(uri: '/account/subscribe')}"><g:message code="custom.user.upgrade.${accountType}"/></a>
            </g:if>
            <div class="list">
                <table class="subscription">
                    <thead>
                        <tr>
                        
	                        <th><g:message code="subscription.transactionId.label" default="Transaction Id" /></th>
	                    
	                        <th><g:message code="subscription.purchaseDate.label" default="Purchase Date" /></th>
	                    
	                        <th><g:message code="subscription.payerEmail.label" default="Payer Email" /></th>
	                    
	                        <th><g:message code="subscription.startDate.label" default="Start Date" /></th>
	                    
	                        <th><g:message code="subscription.endDate.label" default="End Date" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:if test="${0 == subscriptionInstanceTotal}">
                    	<tr class="odd">
                    		<td colspan="5" class="empty"><g:message code="custom.subscription.list.empty" default="no subscriptions" /></td>
                    	</tr>
                    </g:if>
                    <g:each in="${subscriptionInstanceList}" status="i" var="subscriptionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'} ${i == 0 ? 'first' : ''}">
                        
	                        <td class="transactionId">
	                        	<label><g:message code="subscription.transactionId.label" default="Transaction Id" /></label>
	                        	${fieldValue(bean: subscriptionInstance, field: "transactionId")}
	                        </td>
	                    
	                        <td>
	                        	<label><g:message code="subscription.purchaseDate.label" default="Purchase Date" /></label>
	                        	<g:formatDate format="MM/dd/yyyy" date="${subscriptionInstance.purchaseDate}" />
	                        </td>
	                    
	                        <td>
	                        	<label><g:message code="subscription.payerEmail.label" default="Payer Email" /></label>
	                        	${fieldValue(bean: subscriptionInstance, field: "payerEmail")}
	                        </td>
	                    
	                        <td>
	                        	<label><g:message code="subscription.startDate.label" default="Start Date" /></label>
	                        	<g:formatDate format="MM/dd/yyyy" date="${subscriptionInstance.startDate}" />
	                        </td>
	                    
	                        <td>
	                        	<label><g:message code="subscription.endDate.label" default="End Date" /></label>
	                        	<g:formatDate format="MM/dd/yyyy" date="${subscriptionInstance.endDate}" />
	                        </td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons subscriptionPaginateButtons">
                <g:paginate total="${subscriptionInstanceTotal}" />
            </div>
            <div class="content">
	            <p>
	            	You can view additional details on these transactions by logging in to
	            	<a href="${grailsApplication.config.personalwiki.paypal.account.url}" target="_blank">your PayPal account</a>
	            	with the payer e-mail address you used to complete the transaction.
	            </p>
	            <p>
					If you have any questions about your subscription, please e-mail
					<a href="mailto:billing@typelink.net">billing@typelink.net</a>.
	            </p>
            </div>
        </div>
    </body>
</html>
