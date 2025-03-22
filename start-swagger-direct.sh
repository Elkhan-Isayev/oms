#!/bin/bash

# Set JVM memory limits for Replit environment
export JAVA_TOOL_OPTIONS="-Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m"

echo "Building Swagger Documentation Service..."
cd ms-swagger
./gradlew build -x test --no-daemon

echo "Starting Swagger Documentation Service on port 5005..."
# Run the JAR directly
java -Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m \
  -jar build/libs/swagger-0.0.1-SNAPSHOT.jar \
  --server.port=5005 --server.address=0.0.0.0