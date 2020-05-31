FROM adoptopenjdk/openjdk8:jdk8u252-b09-alpine

ENV VERTICLE_NAME com.primeiro.pay.oppwa.payments.capture.MainVertx
ENV VERTICLE_FILE oopwa-capture-service-1.0-SNAPSHOT-fat.jar
ENV VERTICLE_HOME .

EXPOSE 12081

COPY target/$VERTICLE_FILE $VERTICLE_HOME
COPY config.json $VERTICLE_HOME

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]

CMD ["exec java -jar $VERTICLE_FILE"]