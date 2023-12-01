# Eshop-Project-SpringBoot

This repository contains a migration of the backend of the monolith application to the microservice
architecture with Spring Boot framework. 

This migration was done for the master thesis which, can be found here: 
https://is.muni.cz/auth/rozpis/tema?fakulta=1433;balik=58;tema=418130;uplne_info=1

The most relevant part of the project we migrated can be found here: 
https://github.com/khermano/433511_Master_thesis_project

## **Prerequisites**
- Java 21
- Maven 3.9.4+

## **Build application**

***Run serviceRegistry (Eureka server):***
- `cd eshop-project-springboot/serviceRegistry`
- `mvn clean install`
- `mvn spring-boot:run`

***Run userService (Eureka client - service):***
- `cd eshop-project-springboot/userService`
- `mvn clean install`
- `mvn spring-boot:run`

***Run categoryService (Eureka client - service):***
- `cd eshop-project-springboot/categoryService`
- `mvn clean install`
- `mvn spring-boot:run`

***Run productService (Eureka client - service):***
- `cd eshop-project-springboot/productService`
- `mvn clean install`
- `mvn spring-boot:run`

***Run orderService (Eureka client - service):***
- `cd eshop-project-springboot/orderService`
- `mvn clean install`
- `mvn spring-boot:run`

***Run apiGateway (Eureka client - API Gateway):***
- `cd eshop-project-springboot/apiGateway`
- `mvn clean install`
- `mvn spring-boot:run`

## About the service

- every project of this repository has to be up and running for the application to work correctly
- application endpoints will be then available at http://localhost:8080
- Eureka server info can be found here: http://localhost:8761

### Health info details

- application uses Spring Actuator to provide information about the health of the services
- the status of individual services can be found on the Eureka server: http://localhost:8761
- more details about the health of the application can be found on endpoints:
  - serviceRegistry: http://localhost:8761/actuator/health
  - userService: http://localhost:8081/actuator/health
  - categoryService: http://localhost:8082/actuator/health
  - productService: http://localhost:8083/actuator/health
  - orderService: http://localhost:8084/actuator/health
  - apiGateway: http://localhost:8080/actuator/health

### Swagger UI

- is available at http://localhost:8080/swagger-ui.html
- you can choose a service with "Select a definition"

### Spring Boot Dev Tools

- we can use Live Reload in IntelliJ IDEA following this video https://www.youtube.com/watch?v=BvIM9gNTDM4&ab_channel=coder4life
- NOTE: you need to run the application from IntelliJ IDEA
- NOTE: you need to wait a couple of seconds until the application reloads itself