# typelink-web

TypeLink personal wiki webapp and API for iOS app.

**WARNING:** this app has been discontinued and will not be maintained. The author is not available for support. But feel free to fork it to use for your own purposes.

## Running Locally

*NOTE: these instructions have not been updated for many years, and more may be needed. In particular, it may be difficult to install a compatible version of Groovy, and you may instead need to update the app to work with a newer version.*

- Install Grails 1.3.4 locally (note that there is a zipped version of it included in the repo at `grails-1.3.4.zip`)
- Create database manually, filling in configuration values into
  `grails-app/conf/DataSource.groovy` and
  CREATE DATABASE typelink
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
- I recommend disabling PayPal support instead of trying to set it up; it hasn't worked for several years. But if you want to set it up, the config is in `grails-app/conf/Config.groovy`
- There are also SMTP server and bit.ly configurations in `grails-app/conf/Config.groovy` - if you need those, update the credentials there.
- `grails run-app`
- Seed data is populated automatically from
  grails-app/conf/BootStrap.groovy

## Installing on a Server

- `grails war` (uses production settings)
- copy target/ROOT.war into /var/lib/tomcat6/webapps

## License

MIT
