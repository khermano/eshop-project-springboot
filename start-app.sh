#!/bin/bash

PORT_8761=$(lsof -i tcp:8761)
PORT_8081=$(lsof -i tcp:8081)
PORT_8082=$(lsof -i tcp:8082)
PORT_8083=$(lsof -i tcp:8083)
PORT_8084=$(lsof -i tcp:8084)
PORT_8080=$(lsof -i tcp:8080)
if [ "$PORT_8761" != "" ]; then
    echo "Error! Port 8761 in use!"
    echo "$PORT_8761"
elif [ "$PORT_8081" != "" ]; then
    echo "Error! Port 8081 in use!"
    echo "$PORT_8081"
elif [ "$PORT_8082" != "" ]; then
    echo "Error! Port 8082 in use!"
    echo "$PORT_8082"
elif [ "$PORT_8083" != "" ]; then
    echo "Error! Port 8083 in use!"
    echo "$PORT_8083"
elif [ "$PORT_8084" != "" ]; then
    echo "Error! Port 8084 in use!"
    echo "$PORT_8084"
elif [ "$PORT_8080" != "" ]; then
    echo "Error! Port 8080 in use!"
    echo "$PORT_8080"
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
    while true; do
    read -rsn1 input
    if [ "$input" = "q" ]; then
        echo "Stopping start-app script"
        pkill -P $$
    fi
    done
fi