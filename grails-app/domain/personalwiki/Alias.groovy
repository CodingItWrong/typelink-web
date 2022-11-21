package personalwiki

class Alias {

	String name

	static belongsTo = [ page: Page ]

    static constraints = {
		page(nullable:false)
		name(blank:false,maxlength:100)
    }
		
	String toString() {
		name
	}

}
