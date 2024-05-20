# eshop-project-springboot

- available at: https://github.com/khermano/eshop-project-springboot/

This repository contains a migration of the backend of the monolithic application to the microservice
architecture with the Spring Boot framework. 

This migration was done for the master thesis, which can be found here: 
https://is.muni.cz/auth/rozpis/tema?fakulta=1433;balik=58;tema=418130;uplne_info=1.

The most relevant part of the project we migrated can be found here: 
https://github.com/khermano/433511_Master_thesis_project.

The original project without changes can be found here:
https://github.com/fi-muni/PA165

## **Prerequisites**
- Java 21
- Maven 3.9.6
- optionally, newest Docker, or Podman 4.9.4 with podman-docker (see Podman )

## About the app

- every service of this repository has to be up and running for the application to work correctly
- first check that all services are up and running at http://localhost:8761
- application endpoints will then be available at http://localhost:8080
- the application consists of 6 microservices:

|     Service     |   Port   |
|:---------------:|:--------:|
| serviceRegistry |   8761   |
|   userService   |   8081   |
| categoryService |   8082   |
| productService  |   8083   |
|  orderService   |   8084   |
|   apiGateway    |   8080   |

## **Start application**

- you can use one of the two starting scripts available in the /scripts directory
- or you can use the docker-compose.yml file (with Docker/Podman)

### Run the script to start the application locally:
- `cd eshop-project-springboot/scripts/`
- `./start_app.sh` or
- `./start_app_with_tests.sh`
- wait for the message "All services registered." because registration of services and making it available through the API gateway can take a moment
- application endpoints are now available at http://localhost:8080 (see Swagger UI)

### Stop the script:
- press any button and wait for "Application shutdown completed."

### Start app using docker-compose.yml:

- for this option you need to have a Docker or Podman (see Podman)
- `cd eshop-project-springboot/`
- `podman compose up -d --build`
  - this command build images and then in background start containers defined in a docker-compose.yml file, detaching it from the current shell
- `podman compose down`
  - this command stops and removes containers created by docker-compose.yml

### Individual services:

*Build and run serviceRegistry (Eureka server) [port 8761]:*
- `cd eshop-project-springboot/serviceRegistry/`
- `mvn clean install`
- `java -jar target/serviceRegistry-0.0.1-SNAPSHOT.jar`

*Build and run userService (Eureka client - service) [port 8081]:*
- `cd eshop-project-springboot/userService/`
- `mvn clean install`
- `java -jar target/userService-0.0.1-SNAPSHOT.jar`

*Build and run categoryService (Eureka client - service) [port 8082]:*
- `cd eshop-project-springboot/categoryService/`
- `mvn clean install`
- `java -jar target/categoryService-0.0.1-SNAPSHOT.jar`

*Build and run productService (Eureka client - service) [port 8083]:*
- `cd eshop-project-springboot/productService/`
- `mvn clean install`
- `java -jar target/productService-0.0.1-SNAPSHOT.jar`

*Build and run orderService (Eureka client - service) [port 8084]:*
- `cd eshop-project-springboot/orderService/`
- `mvn clean install`
- `java -jar target/orderService-0.0.1-SNAPSHOT.jar`

*Build and run apiGateway (Eureka client - API Gateway) [port 8080]:*
- `cd eshop-project-springboot/apiGateway/`
- `mvn clean install`
- `java -jar target/apiGateway-0.0.1-SNAPSHOT.jar`

## Development

### Spring Boot Dev Tools

- we can use Live Reload in IntelliJ IDEA following this video: https://www.youtube.com/watch?v=BvIM9gNTDM4&ab_channel=coder4life
- NOTE: you need to run the application from IntelliJ IDEA
- NOTE: you need to wait a couple of seconds until the application reloads itself

## Endpoints info

- first check that all services are up and running at http://localhost:8761
- after that all endpoints are available with Swagger UI at http://localhost:8080 (see Swagger UI)
- you can use a verification script to make all example calls (the script should be executed once per one application run to work properly because of POST methods)
  - `cd eshop-project-springboot/scripts/`
  - `./verification.sh`

**Users**

- **GET http://localhost:8080/eshop-rest/users**
  - *returns all users*
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/users" | jq`


- **GET http://localhost:8080/eshop-rest/users/{id}**
  - *returns user according to id*
  - **parameters**:
    - id: 
      - id of the user
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/users/1" | jq`

**Categories**

- **GET http://localhost:8080/eshop-rest/categories**
  - *returns all categories*
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/categories" | jq`


- **GET http://localhost:8080/eshop-rest/categories/{id}**
  - *returns category according to id*
  - **parameters**:
    - id: 
      - id of the category
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/categories/1" | jq`

**Orders**
- **GET http://localhost:8080/eshop-rest/orders**
  - *returns all orders according to the given parameters*
  - **parameters**:
    - status:
      - Available values :
        - ALL: return all orders
        - RECEIVED: orders with StateOrder RECEIVED
        - CANCELED: orders with StateOrder CANCELED
        - SHIPPED: orders with StateOrder SHIPPED
        - DONE: orders with StateOrder DONE
    - last_week:
      - Available values :
        - true - returns orders by status created in last 7 days
        - false - returns all orders defined by status
  - e.g.: 
    - `curl -X GET "http://localhost:8080/eshop-rest/orders?status=ALL" | jq` 
    - `curl -X GET "http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE" | jq`


