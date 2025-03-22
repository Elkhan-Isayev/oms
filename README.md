# Order Management System (OMS) - Microservices Architecture

A scalable Java-based microservices architecture for Order Management System (OMS) that provides comprehensive capabilities for order processing, inventory management, and authentication across multiple services.

## Architecture Overview

The OMS is built using a microservices architecture pattern with the following key components:

1. **API Gateway Service** (Port 5000)
   - Routes external requests to appropriate microservices
   - Handles authentication and authorization
   - Provides a unified entry point for all clients

2. **Authentication Service** (Port 5001)
   - Manages user authentication
   - Handles JWT token generation and validation
   - Stores user credentials and roles

3. **Order Service** (Port 5002)
   - Processes customer orders
   - Manages order lifecycle and status updates
   - Communicates with Inventory Service to check product availability

4. **Inventory Service** (Port 5003)
   - Manages product inventory
   - Handles stock updates and availability checks
   - Provides inventory reports

5. **Swagger Documentation Service** (Port 5006)
   - Centralizes API documentation
   - Provides interactive API testing interface

## Technology Stack

- **Framework**: Spring Boot, Spring Cloud Gateway, Spring Security
- **Database**: PostgreSQL with Liquibase for schema management
- **Authentication**: JWT (JSON Web Tokens)
- **API Documentation**: Swagger/OpenAPI
- **Build Tool**: Gradle

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL database
- Docker (optional for containerization)

### Running the Services

Use the provided scripts to start each service:

```bash
# Start all services including Gateway
./start-all-services.sh

# Start individual services
./start-gateway.sh
./start-auth.sh
./start-order.sh
./start-inventory.sh
./start-swagger.sh

# Start all microservices without Gateway
./start-services-without-gateway.sh
```

## API Endpoints

### Public Endpoints (No Authentication Required)

- `GET /api/public/health` - Health check endpoint
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate and receive a JWT token

### Protected Endpoints (Authentication Required)

- `GET /api/orders` - List all orders for the authenticated user
- `POST /api/orders` - Create a new order
- `GET /api/inventory/products` - List available products
- `GET /api/inventory/products/{id}` - Get specific product details

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For questions or support, please contact the project maintainer.