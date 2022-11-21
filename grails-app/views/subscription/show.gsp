
<%@ page import="personalwiki.Subscription" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'subscription.label', default: 'Subscription')}" />
        <title><g:message code="custom.subscription.success.title" default="Subscribed - TypeLink" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
            <h1><g:message code="custom.subscription.success" default="Now You're a Pro!" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <div class="content">
	            <p>
	            	Your TypeLink Pro subscription is complete, and you now have
	            	access to create as many pages as you like. Go nuts!
	            </p>

				<p>
					The information on your order is below. You will also
					receive a receipt via e-mail from PayPal. You may log into
					<a href="${grailsApplication.config.personalwiki.paypal.account.url}" target="_blank">your PayPal account</a>
					to view details of this transaction.
				</p>

	            <div class="dialog left-fields">
	                    
	            	<div class="value clearfix">
	                    <label class="field"><g:message code="subscription.payerEmail.label" default="Payer Email" /></label>
	                    <span class="value">${fieldValue(bean: subscriptionInstance, field: "payerEmail")}</span>
	            	</div>
	            	
	            	<div class="value clearfix">
	                    <label class="field"><g:message code="subscription.transactionId.label" default="Transaction Id" /></label>
	                    <span class="value">${fieldValue(bean: subscriptionInstance, field: "transactionId")}</span>
	            	</div>
	            	
	            	<div class="value clearfix">
	                    <label class="field"><g:message code="subscription.purchaseDate.label" default="Purchase Date" /></label>
	                    <span class="value"><g:formatDate format="MM/dd/yyyy" date="${subscriptionInstance?.purchaseDate}" /></span>
	            	</div>
	            	
	            	<div class="value clearfix">
	                    <label class="field"><g:message code="subscription.startDate.label" default="Start Date" /></label>
	                    <span class="value"><g:formatDate format="MM/dd/yyyy" date="${subscriptionInstance?.startDate}" /></span>
	            	</div>
	            	
	            	<div class="value clearfix">
	                    <label class="field"><g:message code="subscription.endDate.label" default="End Date" /></label>
	                    <span class="value"><g:formatDate format="MM/dd/yyyy" date="${subscriptionInstance?.endDate}" /></span>
	            	</div>
	            	
	            </div>
	
				<p>
					You can purchase another subscription at any time, and
					another year will be added on after when your current
					subscription runs out.
				</p>
				
				<p>
					If you forget and your subscription runs out, don't worry--your
					pages won't be lost. Just re-subscribe and you'll be set! And if
					you ever decide to stop using TypeLink, your pages aren't stuck
					with us. Just export them and you can take them wherever you
					like.
				</p>
				
				<p>
					If you have any questions about your subscription, please e-mail
					<a href="mailto:billing@typelink.net">billing@typelink.net</a>.
				</p>
			</div>
			
        </div>
    </body>
</html>
