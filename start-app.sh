#!/bin/bash

if [ "$(lsof -i tcp:8761)" != "" ]; then
    echo "Error! Port 8761 in use!"
elif [ "$(lsof -i tcp:8081)" != "" ]; then
    echo "Error! Port 8081 in use!"
elif [ "$(lsof -i tcp:8082)" != "" ]; then
    echo "Error! Port 8082 in use!"
elif [ "$(lsof -i tcp:8083)" != "" ]; then
    echo "Error! Port 8083 in use!"
elif [ "$(lsof -i tcp:8084)" != "" ]; then
    echo "Error! Port 8084 in use!"
elif [ "$(lsof -i tcp:8080)" != "" ]; then
    echo "Error! Port 8080 in use!"
else
    cd serviceRegistry
    mvn clean install
    mvn spring-boot:run &
    cd ..
    cd userService
    mvn clean install
    mvn spring-boot:run &
    cd ..
    cd categoryService
    mvn clean install
    mvn spring-boot:run &
    cd ..
    cd productService
    mvn clean install
    mvn spring-boot:run &
    cd ..
    cd orderService
    mvn clean install
    mvn spring-boot:run &
    cd ..
    cd apiGateway
    mvn clean install
    mvn spring-boot:run &
    cd ..
    sleep 8
    echo ""
    echo "You can now access the application's endpoint at http://localhost:8080/swagger-ui.html"
    echo "Press q to quit"
    read -rsn1 input
    if [ "$input" = "q" ]; then
        pkill -P $$
    fi
    sleep 8
    echo ""
    echo "Application shutdown completed..."
fi