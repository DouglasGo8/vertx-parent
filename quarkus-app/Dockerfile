FROM registry.fedoraproject.org/fedora-minimal
RUN mkdir -p /work

COPY ./target/*-runner /work/app
RUN chmod -R 775 /work

EXPOSE 12900

WORKDIR /work

ENTRYPOINT ["./app","-Dquarkus.http.host=0.0.0.0"]
