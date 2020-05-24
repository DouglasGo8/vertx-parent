# cvc-hotel-broker-quarkus-vertx project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `cvc-hotel-broker-quarkus-vertx-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/cvc-hotel-broker-quarkus-vertx-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/cvc-hotel-broker-quarkus-vertx-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

## **Vert.x Bridging over Camel <~> CVC Hotel Broker By City Name**

##### Microservice based on [Quarkus framework] (https://quarkus.io), Quarkus tailors your application for GraalVM and HotSpot. Amazingly fast boot time, incredibly low RSS memory (not just heap size!) offering near instant scale up and high density memory utilization in container orchestration platforms like Kubernetes. We use a technique we call compile time boot, [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, 
---
#### Compile and Run with Apache Maven
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ mvn clean -DskipTests compile exec:java 
$ mvn clean -DskipTests compile package -Pnative
````
#### Payloads and Tests
**see /snapshots/postman/fullOverview_Test1.png**

#### Running jar with conf dir on the root 
````
$ 

````
#### Endpoint 
**see /snapshots/postman/fullOverview_Test1.png**

