<g:if test="${grailsApplication.config.personalwiki.paypal.sandbox}">
	<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
	<input type="hidden" name="cmd" value="_s-xclick">
	<input type="hidden" name="hosted_button_id" value="2QLJ8VSRGLFUL">
	<input type="image" src="https://www.sandbox.paypal.com/WEBSCR-640-20110306-1/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
	<img alt="" border="0" src="https://www.sandbox.paypal.com/WEBSCR-640-20110306-1/en_US/i/scr/pixel.gif" width="1" height="1">
	</form>
</g:if>
<g:else>
	<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
	<input type="hidden" name="cmd" value="_s-xclick">
	<input type="hidden" name="hosted_button_id" value="K5WGCU9JJJYCC">
	<input type="image" src="https://www.paypalobjects.com/WEBSCR-640-20110401-1/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
	<img alt="" border="0" src="https://www.paypalobjects.com/WEBSCR-640-20110401-1/en_US/i/scr/pixel.gif" width="1" height="1">
	</form>
</g:else>
