FROM docker.io/library/openjdk:21
LABEL maintainer="433511@mail.muni.cz"
COPY target/orderService-0.0.1-SNAPSHOT.jar orderService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/orderService-0.0.1-SNAPSHOT.jar"]