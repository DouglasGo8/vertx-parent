### **Vert.x Bridging over Camel <~> Primeiro Pay :: Pre Auth Service**

##### Microservice based on [Vert.x framework] (http://vertx.io), Vert.x is toolkit lightweight for building reactive application on JVM, [Apache Camel] (https://camel.apache.org) is an open source integration framework that empowers you to quickly and easily integrate various systems consuming or producing data. 
---
#### Compile and Run with Apache Maven
* If using Powershell, wrap -D parameters in double quotes
````
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
$ mvn install:install-file -Dfile=target/utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro.utils -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar
$ cd ..
$ cd pre-authorization-svc
$ mvn clean -DskipTests -DentityId=YOUR_ENTITY_HERE -Dauthentication=YOUR_AUTH_HERE compile exec:java 
````
#### Running the jar
````
$ mvn clean -DskipTests -q compile package 
````
* Inside target dir
````
$ java -jar oopwa-pre-auth-svc-1.0-SNAPSHOT-fat.jar \ 
 -DpreAuthUrl=https://test.oppwa.com/v1/payments \
 -DentityId={{YOUR_ENTITY_HERE}} -Dauthentication={{YOUR_AUTH_HERE}}
````
#### Payload(s)
**see **
* /pre-authorization-svc/snapshots/postman/paymentPostSuccessMessage.png
* (http://localhost:12088/api/v1/preauth/payment) (POST)
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

#### Docker 
````
$ mvn clean -DskipTests -q compile package
$ docker build -t pre-auth-svc .
$ docker run -e preAuthUrl=https://test.oppwa.com/v1/payments \
 -e entityId={{ENTITY_HERE}} \
 -e authentication={{AUTH_HERE}} \
 -p 12088:12088 --rm --name app1 pre-auth-svc
````
#### Docker Hub
docker tag pre-auth-svc:latest douglasdb/pre-auth-svc:latest
docker push douglasdb/pre-auth-svc:latest

##### Quick Run with docker-compose (Needs EntityId and Authorization Codes Envs)
* Inside primeiro-pay-payments dir
````
$ cd docker
$ docker-compose up -d
````
#### Openshift
