FROM dordoka/tomcat
MAINTAINER arun
ADD training.war /opt/tomcat/webapps/
CMD ["/opt/tomcat/bin/catalina.sh", "run"]