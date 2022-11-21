package personalwiki

class Page {

	User user
	String title
	String content
	boolean publiclyVisible
	Font font
	
	static hasMany = [ sharedTo: Share, aliases: Alias ]
	
	static homePageTitle = "Home"
	
    static constraints = {
		user(nullable:false)
		title(blank:false,maxlength:100,matches:"[^/\\\\]+")
		publiclyVisible()
		content()
		font(nullable:true)
    }
	
	static mapping = {
		columns {
			content type:'text'
		}
	}
	
	String toString() {
		user?.login + '/' + title
	}
	
	static countByUser = { owner, viewer ->
		// if logged in and on your own pages, link to all your pages
		if( owner == viewer ) {
			def query = "select count(*) as n" +
					" from Page as p" +
					" where p.user = :owner"
			//println query
			Page.executeQuery(query, [owner: owner])[0]
		
		// if logged in and not on your own pages, link to all this person's pages shared to you or public 
		} else if( null != viewer ) {
			def query = "select count(*) as n" +
					" from Page as p" +
					" left outer join p.sharedTo s" +
					" where p.user = :owner" +
					" and (p.publiclyVisible = true or s.user = :viewer)"
			//println query
			Page.executeQuery(query, [owner: owner, viewer:viewer])[0]
						//.collect { it[0] } // extract just the page, not the share
			
		// if not logged in, link to this person's public pages
		} else {
			def query = "select count(*) as n " +
					" from Page as p" +
					" where p.user = :owner and publiclyVisible = true"
			//println query
			Page.executeQuery(query, [owner: owner])[0]
		}
	}
	
    static findByUser = { owner, viewer, order, max, offset ->
		if( -1 == max ) { max = null }
		if( -1 == offset ) { offset = 0 }
		// if logged in and on your own pages, link to all your pages
		if( owner == viewer ) {
			def query = "from Page as p" +
					" where upper(p.user) = upper(:owner)" +
					" order by ${order}"
			//println query
			if( max ) {
				Page.findAll(query, [owner: owner, max: max, offset: offset])
			} else {
				Page.findAll(query, [owner: owner, offset: offset])
			}
		
		// if logged in and not on your own pages, link to all this person's pages shared to you or public 
		} else if( null != viewer ) {
			def query = "from Page as p" +
					" left outer join p.sharedTo s" +
					" where upper(p.user) = upper(:owner)" +
					" and (p.publiclyVisible = true or s.user = :viewer)" +
					" order by ${order}"
			//println query
			if( max ) {
				Page.findAll(query, [owner: owner, viewer:viewer, max: max, offset: offset])
							.collect { it[0] } // extract just the page, not the share
			} else {
				Page.findAll(query, [owner: owner, viewer:viewer, offset: offset])
							.collect { it[0] } // extract just the page, not the share
			}
			
		// if not logged in, link to this person's public pages
		} else {
			def query = "from Page as p" +
					" where upper(p.user) = upper(:owner) and publiclyVisible = true" +
					" order by ${order}"
			//println query
			if( max ) {
				Page.findAll(query, [owner: owner, max: max, offset: offset])
			} else {
				Page.findAll(query, [owner: owner, offset: offset])
			}
		}
	}
	
	static findSummaryByUser = { owner, viewer ->
		// if logged in and on your own pages, link to all your pages
		if( owner == viewer ) {
			def pageQuery = "select new map( id as id, title as match, title as title, publiclyVisible as publiclyVisible," +
					" (select count(*) from p.sharedTo) as sharedCount)" +
					" from Page as p" +
					" where upper(p.user) = upper(:owner)" +
					" order by length(p.title) desc"
			def pageResults = Page.executeQuery(pageQuery, [owner: owner])
			
			def aliasQuery = "select new map( p.id as id, a.name as match, p.title as title, " +
					" p.publiclyVisible as publiclyVisible," +
					" (select count(*) from p.sharedTo) as sharedCount)" +
					" from Alias as a" +
					" left outer join a.page p" +
					" where upper(a.page.user) = upper(:owner)" +
					" order by length(a.name) desc"
			def aliasResults = Page.executeQuery(aliasQuery, [owner: owner])
		
			return pageResults + aliasResults
			
		// if logged in and not on your own pages, link to all this person's pages shared to you or public 
		} else if( null != viewer ) {
			def pageQuery = "select new map( p.id as id, p.title as match, p.title as title, p.publiclyVisible as publiclyVisible," +
					" (select count(*) from p.sharedTo) as sharedCount)" +
					" from Page as p" +
					" left outer join p.sharedTo s" +
					" where upper(p.user) = upper(:owner)" +
					" and (p.publiclyVisible = true or s.user = :viewer)" +
					" order by length(p.title) desc"
			def pageResults = Page.executeQuery(pageQuery, [owner: owner, viewer:viewer])
			
			def aliasQuery = "select new map( p.id as id, a.name as match, p.title as title, " +
					" p.publiclyVisible as publiclyVisible," +
					" (select count(*) from p.sharedTo) as sharedCount)" +
					" from Alias as a" +
					" left outer join a.page p" +
					" left outer join p.sharedTo s" +
					" where upper(p.user) = upper(:owner)" +
					" and (p.publiclyVisible = true or s.user = :viewer)" +
					" order by length(a.name) desc"
			def aliasResults = Page.executeQuery(aliasQuery, [owner: owner, viewer:viewer])

			return pageResults + aliasResults
			
		// if not logged in, link to this person's public pages
		} else {
			def pageQuery = "select new map( id as id, title as match, title as title, publiclyVisible as publiclyVisible," +
					" (select count(*) from p.sharedTo) as sharedCount)" +
					" from Page as p" +
					" where upper(p.user) = upper(:owner) and publiclyVisible = true" +
					" order by length(p.title) desc"

			def pageResults = Page.executeQuery(pageQuery, [owner: owner])
			
			def aliasQuery = "select new map( p.id as id, a.name as match, p.title as title, " +
					" p.publiclyVisible as publiclyVisible," +
					" (select count(*) from p.sharedTo) as sharedCount)" +
					" from Alias as a" +
					" left outer join a.page p" +
					" where upper(p.user) = upper(:owner) and p.publiclyVisible = true" +
					" order by length(a.name) desc"
			def aliasResults = Page.executeQuery(aliasQuery, [owner: owner])
			
			return pageResults + aliasResults
		}
	}
	
