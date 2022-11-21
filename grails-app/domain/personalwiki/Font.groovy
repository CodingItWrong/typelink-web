package personalwiki

class Font {

	String name
	String iOSCode
	String cssCode

    static constraints = {
		name(blank:false,maxlength:30)
		iOSCode(blank:false,maxlength:30)
		cssCode(blank:false,maxlength:100)
    }
	
	String toString() {
		name
	}
}
