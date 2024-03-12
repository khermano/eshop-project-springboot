#!/bin/bash

test_port () {
  if [ "$(lsof -i tcp:$1)" != "" ]; then
      echo "Error! Port $1 in use!"
      exit 1
  fi
}

run_service () {
  cd $1
  mvn clean install
  java -jar target/$1-0.0.1-SNAPSHOT.jar &
  pids[$2]=$!
  cd ..
}

test_port 8761
test_port 8081
test_port 8082
test_port 8083
test_port 8084
test_port 8080
run_service serviceRegistry 0
run_service userService 1
run_service categoryService 2
run_service productService 3
run_service orderService 4
run_service apiGateway 5
sleep 8
echo
echo "You can now access the application's endpoint at http://localhost:8080"
read -rsn1 -p "For quit press any button and wait until application shutdown is completed"
echo
pkill -P $$
for pid in ${pids[*]}
do
wait $pid
done
echo
echo "Application shutdown completed"