package personalwiki

class Alert {

	String text
	boolean active

    static constraints = {
		text(blank:false)
    }
	
	static mapping = {
		columns {
			content type:'text'
		}
	}
	
}
