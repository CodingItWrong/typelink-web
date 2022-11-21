
<%@ page import="personalwiki.Subscription" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'subscription.label', default: 'Subscription')}" />
        <title><g:message code="custom.subscribe.title" default="Subscribe - TypeLink" /></title>
    </head>
    <body>
    	<g:render template="/tiles/nav"/>
        <div class="body">
        	<h1><g:message code="custom.user.upgrade.${accountType}"/></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <div class="comparison clearfix">
            	<div class="account-type">
            		<div class="account-type-inner">
		            	<h2>TypeLink Basic</h2>
		            	<div class="features">
		           			<ul>
		           				<li>Access to the TypeLink webapp and iOS app, for iPhone, iPad, and iPod Touch.</li>
		           				<li>Access your information anywhere.</li>
		           				<li>Create up to 10 pages.</li>
		           			</ul>
		            	</div>
		            	<div class="cost">
		            		<span>Cost:</span>
		            		Free
		            	</div>
	            	</div>
            	</div>
            	<div class="account-type account-type-last">
            		<div class="account-type-inner">
		            	<h2>TypeLink Pro</h2>
		            	<div class="features">
	            			<p>Everything in TypeLink Basic, PLUS:</p>
	            			<ul>
	            				<li>No limit on the number of pages you can create.</li>
	            			</ul>
		            	</div>
		            	<div class="cost">
		            		<span>Cost:</span>
	           				Only $5 per year!
	           				
	           				<div class="payment-box clearfix">
					        	<g:render template="/tiles/paypalButton"/>
								
								<!-- PayPal Logo -->
								<div class="paypal-logo">
									<a href="#" onclick="javascript:window.open('https://www.paypal.com/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside','olcwhatispaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=400, height=350');"><img  src="https://www.paypal.com/en_US/i/bnr/bnr_shopNowUsing_150x40.gif" border="0" alt="Additional Options"></a>
								</div>
								<!-- PayPal Logo -->
								
								<p>
								IMPORTANT! After you check out, if you are not
								automatically returned to TypeLink, be sure to
								click "Return to Typelink" to complete your subscription.
								</p>
							</div>
						</div>
					</div>
            	</div>
            </div>
            
            <h2>Frequently Asked Questions</h2>
            
            <dl class="faq">
            	<dt>Is subscribing secure?</dt>
            	<dd>Yes. TypeLink uses PayPal's secure servers to process your transaction. Credit card information is never transferred off of PayPal's secure servers.</dd>
            	
            	<dt>How do I renew my subscription?</dt>
            	<dd>You can purchase another subscription at any time, and another year will be added on after when your current subscription runs out.</dd>
            	
            	<dt>What if I forget to renew my subscription? Will I lose my pages?</dt>
            	<dd>Don't worry, your pages are safe! Just re-subscribe at any time and you'll be set to create new pages again.</dd>

				<dt>What if I decide not to use TypeLink anymore? Will I lose my pages?</dt>
				<dd>Don't worry, your pages aren't stuck with us! You can use the Export feature at any time to save your pages to your computer. Then you can use them in any other notetaking app you like.</dd>
            </dl>
            
        </div>
    </body>
</html>
