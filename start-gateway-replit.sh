#!/bin/bash

# Set Replit-specific JVM and server options
export JAVA_TOOL_OPTIONS="-Xmx512m -XX:+UseSerialGC -XX:MaxRAM=512m"
export SPRING_PROFILES_ACTIVE="replit"

echo "Starting Gateway Service for Replit on port 5000..."
cd gw-oms || exit 1

# Make routing explicitly use localhost addresses for services as a fallback
# This ensures all service calls use the correct networking setup
cat > src/main/resources/application-replit.properties << EOF
# Replit-specific configurations
server.address=0.0.0.0
server.port=5000
spring.main.web-application-type=reactive

# Service URLs - using localhost for internal routing
service.auth.url=http://localhost:5001
service.order.url=http://localhost:5002
service.inventory.url=http://localhost:5003
service.swagger.url=http://localhost:5005

# Reactive server configuration for Netty
spring.netty.leak-detection=advanced
EOF

# Start the service with specific options for Replit environment
./gradlew bootRun