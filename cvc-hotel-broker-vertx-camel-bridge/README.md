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
$ mvn clean -DskipTests compile exec:java 
````
#### Payloads and Tests
**see /snapshots/postman/fullOverview_Test1.png**

#### Running jar with conf dir on the root 
````
$ java -jar target/cvc-hotel-broker-1.0-SNAPSHOT-fat.jar
````
#### Endpoint 
**see /snapshots/postman/fullOverview_Test1.png**

