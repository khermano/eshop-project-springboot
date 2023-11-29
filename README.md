# Eshop-Project-SpringBoot

This repository contains a migration of the backend of the monolith application to the microservice
architecture with Spring Boot framework. 

This migration was done for the master thesis which can be found here: 
https://is.muni.cz/auth/rozpis/tema?fakulta=1433;balik=58;tema=418130;uplne_info=1

The most relevant part of the project we migrated can be found here: 
https://github.com/khermano/433511_Master_thesis_project

## **Prerequisites:**
- Java 21
- Maven 3.9.4

## **How to run this:**

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

## About the service:

- we need to have every project of this repository up and running
- application endpoints will be then available at http://localhost:8080
- Eureka server info can be found here: http://localhost:8761