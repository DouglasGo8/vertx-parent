### **Vert.x Bridging over Camel <~> Primeiro Pay :: Pre Auth Service**

##### Microservice based on [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, [Apache Camel] (https://camel.apache.org) is an open source integration framework that empowers you to quickly and easily integrate various systems consuming or producing data. 
---
#### Compile and Run with Apache Maven
* JDK 8 (Mandatory)
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro.utils -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd ..
$ cd preauthorization-service
$ mvn clean -DskipTests -Dauthentication=YOUR_AUTH_HERE -q compile exec:java
````
#### Running the jar
* Inside preauthorization-service dir
````
$ mvn clean -DskipTests -q compile package 
````
* Inside target dir
````
$ java -jar oopwa-preauthorization-service-1.0-SNAPSHOT-fat.jar -Dauthentication={{YOUR_AUTH_HERE}}
````
#### Payload(s)
**see**
* ./snapshots/postman/preauth/*.png
* (http://localhost:12080/api/v1/preauth/payment) (POST)(PA)
```json
{
    "amount": "92.00",
    "currency": "EUR",
    "paymentType": "PA",
    "paymentBrand": "VISA",
    "documentNumber": "94429110077",
    "cardCvv": "123",
    "cardExpiryYear": "2020",
    "cardExpiryMonth": "05",
    "cardNumber": "4200000000000000",
    "cardHolder": "Jane Jone"
}
```
* (http://localhost:12080/api/v1/preauth/payment) (POST)(DB)
```json
{
    "amount": "92.00",
    "entityId": "8ac7a4ca6db97ef1016dbe9214e70aac",
    "currency": "EUR",
    "paymentType": "DB",
    "paymentBrand": "VISA",
    "documentNumber": "94429110077",
    "cardCvv": "123",
    "cardExpiryYear": "2020",
    "cardExpiryMonth": "05",
    "cardNumber": "4200000000000000",
    "cardHolder": "Jane Jone"
}
```
#### Docker (with env:propertyPlaceHolder)
````
$ mvn clean -DskipTests -q compile package
$ docker build -t preauth-service .
$ docker run --rm -e authentication={{AUTH_HERE}} -p 12080:12080 --rm --name app1 preauth-service
````
#### Docker Hub
docker tag preauth-service:latest douglasdb/preauth-service:latest
docker push douglasdb/preauth-service:latest

##### Quick Run with docker-compose (Needs Authorization Code in Env)
* Inside primeiropay-payments dir
````
$ cd docker
$ docker-compose up -d
````
#### Openshift
* (under construction)

<hr />

### **Vert.x Bridging over Camel <~> Primeiro Pay :: Capture Service**

##### Microservice based on [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, [Apache Camel] (https://camel.apache.org) is an open source integration framework that empowers you to quickly and easily integrate various systems consuming or producing data. 
---
#### Compile and Run with Apache Maven
* JDK 8 (Mandatory)
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro.utils -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd ..
$ cd capture-service
$ mvn clean -DskipTests -Dauthentication=YOUR_AUTH_HERE -q compile exec:java
````
#### Running the jar
* Inside capture-service dir
````
$ mvn clean -DskipTests -q compile package 
````
* Inside target dir
````
$ java -jar oopwa-capture-service-1.0-SNAPSHOT-fat.jar -Dauthentication={{YOUR_AUTH_HERE}}
````
#### Payload(s)
**see**
* ./snapshots/postman/capture/*.png
* (http://localhost:12081/api/v1/capture/rebill)(POST)(PA id)
```json
{
	"id": "8ac7a4a2725fb8f401726b74e450367d",
	"amount": "10.00",
	"entityId": "8a8294184e736012014e78a17a5615ac",
	"currency": "EUR",
	"paymentType": "CP"
}
```
#### Docker (with env:propertyPlaceHolder)
````
$ mvn clean -DskipTests -q compile package
$ docker build -t capture-service .
$ docker run --rm -e authentication={{AUTH_HERE}} -p 12081:12081 --rm --name app2 capture-service
````
#### Docker Hub
docker tag capture-service:latest douglasdb/capture-service:latest
docker push douglasdb/capture-service:latest

##### Quick Run with docker-compose (Needs Authorization Code in Envs)
* Inside primeiropay-payments dir
````
$ cd docker
$ docker-compose up -d
````
#### Openshift
* (under construction)

<hr />

### **Vert.x Bridging over Camel <~> Primeiro Pay :: Refund Service**

##### Microservice based on [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, [Apache Camel] (https://camel.apache.org) is an open source integration framework that empowers you to quickly and easily integrate various systems consuming or producing data. 
---
#### Compile and Run with Apache Maven
* JDK 8 (Mandatory)
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro.utils -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd ..
$ cd refund-service
$ mvn clean -DskipTests -Dauthentication=YOUR_AUTH_HERE -q compile exec:java
````
#### Running the jar
* Inside capture-service dir
````
$ mvn clean -DskipTests -q compile package 
````
* Inside target dir
````
$ java -jar oopwa-refund-service-1.0-SNAPSHOT-fat.jar -Dauthentication={{YOUR_AUTH_HERE}}
````
#### Payload(s)
**see**
* ./snapshots/postman/capture/*.png
* (http://localhost:12081/api/v1/capture/rebill)(POST)(PA id)
```json
{
	"id": "8ac7a4a2725fb8f401726b74e450367d",
	"amount": "10.00",
	"entityId": "8a8294184e736012014e78a17a5615ac",
	"currency": "EUR",
	"paymentType": "CP"
}
```
#### Docker (with env:propertyPlaceHolder)
````
$ mvn clean -DskipTests -q compile package
$ docker build -t refund-service .
$ docker run --rm -e authentication={{AUTH_HERE}} -p 12082:12082 --rm --name app3 refund-service
````
#### Docker Hub
docker tag refund-service:latest douglasdb/refund-service:latest
docker push douglasdb/refund-service:latest

##### Quick Run with docker-compose (Needs Authorization Code in Envs)
* Inside primeiropay-payments dir
````
$ cd docker
$ docker-compose up -d
````
#### Openshift
* (under construction)

