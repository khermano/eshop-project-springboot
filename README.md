# Eshop-Project-SpringBoot

This repository contains a migration of the backend of the monolith application to the microservice
architecture with the Spring Boot framework. 

This migration was done for the master thesis, which can be found here: 
https://is.muni.cz/auth/rozpis/tema?fakulta=1433;balik=58;tema=418130;uplne_info=1

The most relevant part of the project we migrated can be found here: 
https://github.com/khermano/433511_Master_thesis_project

## **Prerequisites**
- Java 21
- Maven 3.9.5

## **Build application**

**Run script:**
- `cd eshop-project-springboot/`
- `chmod +x start-app.sh`
- `./start-app.sh`

**Or start services individually:**

*Run serviceRegistry (Eureka server):*
- `cd eshop-project-springboot/serviceRegistry`
- `mvn clean install`
- `mvn spring-boot:run`

*Run userService (Eureka client - service):*
- `cd eshop-project-springboot/userService`
- `mvn clean install`
- `mvn spring-boot:run`

*Run categoryService (Eureka client - service):*
- `cd eshop-project-springboot/categoryService`
- `mvn clean install`
- `mvn spring-boot:run`

*Run productService (Eureka client - service):*
- `cd eshop-project-springboot/productService`
- `mvn clean install`
- `mvn spring-boot:run`

*Run orderService (Eureka client - service):*
- `cd eshop-project-springboot/orderService`
- `mvn clean install`
- `mvn spring-boot:run`

*Run apiGateway (Eureka client - API Gateway):*
- `cd eshop-project-springboot/apiGateway`
- `mvn clean install`
- `mvn spring-boot:run`

## Endpoints info

**Users**

- **GET http://localhost:8080/eshop-rest/users**
  - *returns all users*
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/users`


- **GET http://localhost:8080/eshop-rest/users/{id}**
  - *returns user according to id*
  - **parameters**:
    - id: 
      - id of the user
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/users/1`

**Categories**

- **GET http://localhost:8080/eshop-rest/categories**
  - *returns all categories*
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/categories`


- **GET http://localhost:8080/eshop-rest/categories/{id}**
  - *returns category according to id*
  - **parameters**:
    - id: 
      - id of the category
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/categories/1`

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
    - `curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL` 
    - `curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE`


- **GET http://localhost:8080/eshop-rest/orders/by_user_id/{user_id}**
  - *returns all orders created by a user with the given id*
  - **parameters**:
    - user_id: 
      - id of the user
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/orders/by_user_id/1`


- **GET http://localhost:8080/eshop-rest/orders/{id}**
  - *returns order with given id*
  - **parameters**:
    - id: 
      - id of the order
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/orders/1`
  
  
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
  - e.g.: `curl -i -X POST http://localhost:8080/eshop-rest/orders/2?action=FINISH`


**Products**

- **GET http://localhost:8080/eshop-rest/products**
  - *returns all products*
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/products`


- **GET http://localhost:8080/eshop-rest/products/{id}**
  - *returns the product with the given id*
  - **parameters**:
    - id: 
      - id of the product
  - e.g.: `curl -i -X GET http://localhost:8080/eshop-rest/products/1`


- **DELETE http://localhost:8080/eshop-rest/products/{id}**
  - *deletes a product with the given id*
  - **parameters**:
    - id:
      - id of the product
  - e.g.: `curl -i -X DELETE http://localhost:8080/eshop-rest/products/1`


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
  - e.g.: `curl -X POST -i -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}' http://localhost:8080/eshop-rest/products/create`


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
  - e.g.: `curl -X PUT -i -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' http://localhost:8080/eshop-rest/products/4`


- **POST http://localhost:8080/eshop-rest/products/{id}/categories**
  - *adds a new category to the product*
  - **parameters**:
    - id (required):
      - id of the product to be updated
  - **request body**:
    - id (required):
      - id of existing category we want to add to the product
  - e.g.: `curl -X POST -i -H "Content-Type: application/json" --data '5' http://localhost:8080/eshop-rest/products/2/categories`
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
  - e.g.: `curl -i -X GET http://localhost:8080/products/2/currentPrice`


- **GET http://localhost:8080/products/getCurrencyRate/{currency1}/{currency2}**
  - *not part of the original project!*
  - *get the currency rate for a given currency pair*
  - **parameters**:
    - currency1 (required):
      - Available values: CZK, EUR, USD
    - currency2 (required):
      - Available values: CZK, EUR, USD
  - e.g.: `curl -i -X GET http://localhost:8080/products/getCurrencyRate/CZK/EUR`

## About the app

- every service of this repository has to be up and running for the application to work correctly
- application endpoints will then be available at http://localhost:8080
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

- we can use Live Reload in IntelliJ IDEA following this video: https://www.youtube.com/watch?v=BvIM9gNTDM4&ab_channel=coder4life
- NOTE: you need to run the application from IntelliJ IDEA
- NOTE: you need to wait a couple of seconds until the application reloads itself