FROM registry.access.redhat.com/ubi8/ubi-minimal
RUN mkdir -p /work

COPY ./target/*-runner /work/app
RUN chmod -R 775 /work

EXPOSE 12900

WORKDIR /work
#RUN chmod +x ./app

ENTRYPOINT ["./app","-Dquarkus.http.host=0.0.0.0"]
