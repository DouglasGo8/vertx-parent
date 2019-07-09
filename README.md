# AWS Corretto
https://aws.amazon.com/blogs/opensource/using-graalvm-build-minimal-docker-images-java-applications/?sc_channel=sm&sc_campaign=Open_Source&sc_publisher=TWITTER&sc_country=Open+Source&sc_geo=GLOBAL&sc_outcome=adoption&trk=open_source_TWITTER&linkId=69306543


# Cluster

https://github.com/singhmarut/vertx-cluster


# AMQP

docker run -d --rm --hostname rabbitmq --name rabbitmq \
    -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=welcome1 \
    -p 15672:15672 -p 5672:5672 \
    rabbitmq:3-management