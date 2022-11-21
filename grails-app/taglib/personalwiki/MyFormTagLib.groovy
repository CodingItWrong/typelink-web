package personalwiki

class MyFormTagLib {
	def emailField = { attrs ->
		attrs.tagName = "textField"
		attrs.type = "email"
		fieldImpl(out, attrs)
	}
	
	/* copied from FormTagLib */
    def fieldImpl(out, attrs) {
        resolveAttributes(attrs)
        attrs.id = attrs.id ? attrs.id : attrs.name
        out << "<input type=\"${attrs.remove('type')}\" "
        outputAttributes(attrs)
        out << "/>"
    }

    /**
    * Check required attributes, set the id to name if no id supplied, extract bean values etc.
    */
    void resolveAttributes(attrs) {
        if (!attrs.name && !attrs.field) {
            throwTagError("Tag [${attrs.tagName}] is missing required attribute [name] or [field]")
        }

        attrs.remove('tagName')

        attrs.id = (!attrs.id ? attrs.name : attrs.id)

        def val = attrs.remove('bean')
        if (val) {
            if (attrs.name.indexOf('.')) {
                attrs.name.split('\\.').each {val = val?."$it"}
            }
            else {
                val = val[name]
            }
            attrs.value = val
        }
        attrs.value = (attrs.value != null ? attrs.value : "")
    }

    /**
     * Dump out attributes in HTML compliant fashion
     */
    void outputAttributes(attrs) {
        attrs.remove('tagName') // Just in case one is left
        def writer = getOut()
        attrs.each {k, v ->
            writer << "$k=\"${v.encodeAsHTML()}\" "
        }
    }

}
