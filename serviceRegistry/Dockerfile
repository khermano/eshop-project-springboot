FROM docker.io/library/openjdk:21
LABEL maintainer="433511@mail.muni.cz"
COPY target/serviceRegistry-0.0.1-SNAPSHOT.jar serviceRegistry-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/serviceRegistry-0.0.1-SNAPSHOT.jar"]