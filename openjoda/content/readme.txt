Install it - www.xo3.com.

Delete /content first from /tomcat/webapps/root
Copy my /content
Copy my root//webapps/Jetspeed-properties.jcfg

Copy JetspeedResources.properties to tomcat/webapps/root/web-inf/

Copy my PortletCache  to /jetspeed/src/java/org/...../disk/memory
Copy my JetspeedTopNavigation.java to 	/jetspeed/src/java/org/apache/jetspeed/turbine/navigation
Run build from /jetspeed/build/build

Copy my /openjoda/jboss2/bin/openjoda.bat

Delete /jboss2/deploy/*.ejb

Start Siena Server at port 31337 on localserver

start apache web-server for code download

start server from menu

open /jetspeed home from menu OR start http://localhost:8080/servlet/jetspeed