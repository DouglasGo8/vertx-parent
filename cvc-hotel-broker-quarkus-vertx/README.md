## **Quarkus Reactive with Vert.x <~> CVC Hotel Broker By City Name**

##### Microservice based on [Quarkus framework] (https://quarkus.io), Quarkus tailors your application for GraalVM and HotSpot. Amazingly fast boot time, incredibly low RSS memory (not just heap size!) offering near instant scale up and high density memory utilization in container orchestration platforms like Kubernetes. We use a technique we call compile time boot, [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, 
---
#### Compile and Run
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd cvc-hotel-broker-quarkus-vertx
$ ./mvnw clean -DskipTests compile quarkus:dev
````
#### Compile and Run Native Images
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd cvc-hotel-broker-quarkus-vertx
$ ./mvnw clean -DskipTests package -Pnative
$ ./cvc-hotel-broker-quarkus-vertx-1.0.0-SNAPSHOT-runner
````
#### Payload(s)
**see /snapshots/postman/getHotelByID.png**

#### Running the jar
````
$ java -jar cvc-hotel-broker-quarkus-vertx-1.0.0-SNAPSHOT-runner.jar
````
#### Endpoint 
**see /snapshots/postman/getHotelByID.png**

Some issues or Mistakes report to sgtear@hotmail.com 