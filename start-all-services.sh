#!/bin/bash

# Start all services in parallel using background processes

# Start Gateway Service - We'll use lsof to check for port use
echo "Starting OMS Gateway Service..."
./start-gateway.sh &
GATEWAY_PID=$!

echo "Starting OMS Auth Service..."
./start-auth.sh &
AUTH_PID=$!

echo "Starting OMS Order Service..."
./start-order.sh &
ORDER_PID=$!

echo "Starting OMS Inventory Service..."
./start-inventory.sh &
INVENTORY_PID=$!

echo "Starting OMS Swagger Service..."
./start-swagger.sh &
SWAGGER_PID=$!

echo "All services started in background."
if [ -n "$GATEWAY_PID" ]; then
  echo "Gateway PID: $GATEWAY_PID"
fi
echo "Auth PID: $AUTH_PID"
echo "Order PID: $ORDER_PID"
echo "Inventory PID: $INVENTORY_PID"
echo "Swagger PID: $SWAGGER_PID"

# Wait for all processes
wait