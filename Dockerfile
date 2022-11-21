FROM tomcat:7.0.82-jre7

RUN rm -fr /usr/local/tomcat/webapps/ROOT
ADD ROOT.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
