# Vert.x bridge Camel <~> CVC Hotel Broker


Microservice based on Vert.x framework <http://vertx.io />, Vert.x is toolkit lightweight for building reactive
application on JVM, Apache Camel <https://camel.apache.org /> is an open source integration framework that empowers 
you to quickly and easily integrate various systems consuming or producing data. 


<https://github.com/vert-x3/vertx-junit5/tree/master/vertx-junit5-web-client/src/main/java/examples />
<https://vertx.io/blog/unit-and-integration-tests/>
<https://vertx.io/blog/unit-and-integration-tests />

# Compile and Run with Apache Maven
$ cd vertx-parent
$ mvn clean dependency:resolve
$ cd utils-lib
$ mvn clean -DskipTests package 
(Windows 10 installation needs double quotes)
$ mvn install:install-file -Dfile=.\target\utils-lib-1.0-SNAPSHOT.jar -DgroupId=io.vertx.pagseguro -DartifactId=utils-lib -Dversion=1.0-SNAPSHOT -Dpackaging=jar

$ mvn clean -DskipTests compile
$ mvn clean -DskipTests package 

# Running only with jar 

$ java -jar target/************pending
Or
$ java -jar *******pending

# Docker Release
docker build -t cvc-hotel-app .
docker run --rm --name cvc-hotel-app -p 32666:32666 cvc-hotel-app

# OKD Release
oc cluster up
oc login -u developer -p welcome1
oc new-build --binary --name=cvc-hotel-app-app -l app=cvc-hotel-app-app
mvn dependency:copy-dependencies compile
oc start-build cvc-hotel-app-app --from-dir=. --follow
oc new-app cvc-hotel-app-app -l app=cvc-hotel-app-app
oc expose service cvc-hotel-app-app

# Endpoint 
<http://localhost:32666/api/greeting/sayHello />

# Endpoints GET Methods (Works only with a default Customer Created in memory)

****************** PENDING



# Endpoint PUT

<http://localhost:32666/api/account/withdraw />

# Raw body Header Content-Type application/json)

# Unit Test

mvn clean test

# Integration Test

mvn clean verify