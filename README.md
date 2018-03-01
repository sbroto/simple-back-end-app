
{Project Name}
===================








* **src**: the source folder of the microservice

This is a Java Spring project.

* maven
* java 8
* cloud foundry cli

```
cd ./<microservice folder>
mvn clean install
```

In order to deploy or update the microservice to the cloud, its manifest has to be created using the manifest template and the following commands have to be executed:
```
cf login
cf push [-f <manifest file>]
```

The components of the application can be started locally for debugging or testing purpose.   

The java microservice can be started locally from your IDE using the dev profile.   
It can also be started manually with the following commands:
```
mvn clean install
cd target
java -jar <microservice>.jar -Dspring.profiles.active=dev
```
Access API documentation on {project-url}/swagger-ui.html


Add necessary detail about the API
