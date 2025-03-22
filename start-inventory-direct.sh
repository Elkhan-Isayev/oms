#!/bin/bash

# Set JVM memory limits for Replit environment
export JAVA_TOOL_OPTIONS="-Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m"

echo "Building Inventory Service..."
cd ms-inventory
./gradlew build -x test --no-daemon

echo "Starting Inventory Service on port 5003..."
# Run the JAR directly
java -Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m \
  -jar build/libs/inventory-0.0.1-SNAPSHOT.jar \
  --server.port=5003 --server.address=0.0.0.0