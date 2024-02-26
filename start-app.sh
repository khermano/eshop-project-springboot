#!/bin/bash

cd serviceRegistry
mvn clean install
gnome-terminal -- sh -c "mvn spring-boot:run;  bash"
cd ..
cd userService
mvn clean install
gnome-terminal -- sh -c "mvn spring-boot:run;  bash"
cd ..
cd categoryService
mvn clean install
gnome-terminal -- sh -c "mvn spring-boot:run;  bash"
cd ..
cd productService
mvn clean install
gnome-terminal -- sh -c "mvn spring-boot:run;  bash"
cd ..
cd orderService
mvn clean install
gnome-terminal -- sh -c "mvn spring-boot:run;  bash"
cd ..
cd apiGateway
mvn clean install
gnome-terminal -- sh -c "mvn spring-boot:run;  bash"
cd ..