# spring-boot-springmvc-springwebclient (careteam-interface-client)
Spring Boot app that implements the following:
* Spring MVC + Thymleaf UI
* Spring REST client for the CareTeam_Interface module.
    
# Stack
* Spring Boot
* Thymeleaf

# Prerequisites 
* Java 11 or above
* Install Docker Desktop if you are going to run the docker image.

# Quick Start (with Docker image)
* Login into GitHub Packages and pull the docker image:
    * ```docker login -u mhvaleriearena -p 838af7ed53bb2d5d1b8eefdc26cafe202d97da2c docker.pkg.github.com```
    * ```docker pull docker.pkg.github.com/mhvaleriearena/careteam-interface-client/ctclient_docker:v1```
* Run the docker image:
    * ```docker run -p 8080:8080 --name ctclient docker.pkg.github.com/mhvaleriearena/careteam-interface-client/ctclient_docker:v1``` 
* Follow guidelines under Usage.
* Stop the docker container:      
    * ```docker stop ctclient```
* ```http://localhost:8080/ctassignments``    
    
# Quick Start (without Docker image)
* Clone the git repo.
* Build and run the app:
    * ```./gradlew bootRun ```
* Follow guidelines under Usage.    
* Stop the app:
    * ```./gradlew –stop```
* ```http://localhost:8080/ctassignments```

# Setup
* Clone git repo.
* Import the project into your IDE if you would like to step through the code.

# Configuration & Deployment
* Settings are configured in application.properties under resources. Descriptions for each setting are in application.properties.
* To run the app via the command line without building and then to stop, cd to the root of the repo end execute:
    * ```./gradlew bootRun```
    * ```./gradlew –stop ```
* To build the app via the command line and then run, cd to the root of the repo and execute (if you do not want to skip tests, remove '-x test'):
    * ```./gradlew clean build -x test```
    * ```java -jar build/libs/ctclient-1.0.0.jar ```
* To build a docker image via the command line and then run:   
    * ```./gradlew jibDockerBuild --image=ctclient_docker```
    * ```docker run -p 8080:8080 --name ctclient ctclient_docker```
* To stop and remove the docker container:      
    * ```docker stop ctclient```
    * ```docker rm ctclient```
* To delete the image from your local repository, first find the image ID and the remove it using the image ID:
    * ```docker images```    
    * ```docker rmi IMAGE ID```
* If you would like to step through the code, build and then run the app as a Java application in your IDE. 

# Usage
* Open the following URL in your browser: http://localhost:8080/ctassignments.
* To get an access token, click the Get Access Token link. NOTE: The access token is stored in mememory. If you restart the app, please remember to retrieve another access token.
* To make a patient assignment, click "Make Patient Assignment" and follow the instructions displayed.
* To make a location assignment, click "Make Location Assignment" and follow the instructions displayed.
* To view a patient's care team, click "View Care Team" and follow the instructions displayed.

# Monitoring
* The log file name, path, and log level can be configured in application.properties. 
* Spring Boot Actuator Endpoints: http://localhost:8080/actuator

# Troubleshooting
* If you run the app on macOS and you receive the Tomcat Native library error below, you need to install 'tomcat-native' via brew:

_Brew Command_
```
brew install tomcat-native
```

_Tomcat Native Library Exception_
```
org.apache.tomcat.jni.LibraryNotFoundError: Can't load library: /Users/valeriearena/bitbucket/ctclient/bin/libtcnative-1.dylib, Can't load library: /Users/valeriearena/bitbucket/ctclient/bin/liblibtcnative-1.dylib, no tcnative-1 in java.library.path, no libtcnative-1 in java.library.path
	at org.apache.tomcat.jni.Library.<init>(Library.java:102) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.tomcat.jni.Library.initialize(Library.java:206) ~[tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.AprLifecycleListener.init(AprLifecycleListener.java:198) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.apache.catalina.core.AprLifecycleListener.isAprAvailable(AprLifecycleListener.java:107) [tomcat-embed-core-9.0.30.jar:9.0.30]
	at org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory.getDefaultLifecycleListeners(TomcatServletWebServerFactory.java:168) [spring-boot-2.2.4.RELEASE.jar:2.2.4.RELEASE]
```

* Spring Boot automatically configures Reactor Netty as the default server for Spring WebFlux. If you run the app on Java 9 or above with logging.level.root=DEBUG, the netty library logs the exception below. The stacktrace can be ignored or the Java internal APIs can be accessed with the following Java options:

_Java Opts To Access Internal APIS_
```
java -jar build/libs/ctclient-1.0.0.jar --add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true
```

_Netty Library Exception_
```
java.lang.UnsupportedOperationException: Reflective setAccessible(true) disabled
	at io.netty.util.internal.ReflectionUtil.trySetAccessible(ReflectionUtil.java:31) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.internal.PlatformDependent0$4.run(PlatformDependent0.java:225) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at java.base/java.security.AccessController.doPrivileged(Native Method) ~[na:na]
	at io.netty.util.internal.PlatformDependent0.<clinit>(PlatformDependent0.java:219) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.internal.PlatformDependent.isAndroid(PlatformDependent.java:273) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.internal.PlatformDependent.<clinit>(PlatformDependent.java:92) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.ConstantPool.<init>(ConstantPool.java:32) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.channel.ChannelOption$1.<init>(ChannelOption.java:36) ~[netty-transport-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.channel.ChannelOption.<clinit>(ChannelOption.java:36) ~[netty-transport-4.1.45.Final.jar!/:4.1.45.Final]

java.lang.IllegalAccessException: class io.netty.util.internal.PlatformDependent0$6 cannot access class jdk.internal.misc.Unsafe (in module java.base) because module java.base does not export jdk.internal.misc to unnamed module @6cce16f4
	at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361) ~[na:na]
	at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:591) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:558) ~[na:na]
	at io.netty.util.internal.PlatformDependent0$6.run(PlatformDependent0.java:335) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at java.base/java.security.AccessController.doPrivileged(Native Method) ~[na:na]
	at io.netty.util.internal.PlatformDependent0.<clinit>(PlatformDependent0.java:326) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.internal.PlatformDependent.isAndroid(PlatformDependent.java:273) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.internal.PlatformDependent.<clinit>(PlatformDependent.java:92) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.util.ConstantPool.<init>(ConstantPool.java:32) ~[netty-common-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.channel.ChannelOption$1.<init>(ChannelOption.java:36) ~[netty-transport-4.1.45.Final.jar!/:4.1.45.Final]
	at io.netty.channel.ChannelOption.<clinit>(ChannelOption.java:36) ~[netty-transport-4.1.45.Final.jar!/:4.1.45.Final]
```
