## **Vert.x Bridging over Camel <~> CVC Hotel Broker By City Name**

##### Microservice based on [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, [Apache Camel] (https://camel.apache.org) is an open source integration framework that empowers you to quickly and easily integrate various systems consuming or producing data. 
---
#### Compile and Run with Apache Maven
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd cvc-hotel-broker-vertx-camel-bridge
$ mvn clean -DskipTests compile exec:java 
````
#### Payload(s)
**see /snapshots/postman/getHotelByCityId.png.png**

#### Running the jar
* Inside target dir
````
$ java -jar cvc-hotel-broker-1.0-SNAPSHOT-fat.jar
````
#### Endpoint 
**see /snapshots/postman/getHotelByCityId.png**


Some issues or Mistakes report to sgtear@hotmail.com 

