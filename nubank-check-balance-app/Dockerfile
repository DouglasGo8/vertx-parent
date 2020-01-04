FROM openjdk:11-jdk-slim

ENV VERTICLE_NAME com.nubank.check.balance.MainVerticle
ENV VERTICLE_FILE nubank-check-balance-app-1.0-SNAPSHOT-fat.jar
ENV VERTICLE_HOME .

EXPOSE 32666

COPY target/$VERTICLE_FILE $VERTICLE_HOME

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]

CMD ["exec java -jar $VERTICLE_FILE"]