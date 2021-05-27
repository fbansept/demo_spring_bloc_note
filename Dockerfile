FROM tomcat:8
COPY target/demo.war /usr/local/tomcat/webapps/demo.war
ENTRYPOINT ["/bin/bash", "/usr/local/tomcat/bin/catalina.sh", "run"]
