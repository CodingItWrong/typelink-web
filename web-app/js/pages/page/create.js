var formSubmitting = false

new function(){
	// cursor to title field
	window.onload = function() {
		var titleField = document.getElementById("title")
		titleField.focus()
    };
    
    // prevent accidental navigation
    window.onbeforeunload = function() { if( !formSubmitting && !disableUnload ) { return "Your changes will not be saved."; } }
    
    // switch to iOS checkboxes
	if( iOS() ) {
	    document.observe("dom:loaded", function() {
	      new iPhoneStyle('input[type=checkbox]');
	    });
	}
}()
