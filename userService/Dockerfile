FROM docker.io/library/openjdk:21
LABEL maintainer="433511@mail.muni.cz"
COPY target/userService-0.0.1-SNAPSHOT.jar userService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/userService-0.0.1-SNAPSHOT.jar"]