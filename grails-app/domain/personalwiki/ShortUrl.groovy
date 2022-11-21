package personalwiki

class ShortUrl {

	String longUrl
	String shortUrl

	static mapping = {
		longUrl index: 'LongUrl_Idx'
	}

    static constraints = {
		longUrl(blank:false,maxlength:1000)
		shortUrl(blank:false,maxlength:40)
    }
}
