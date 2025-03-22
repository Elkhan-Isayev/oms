#!/bin/bash

# Set JVM memory limits for Replit environment
export JAVA_TOOL_OPTIONS="-Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m"

echo "Building Order Service..."
cd ms-order
# Clean previous build artifacts first
./gradlew clean --no-daemon
# Build with more memory for Gradle
GRADLE_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m" ./gradlew classes --no-daemon
GRADLE_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m" ./gradlew bootJar --no-daemon -x test

echo "Starting Order Service on port 5002..."
# Check if JAR exists
if [ -f "build/libs/order-0.0.1-SNAPSHOT.jar" ]; then
  # Run the JAR directly with proper memory settings
  java -Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m \
    -jar build/libs/order-0.0.1-SNAPSHOT.jar \
    --server.port=5002 --server.address=0.0.0.0
else
  echo "JAR file not found. Running with bootRun instead..."
  GRADLE_OPTS="-Xmx512m -XX:MaxMetaspaceSize=256m" ./gradlew bootRun \
    --no-daemon -x test \
    -Pargs="--server.port=5002 --server.address=0.0.0.0"
fi