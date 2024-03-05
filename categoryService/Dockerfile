FROM alpine:latest

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk
ENV MAVEN_HOME=/usr/share/maven

RUN apk add --no-cache openjdk21 maven

LABEL maintainer="433511@mail.muni.cz"

WORKDIR /app

COPY . /app

RUN mvn clean install

CMD ["java", "-jar", "target/categoryService-0.0.1-SNAPSHOT.jar"]

EXPOSE 8082