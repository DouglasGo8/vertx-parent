#  vertx-quarkus-app

Add instructions for project developers here.

mvn clean package -DskipTests -Dnative=true -Dnative-image.docker-build=true


# Links
<https://github.com/quarkusio/quarkus/issues/2822 />

create table fruits (id serial primary key  not null, name text not null)

insert into fruits(name) values ('orange')
insert into fruits(name) values ('watermelon')
insert into fruits(name) values ('apple')
insert into fruits(name) values ('banana')
insert into fruits(name) values ('pears')



minishift start --vm-driver virtualbox --show-libmachine-logs



minishift addon install ~/.addons/management-infra/
minishift addon apply management-infra
oc delete project/management-infra clusterrole/management-infra-admin clusterrole/hawkular-metrics-admin --as=system:admin



http://hawkular-grafana-grafana.192.168.99.100.nip.io
oc delete project -n grafana --as=system:admin