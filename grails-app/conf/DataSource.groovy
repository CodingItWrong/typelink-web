dataSource {
    pooled = true
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:hsqldb:mem:devDB"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "typelink_test_username"
            password = "REPLACEME"
            url = "jdbc:mysql://DOMAIN:3306/TEST_DATABASE?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }
    beta {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "typelink_beta_username"
            password = "REPLACEME"
            url = "jdbc:mysql://DOMAIN:3306/BETA_DATABASE?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "typelink_beta_username"
            password = "REPLACEME"
            url = "jdbc:mysql://DOMAIN:3306/PROD_DATABASE?autoreconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }
}
