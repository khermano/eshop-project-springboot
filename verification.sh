#!/bin/bash

echo
echo "This is verification script which executes example calls of endpoints provided by eshop-project-springboot application and shows their output"
echo "Please keep in mind that application must be up and running and the script should be used once per application run to work properly"
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/users"
curl -i -X GET http://localhost:8080/eshop-rest/users
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/users/1"
curl -i -X GET http://localhost:8080/eshop-rest/users/1
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/categories"
curl -i -X GET http://localhost:8080/eshop-rest/categories
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/categories/1"
curl -i -X GET http://localhost:8080/eshop-rest/categories/1
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL"
curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE"
curl -i -X GET http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/orders/by_user_id/1"
curl -i -X GET http://localhost:8080/eshop-rest/orders/by_user_id/1
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/orders/1"
curl -i -X GET http://localhost:8080/eshop-rest/orders/1
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X POST http://localhost:8080/eshop-rest/orders/2?action=FINISH"
curl -i -X POST http://localhost:8080/eshop-rest/orders/2?action=FINISH
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/products"
curl -i -X GET http://localhost:8080/eshop-rest/products
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/products/1"
curl -i -X GET http://localhost:8080/eshop-rest/products/1
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X DELETE http://localhost:8080/eshop-rest/products/1"
curl -i -X DELETE http://localhost:8080/eshop-rest/products/1
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -X POST -i -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}' http://localhost:8080/eshop-rest/products/create"
curl -X POST -i -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}' http://localhost:8080/eshop-rest/products/create
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -X PUT -i -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' http://localhost:8080/eshop-rest/products/4"
curl -X PUT -i -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' http://localhost:8080/eshop-rest/products/4
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -X POST -i -H "Content-Type: application/json" --data '5' http://localhost:8080/eshop-rest/products/2/categories"
curl -X POST -i -H "Content-Type: application/json" --data '5' http://localhost:8080/eshop-rest/products/2/categories
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/products/2/currentPrice"
echo "This endpoint is not a part of the original project"
curl -i -X GET http://localhost:8080/eshop-rest/products/2/currentPrice
echo
read -rsn1 -p "Press any key to continue"

echo
echo "curl -i -X GET http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR"
echo "This endpoint is not a part of the original project"
curl -i -X GET http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR
echo
read -rsn1 -p "Press any key to continue"

echo
echo "End of the script"
