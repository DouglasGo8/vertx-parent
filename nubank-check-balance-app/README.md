# Vert.x NuBank Check and Balances Evaluations

Microservice based on Vert.x framework <http://vertx.io />, Vert.x is toolkit lightweight that provides
non-blocking and reactive apis

<https://github.com/vert-x3/vertx-junit5/tree/master/vertx-junit5-web-client/src/main/java/examples />

<https://vertx.io/blog/unit-and-integration-tests/>

<https://vertx.io/blog/unit-and-integration-tests />

# Compile

$ mvn clean -DskipTests compile
$ mvn clean -DskipTests package 

# Running only with jar 

$ java -jar target/nubank-check-balance-app-1.0-SNAPSHOT-fat.jar
Or
$ java -jar nubank-check-balance-app-1.0-SNAPSHOT-fat.jar

# Docker Release
docker build -t nubank-app .
docker run --rm --name nubank-app -p 32666:32666 nubank-app

# OKD Release
oc cluster up
oc login -u developer -p welcome1
oc new-build --binary --name=nubank-check-balance-app -l app=nubank-check-balance-app
mvn dependency:copy-dependencies compile
oc start-build nubank-check-balance-app --from-dir=. --follow
oc new-app nubank-check-balance-app -l app=nubank-check-balance-app
oc expose service nubank-check-balance-app

# Endpoint 
<http://localhost:32666/api/greeting/sayHello />

# Endpoints GET Methods (Works only with a default Customer Created in memory)

<http://localhost:32666/api/greeting/dbatista />
<http://localhost:32666/api/status/balance/30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4/0998872-1 />
<http://localhost:32666/api/greeting/unknown />

# Endpoints POST
<http://localhost:32666/api/account/add/credit />

# Raw body (Header Content-Type application/json)
{
	"id": "30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4",
	"acc": "0998872-1",
	"credit": 487.12
}

# Endpoint PUT

<http://localhost:32666/api/account/withdraw />

# Raw body Header Content-Type application/json)

{
	"id": "30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4",
	"acc": "0998872-1",
	"debit": 87.12
}

# Unit Test

mvn clean test

# Integration Test

mvn clean verify

# K8s Release

kubectl create -f nubank-check-balance-app-pod.yaml
kubectl get po pod-name-here -o yaml
kubectl get po pod-name-here -o json
kubectl get pods
kubectl logs pod-name-here
kubectl logs pod-name-here -c container-name-here (multiples containers options)
kubectl port-forward pod-name-here 32666:8080 (curl http://localhost:8888/endpoint-here)

# K8s w/ Labels

kubectl get po --show-labels
kubectl get po -L tier,ship

kubectl delete po pod-name-here
kubectl delete po -L name=nubank-check-balance-app,tier=backend
kubectl delete all --all (caution)