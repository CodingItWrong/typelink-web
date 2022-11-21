class UrlMappings {

	static mappings = {
		"/rest/alerts"{
			controller = "alert"
			action = [ GET:"restGet" ]
		}
		
		"/rest/fonts"{
			controller = "font"
			action = [ GET:"restGet" ]
		}
		
		"/rest/account"{
			controller = "user"
			action = [ GET:"restGet", PUT:"restPut" ]
		}
		
		"/rest/$userName"{
			controller = [ GET: "page", POST: "user" ]
			action = [ GET: "restGet", POST:"restPost" ]
		}
		
		"/rest/$userName/sharedTo"{
			controller = "page"
			action = [ GET:"restGetShared" ]
		}
		
		"/rest/$userName/$pageTitle"{
			controller = "page"
			action = [ GET:"restGet", PUT:"restPut", DELETE:"restDelete", POST:"restPost" ]
		}
		
		"/rest/$userName/$pageTitle?/sharedTo/$shareUserName"{
			controller = "share"
			action = [ DELETE:"restDelete", POST:"restPost" ]
		}
		
		"/rest/$userName/$pageTitle?/alias/$alias"{
			controller = "alias"
			action = [ DELETE:"restDelete", POST:"restPost" ]
		}
		
		"/$userName"{
			controller = "page"
			action = "listByUser"
			constraints {
				userName(matches:/^((?!^css$)(?!^js$)(?!^images$)(?!^session$)(?!^page$)(?!^share$)(?!^user$)(?!^alias$)(?!^subscription$).)*$/)
			}
		}
		
		"/account"{
			controller = "user"
			action = "edit"
			constraints {
				userName(matches:/^((?!^css$)(?!^js$)(?!^images$)(?!^session$)(?!^page$)(?!^share$)(?!^user$)(?!^alias$)(?!^subscription$).)*$/)
			}
		}
		
		"/account/unsubscribe"{
			controller="unsubscribeRequest"
			action="create"
		}
		
		"/account/unsubscribe/save"{
			controller="unsubscribeRequest"
			action="save"
		}
		
		"/account/unsubscribe/success"{
			controller="unsubscribeRequest"
			action="success"
		}
		
		"/account/history"{
			controller = "subscription"
			action = "list"
		}
		
		"/account/subscribe"{
			controller = "subscription"
			action = "index"
		}
		
		"/account/subscribe/create"{
			controller = "subscription"
			action = "save"
		}
		
		"/account/subscribe/success"{
			controller = "subscription"
			action = "show"
		}
		
		"/$userName/shared-to"{
			controller = "page"
			action = "listBySharedUser"
			constraints {
				userName(matches:/^((?!^css$)(?!^js$)(?!^images$)(?!^session$)(?!^page$)(?!^share$)(?!^user$)(?!^alias$)(?!^subscription$).)*$/)
			}
		}
		
		"/$userName/export"{
			controller = "page"
			action = "exportByUser"
			constraints {
				userName(matches:/^((?!^css$)(?!^js$)(?!^images$)(?!^session$)(?!^page$)(?!^share$)(?!^user$)(?!^alias$)(?!^subscription$).)*$/)
			}
		}
		
		"/$userName/import"{
			controller = "page"
			action = [ GET:"importByUser", POST:"importPost" ]
			constraints {
				userName(matches:/^((?!^css$)(?!^js$)(?!^images$)(?!^session$)(?!^page$)(?!^share$)(?!^user$)(?!^alias$)(?!^subscription$).)*$/)
			}
		}
		
		"/$userName/$pageTitle"{
			controller = "page"
			action = "showByUserAndTitle"
			constraints {
				userName(matches:/^((?!^css$)(?!^js$)(?!^images$)(?!^session$)(?!^page$)(?!^share$)(?!^user$)(?!^alias$)(?!^subscription$)(?!^account$).)*$/)
			}
		}
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"{
			controller = "session"
			action = "index"
		}
		//"/"(view:"/index")
		"500"(view:'/error')
	}
}
