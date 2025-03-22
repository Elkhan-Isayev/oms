#!/bin/bash

# Set JVM memory limits for Replit environment
export JAVA_TOOL_OPTIONS="-Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m"
export GRADLE_OPTS="-Dorg.gradle.daemon=false"

echo "Starting Auth Service on port 5001..."
cd ms-auth
./gradlew bootRun -Dserver.port=5001 --no-daemon