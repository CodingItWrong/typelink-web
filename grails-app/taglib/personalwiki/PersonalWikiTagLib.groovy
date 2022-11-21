package personalwiki

import java.util.regex.Pattern;
import grails.converters.*

class PersonalWikiTagLib {
	def json = { attrs ->
		out << (attrs['object'] as JSON);
	}
	
	def pageLink = { attrs ->
		def page = attrs['page']
		def user = attrs['user'].toString()
		def styleClass = null;
		if( page.publiclyVisible ) {
			styleClass = "public"
		} else if( page.sharedTo.size() > 0 ) {
			styleClass = "shared"
		} else {
			styleClass = "private"
		}
		
		out << "<a href='"
		out << createLink(uri: '/'+page.user.login+'/'+page.title.encodeAsURL())
		out << "' class=${styleClass}>${page.title.encodeAsHTML()}</a>"
	}
	
	def pageTitle = { attrs, body ->
		def page = attrs['myPage']
		def titleText = null
		def hrClass = null
		def styleClass1 = ""
		def styleClass2 = ""
		
		if( page?.user?.login?.equals( session.user?.login ) ) {
			titleText = page?.title.encodeAsHTML()
		} else {
			titleText =
				"<a href='" + createLink(uri: '/'+page.user.login) + "'>" +
				page?.user?.login +
				"</a>" +
				" / " +
				page?.title.encodeAsHTML()
		}

		hrClass = "private"
		if( page.sharedTo.size() > 0 ) {
			hrClass = "shared" // overwrite
			styleClass2 = "shared"
		}
		if( page.publiclyVisible ) {
			hrClass = "public" // overwrite
			styleClass1 = "public"
		}
		
		out << "<h1 class='${hrClass}'><span class='perm-icon ${styleClass1}'><span class='perm-icon ${styleClass2}'>${titleText}</span></span>"
		out << body()
		out << "</h1>"
	}
	
	def wikify = { attrs ->
		def user = attrs['user']
		def page = attrs['myPage']
		
		// non-wiki-link replacements
		def content = page.content.encodeAsHTML() 	// special characters
			.replaceAll( /\r\n?|\n/, "<br/>")		// line breaks
			/*
			.replaceAll(							// inline images
				/(?i)\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|]\.(gif|jpg|jpeg|png)/,
				'<a href="$0"><img src="$0"/></a>' )*/
		content = replaceIterative( content,		// make urls clickable
				/(?i)(^|>)([^<]*)\b(https?|ftp|file)(:\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;\(\)\[\]]*[-A-Z0-9+&@#\/%=~_|\(\)\[\]])([^>]*)(<[^\/]|$)/,
				'$1$2<a href="$3$4" class="external">$3$4</a>$5$6' )
		content = content.replaceAll(				// make e-mail addresses clickable
				/[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}/,
				'<a href="mailto:$0" class="external">$0</a>')
		content = replaceIterative( content,		// make twitter usernames clickable
				/(?i)(^|>)([^<]*)\B@([a-zA-Z0-9_]+)([^>]*)(<[^\/]|$)/,
				'$1$2<a href="http://twitter.com/$3" class="external">@$3</a>$4$5' )
		
		def viewer = session.user
		content = Page.findSummaryByUser( page.user, viewer ).inject( content )
			{ myContent, myPage ->
				def quotedMatch = Pattern.quote(myPage.match.encodeAsHTML())
				if( myPage.id == page.id ) { myContent } // do not link to current page
				else {
					def styleClass = "";
					if( myPage.publiclyVisible ) {
						styleClass = "public"
					} else if( myPage.sharedCount > 0 ) {
						styleClass = "shared"
					} else {
						styleClass = "private"
					}
					replaceIterative( myContent,
						/(?i)(^|>)([^<]*)(?<!\w)(${quotedMatch})(?!\w)([^>]*)(<[^\/]|$)/,
						'$1$2<a href="'+myPage.title.encodeAsURL()+'" class="'+styleClass+'">$3</a>$4$5' )
				}
			}
			
		// output results
		out << content
	}
	def maxlength = { attrs ->
		def s = attrs['string']
		def l = Integer.valueOf( attrs['length'] )
		if( null != s ) {
			out << ( s.length() > l ? s.substring( 0, l ) + "..." : s )
		}
	}
	
    /**
     * Render an authentication form
     * 
     * Attributes:
     * 
     * action - optional : will default to plugin's authentication controller with "authAction" as action
     * method - optional : defaults to POST (via g:form)
     * successUrl - map of controller/action/id params
     * errorUrl - map of controller/action/id params
     * Usage:
     * 
     * <auth:form authAction="login" successUrl="[controller:'portal', action:'justLoggedIn']"
     *     errorUrl="[controller:'content', action:'login']">
     *    <!-- input fields here, auto generated if blank body() -->
     * </auth:form>
     */
    def authForm = { attrs, body ->
        def authAction = attrs.remove('authAction')
        def args = [success:attrs.remove('success'), error:attrs.remove('error')]
        
        if (!args.success) {
            args.success = [:]
        }
        
        if (!args.error) {
            args.error = [:]
        }

        args.keySet().each() {
            def theparams = args[it]
            if (!theparams.controller) {
                theparams.controller = controllerName
                 // only autocomplete action if we autocomplete controller
                 if (!theparams.action) theparams.action = actionName
            }
        }
            
        def formAttrs = [:] + attrs
        if (!attrs.url) {
            if (!authAction)
                throwTagError("auth:form tag requires 'authAction' parameter to indicate login action")
            formAttrs.url = [controller:'session', action:authAction]
        }
            
        out << g.form(formAttrs) {
            args.keySet().each() { kind ->
                def url = args[kind]
                if (url.controller) {
                    out << g.hiddenField(name:"${kind}_controller", value: url.controller)
                }
                if (url.action) {
                    out << g.hiddenField(name:"${kind}_action", value: url.action)
                }
                if (url.id) {
                    out << g.hiddenField(name:"${kind}_id", value: url.id)
                }
            }
            if (body) {
                out << body() // Add to this - if body is null, automatically write a simple user+pass form with Log in button
            }
        }
    }
    
	/* helpers */
	
	/*
	 * Searches for a pattern and, if found, runs replaceFirst, then runs the pattern
	 * again from the start. This is helpful if you might have overlapping matches
	 * that wouldn't be found for a single run of replaceAll, but you need to make
	 * sure you don't get into an infinite loop by having your replacement match again.
	 */
	def replaceIterative = { string, pattern, replacement ->
	    def tempString = string; 
	    def matcher = string =~ pattern;
	    while( matcher.find() ) {
	        tempString = matcher.replaceFirst(replacement);
	        matcher = tempString =~ pattern;
	    }
	    tempString
	}
}
