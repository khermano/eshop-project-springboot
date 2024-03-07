FROM docker.io/library/openjdk:21
LABEL maintainer="433511@mail.muni.cz"
COPY target/productService-0.0.1-SNAPSHOT.jar productService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/productService-0.0.1-SNAPSHOT.jar"]