	static countForUser = { owner, viewer ->
		if( owner == viewer ) {
		} else if( null != viewer ) {
		} else {
		}
    }

    static findByUserAndTitle = { userName, pageTitle ->
		// search by title
		def results = Page.findAll(
				"from Page as p " +
				"where upper(p.user.login) = upper(:userName) " +
				"and title = :pageTitle",
				[userName: userName, pageTitle: pageTitle])
		if( results[0] ) {
			return results[0]
		}
		
		// if not found, search by alias
		results = Page.findAll(
				"from Page as p " +
				"join p.aliases as a " +
				"where upper(p.user.login) = upper(:userName) " +
				"and a.name = :pageTitle",
				[userName: userName, pageTitle: pageTitle])
		if( results[0] ) {
			return results[0][0] // second 0 is to get just the page, not the alias
		}
		return null
	}

    static findOtherByUserAndTitle = { userName, pageTitle, id ->
		def results = Page.findAll(
				"from Page as p " +
				"where upper(p.user.login) = upper(:userName) " +
				"and title = :pageTitle " +
				"and id != :id",
				[userName: userName, pageTitle: pageTitle, id: id])
        results[0] 
	}
	
	static countBySharedTo = { viewer ->
		def query = "select count(*) as n" +
				" from Page as p" +
				" left outer join p.sharedTo s" +
				" where s.user = :viewer"
		//println query
		Page.executeQuery(query, [viewer:viewer])[0]
    }
	
	static findBySharedTo = { viewer, max, offset ->
		if( -1 == max ) { max = 1000 }
		if( -1 == offset ) { offset = 0 }
		def query = "from Page as p" +
				" left outer join p.sharedTo s" +
				" where s.user = :viewer" +
				" order by upper(p.title) asc"
		//println query
		Page.findAll(query, [viewer:viewer, max:max, offset:offset])
					.collect { it[0] } // extract just the page, not the share
    }
	
	static createHomePage = { user ->
			new Page(user:user,title:Page.homePageTitle,content:"""\
Welcome to your new TypeLink home page! To get started, you will want to create some new pages by linking to them. For help on how to do this, watch this helpful video.

http://www.youtube.com/watch?v=wTQ8D-JY4dw
(When you're finished watching this video, you can delete the YouTube link from this page to remove it from your home page. You can get back to it again at the tour video link below.)

Next Steps 

- To see the rest of the tour videos: http://typelink.net/about/TypeLink+Tour
- To learn more about TypeLink: http://typelink.net/about/Home
- For help using TypeLink: http://typelink.net/help/How+To+Use+TypeLink
""")
	}
	
	def viewableBy = { user ->
		if( this.publiclyVisible ) { return true }
		else if( this.user.login == user?.login ) { return true }
		else {
			def sharedToUser = false
			sharedTo.each { if( it.user.login.equalsIgnoreCase(user?.login) ) { sharedToUser = true } }
			return sharedToUser
		}
    }
	
	def editableBy = { user ->
		if( this.user.login == user?.login ) { return true }
		else {
			def sharedToUser = false
			sharedTo.each { if( it.user.login.equalsIgnoreCase(user?.login) ) { sharedToUser = true } }
			return sharedToUser
		}
    }
	
}