- **GET http://localhost:8080/eshop-rest/orders/by_user_id/{user_id}**
  - *returns all orders created by a user with the given id*
  - **parameters**:
    - user_id: 
      - id of the user
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/orders/by_user_id/1" | jq`


- **GET http://localhost:8080/eshop-rest/orders/{id}**
  - *returns order with given id*
  - **parameters**:
    - id: 
      - id of the order
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/orders/1" | jq`
  
  
- **POST http://localhost:8080/eshop-rest/orders/{order_id}**
  - *perform one action on the order, either canceling, shipping, or finishing the order*
  - *the only allowed changes of state are: RECEIVED -> CANCELED, RECEIVED -> SHIPPED, SHIPPED -> DONE*
  - **parameters**:
    - order_id: 
      - id of the order
    - action: 
      - Available values:
        - CANCEL: RECEIVED -> CANCELED
        - SHIP: RECEIVED -> SHIPPED
        - FINISH: SHIPPED -> DONE
  - e.g.: `curl -X POST "http://localhost:8080/eshop-rest/orders/2?action=FINISH" | jq`


**Products**

- **GET http://localhost:8080/eshop-rest/products**
  - *returns all products*
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products" | jq`


- **GET http://localhost:8080/eshop-rest/products/{id}**
  - *returns the product with the given id*
  - **parameters**:
    - id: 
      - id of the product
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products/1" | jq`


- **DELETE http://localhost:8080/eshop-rest/products/{id}**
  - *deletes a product with the given id*
  - **parameters**:
    - id:
      - id of the product
  - e.g.: `curl -X DELETE "http://localhost:8080/eshop-rest/products/1" | jq`


- **POST http://localhost:8080/eshop-rest/products/create**
  - *create a new product*
  - **request body**:
    - image: 
      - image file
    - imageMimeType (required if the image is added): 
      - image type 
    - name (required, must be unique): 
      - name of the product
    - description: 
      - product description
    - color: 
      - Available values: BLACK, WHITE, RED, GREEN, BLUE, ORANGE, YELLOW, AZURE, MAGENTA, BROWN, PINK, GREY, UNDEFINED
    - price (required): 
      - price of one piece of product
    - currency (required): 
      - Available values: CZK, EUR, USD
    - categoryId (required): 
      - id of category that exists
  - e.g.: `curl -X POST -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}' "http://localhost:8080/eshop-rest/products/create" | jq`


- **PUT http://localhost:8080/eshop-rest/products/{id}**
  - *update the price for one product (It is not allowed to change the price by more than 10%!)*
  - **parameters**:
    - id: 
      - id of product to be updated
  - **request body**:
    - value (required): 
      - It is not allowed to change the price by more than 10%!
    - currency (required): 
      - Available values: CZK, EUR, USD
  - e.g.: `curl -X PUT -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' "http://localhost:8080/eshop-rest/products/4" | jq`


- **POST http://localhost:8080/eshop-rest/products/{id}/categories**
  - *adds a new category to the product*
  - **parameters**:
    - id (required):
      - id of the product to be updated
  - **request body**:
    - id (required):
      - id of existing category we want to add to the product
  - e.g.: `curl -X POST -H "Content-Type: application/json" --data '5' "http://localhost:8080/eshop-rest/products/2/categories" | jq`
  - **original project request body**:
    - *it was changed because only the ID parameter of the required body is used for adding a category*
      - id (required):
        - id of the existing category we want to add to the product
      - name:
        - name of the existing category we want to add to the product (we need only ID of existing category we want to add)
  - e.g.: in the original project: `curl -X POST -i -H "Content-Type: application/json" --data '{"id":"5","name":"Presents"}' http://localhost:8080/eshop-rest/products/2/categories`


- **GET http://localhost:8080/products/{id}/currentPrice**
  - *not part of the original project!*
  - *get the current price of the product with the given id*
  - **parameters**:
    - id (required):
      - id of the product 
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products/2/currentPrice" | jq`


- **GET http://localhost:8080/products/getCurrencyRate/{currency1}/{currency2}**
  - *not part of the original project!*
  - *get the currency rate for a given currency pair*
  - **parameters**:
    - currency1 (required):
      - Available values: CZK, EUR, USD
    - currency2 (required):
      - Available values: CZK, EUR, USD
  - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR" | jq`

## Swagger UI

- is available at http://localhost:8080
- you can choose a service with "Select a definition"
- allows to visualize and interact with the API's resources without having any of the implementation logic in place
- more info here: https://swagger.io/tools/swagger-ui/?ref=the-best-recipe

## Health info details

- the status of individual services can be found on the Eureka server: http://localhost:8761
- NOTE: sometimes Eureka service discovery needs couple more seconds to load everything up
- application uses Spring Actuator to provide information about the health of the services
- more details about the health of the application can be found on endpoints:
  - serviceRegistry: http://localhost:8761/actuator/health
  - userService: http://localhost:8081/actuator/health
  - categoryService: http://localhost:8082/actuator/health
  - productService: http://localhost:8083/actuator/health
  - orderService: http://localhost:8084/actuator/health
  - apiGateway: http://localhost:8080/actuator/health

## Podman:
- if you are having Podman, you can install podman-docker to use docker aliases
  - `sudo dnf install podman-docker`
- after that you should be able to use Docker commands in Podman 