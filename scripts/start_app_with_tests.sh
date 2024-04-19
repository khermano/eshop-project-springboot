#!/bin/bash

test_port () {
  if [ "$(lsof -i tcp:"$1")" != "" ]; then
      echo "Error! Port $1 in use!"
      exit 1
  fi
}

run_postgres () {
  docker run --rm --name dev-postgres-"$1" -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB="$1"Service -p "$2":5432 postgres > logs/"$1".log &
}

kill_postgres () {
  docker kill dev-postgres-"$1"
}

postgres_available () {
  if cat scripts/logs/"$1".log | grep -q "database system is ready to accept connections"; then
    return 0
  else
    return 1
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
test_port 5431
test_port 5432
test_port 5433
test_port 5434

run_postgres user 5431
run_postgres category 5432
run_postgres product 5433
run_postgres order 5434

cd ..

run_service serviceRegistry 0

while true; do
  if postgres_available user; then
    run_service userService 1
    break
  else
    sleep 1
  fi
done

while true; do
  if postgres_available category; then
    run_service categoryService 2
    break
  else
    sleep 1
  fi
done

while true; do
  if postgres_available product; then
    run_service productService 3
    break
  else
    sleep 1
  fi
done

while true; do
  if postgres_available order; then
    run_service orderService 4
    break
  else
    sleep 1
  fi
done

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
kill_postgres user
kill_postgres category
kill_postgres product
kill_postgres order
sleep 1
echo
echo "Application shutdown completed."