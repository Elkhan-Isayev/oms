#!/bin/bash

# Set JVM memory limits for Replit environment
export JAVA_TOOL_OPTIONS="-Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m"

echo "Starting Direct Auth Service on port 5001..."
cd ms-auth
./gradlew clean bootJar -x test --no-daemon

# Directly run the JAR file
java -Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m \
  -jar build/libs/auth-0.0.1-SNAPSHOT.jar \
  --server.port=5001 --server.address=0.0.0.0