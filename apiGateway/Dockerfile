FROM docker.io/library/openjdk:21
LABEL maintainer="433511@mail.muni.cz"
COPY target/apiGateway-0.0.1-SNAPSHOT.jar apiGateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/apiGateway-0.0.1-SNAPSHOT.jar"]