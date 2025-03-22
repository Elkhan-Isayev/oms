#!/bin/bash

echo "Starting microservices without Gateway..."

# Start Auth Service
cd ms-auth
./gradlew clean build -x test
java -jar build/libs/auth-0.0.1-SNAPSHOT.jar &
auth_pid=$!
cd ..

# Start Order Service
cd ms-order
./gradlew clean build -x test
java -jar build/libs/order-0.0.1-SNAPSHOT.jar &
order_pid=$!
cd ..

# Start Inventory Service
cd ms-inventory
./gradlew clean build -x test
java -jar build/libs/inventory-0.0.1-SNAPSHOT.jar &
inventory_pid=$!
cd ..

# Start Swagger Service
cd ms-swagger
./gradlew clean build -x test
java -jar build/libs/swagger-0.0.1-SNAPSHOT.jar &
swagger_pid=$!
cd ..

echo "All services started!"
echo "Auth service PID: $auth_pid"
echo "Order service PID: $order_pid"
echo "Inventory service PID: $inventory_pid"
echo "Swagger service PID: $swagger_pid"

# Wait for all services
wait