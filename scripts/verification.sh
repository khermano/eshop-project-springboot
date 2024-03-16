#!/bin/bash

echo
echo "This is verification script which executes example calls of endpoints provided by eshop-project-springboot application and shows their output."
echo "Please keep in mind that application must be up and running and the script should be used once per application run to work properly."
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/users | jq"
curl -X GET "http://localhost:8080/eshop-rest/users" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/users/1 | jq"
curl -X GET "http://localhost:8080/eshop-rest/users/1" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/categories | jq"
curl -X GET "http://localhost:8080/eshop-rest/categories" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/categories/1 | jq"
curl -X GET "http://localhost:8080/eshop-rest/categories/1" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/orders?status=ALL | jq"
curl -X GET "http://localhost:8080/eshop-rest/orders?status=ALL" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE | jq"
curl -X GET "http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/orders/by_user_id/1 | jq"
curl -X GET "http://localhost:8080/eshop-rest/orders/by_user_id/1" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/orders/1 | jq"
curl -X GET "http://localhost:8080/eshop-rest/orders/1" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X POST http://localhost:8080/eshop-rest/orders/2?action=FINISH | jq"
curl -X POST "http://localhost:8080/eshop-rest/orders/2?action=FINISH" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/products | jq"
curl -X GET "http://localhost:8080/eshop-rest/products" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/products/1 | jq"
curl -X GET "http://localhost:8080/eshop-rest/products/1" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X DELETE http://localhost:8080/eshop-rest/products/1 | jq"
curl -X DELETE "http://localhost:8080/eshop-rest/products/1" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X POST -H \"Content-Type: application/json\" --data '{\"name\":\"test\",\"description\":\"test\",\"color\":\"UNDEFINED\",\"price\":\"200\", \"currency\":\"CZK\", \"categoryId\":\"1\"}' http://localhost:8080/eshop-rest/products/create | jq"
curl -X POST -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}' "http://localhost:8080/eshop-rest/products/create" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X PUT -H \"Content-Type: application/json\" --data '{\"value\":\"16.33\",\"currency\":\"CZK\"}' http://localhost:8080/eshop-rest/products/4 | jq"
curl -X PUT -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' "http://localhost:8080/eshop-rest/products/4" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X POST  -H \"Content-Type: application/json\" --data '5' http://localhost:8080/eshop-rest/products/2/categories | jq"
curl -X POST -H "Content-Type: application/json" --data '5' "http://localhost:8080/eshop-rest/products/2/categories" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/products/2/currentPrice | jq"
echo "This endpoint is not a part of the original project"
curl -X GET "http://localhost:8080/eshop-rest/products/2/currentPrice" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "curl -X GET http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR | jq"
echo "This endpoint is not a part of the original project"
curl -X GET "http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR" | jq
echo
read -rsn1 -p "Press any key to continue."

echo
echo "End of the script."
