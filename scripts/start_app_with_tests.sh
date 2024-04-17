#!/bin/bash

test_port () {
  if [ "$(lsof -i tcp:"$1")" != "" ]; then
      echo "Error! Port $1 in use!"
      exit 1
  fi
}

run_service () {
  cd "$1" || exit
  mvn clean install
  cd ..
  java -jar "$1"/target/"$1"-0.0.1-SNAPSHOT.jar &
  pids[$2]=$!
}

app_available () {
  if echo "$output" | grep -q "users" && echo "$output" | grep -q "categories" && echo "$output" | grep -q "products" && echo "$output" | grep -q "orders" && echo "$output" | grep -q "api-gateway-service"; then
          return 0
      else
          return 1
      fi
}

test_port 8761
test_port 8081
test_port 8082
test_port 8083
test_port 8084
test_port 8080

cd ..
run_service serviceRegistry 0
run_service userService 1
run_service categoryService 2
run_service productService 3
run_service orderService 4
run_service apiGateway 5

while true; do
  output=$(curl localhost:8080/actuator/health | jq)
  if app_available; then
    echo
    echo "All services registered."
    break
  else
    echo "One or more services are not registered yet. Retrying..."
    sleep 3
  fi
done

echo "You can now access the application endpoints at http://localhost:8080."
echo "In Swagger UI choose a service with Select a definition."
read -rsn1 -p "For quit press any button and wait until application shutdown is completed."
echo
pkill -P $$
for pid in ${pids[*]}
do
  wait $pid
done
echo
echo "Application shutdown completed."