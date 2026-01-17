# Energy & Water Management System for Irrigation Stations

## Project Description

This project implements a **distributed microservices architecture** for managing energy consumption and water resources in irrigation stations. The system provides real-time monitoring, intelligent pump control, and automated resource optimization through both **synchronous** and **asynchronous** communication patterns.

### Key Features

- **Energy Consumption Monitoring**: Track and analyze electrical consumption for irrigation pumps with overconsumption detection (threshold: 150 kWh)
- **Water Resource Management**: Monitor reservoir levels and water flow rates (débits mesurés)
- **Intelligent Pump Control**: Synchronous energy availability checks before pump activation
- **Real-time Alerts**: Asynchronous event-driven notifications for overconsumption scenarios
- **Dynamic Pump Status**: Automatic energy status updates (Normal/Overconsumption) via async callbacks
- **Centralized Gateway**: Single entry point for all frontend requests with CORS management

### Architecture Overview

The system follows a **microservices architecture** with:

- **Synchronous Communication**: RESTful HTTP calls between Water Service and Energy Service for real-time validation
- **Asynchronous Communication**: Event-driven messaging via RabbitMQ for overconsumption alerts and status updates
- **Service Discovery**: Eureka server for dynamic service registration and load balancing
- **Centralized Configuration**: Spring Cloud Config Server for externalized configuration management
- **API Gateway**: Spring Cloud Gateway for request routing, CORS handling, and security

```
┌─────────────────────────────────────────────────────────────┐
│                       Frontend (React)                       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                   API Gateway (Port 8080)                    │
└───────────────┬──────────────────────────┬──────────────────┘
                │                          │
       ┌────────↓────────┐        ┌───────↓────────┐
       │ Energy Service  │        │ Water Service  │
       │   (Port 8081)   │←──────→│  (Port 8082)   │
       │                 │  REST  │                │
       │  - Pompes       │        │  - Réservoirs  │
       │  - Consommations│        │  - Débits      │
       └────────┬────────┘        └───────┬────────┘
                │                         │
                │      ┌──────────────────┘
                │      │ Async Events
                └──────↓───────┐
                │   RabbitMQ   │
                │ (Port 5672)  │
                └──────────────┘
                       ↑
        ┌──────────────┼──────────────┐
        │              │              │
┌───────↓──────┐ ┌────↓─────┐ ┌──────↓──────┐
│Config Server │ │  Eureka  │ │   Database  │
│ (Port 8888)  │ │(Port 8761)│ │  (H2 Mem)   │
└──────────────┘ └──────────┘ └─────────────┘
```

---

## Technologies Used

### Backend Technologies

- **Java 17** - Programming language
- **Spring Boot 3.2.1** - Application framework
- **Spring Cloud 2023.0.0** - Microservices toolkit
  - Spring Cloud Gateway - API Gateway implementation
  - Spring Cloud Netflix Eureka - Service discovery
  - Spring Cloud Config - Centralized configuration
  - Spring Cloud LoadBalancer - Client-side load balancing
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - ORM framework
- **Maven 3.9+** - Dependency management and build tool

### Messaging & Integration

- **RabbitMQ 3.13** - Message broker for asynchronous communication
- **Spring AMQP** - RabbitMQ integration
- **RestTemplate** - Synchronous REST client with load balancing

### Data & Persistence

- **H2 Database** - In-memory relational database (development)
- **Lombok** - Boilerplate code reduction
- **Jakarta Validation** - Bean validation

### Frontend Technologies

- **React 18.x** - UI library
- **Axios** - HTTP client for API communication
- **CSS3** - Styling and responsive design

### DevOps & Containerization

- **Docker 24+** - Container platform
- **Docker Compose** - Multi-container orchestration

### Development Tools

- **Git** - Version control
- **Postman/cURL** - API testing
- **Docker Desktop** - Container management

---

## Installation Instructions

### Prerequisites

Ensure the following tools are installed on your system:

- **Java Development Kit (JDK) 17+**
- **Apache Maven 3.9+**
- **Node.js 18+ and npm**
- **Docker Desktop** (with Docker Compose)
- **Git**

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd microService
```

### Step 2: Build Backend Microservices

Build all Spring Boot services using Maven:

```bash
# Build Config Server
cd config-server
mvn clean package -DskipTests
cd ..

# Build Eureka Server
cd eureka-server
mvn clean package -DskipTests
cd ..

# Build API Gateway
cd api-gateway
mvn clean package -DskipTests
cd ..

# Build Energy Service
cd energy-service
mvn clean package -DskipTests
cd ..

# Build Water Service
cd water-service
mvn clean package -DskipTests
cd ..
```

### Step 3: Start Docker Containers

Launch all services using Docker Compose:

```bash
# From the root directory (microService/)
docker-compose up -d
```

This command will start 6 containers:
- `config-server` (Port 8888)
- `eureka-server` (Port 8761)
- `rabbitmq` (Ports 5672, 15672)
- `api-gateway` (Port 8080)
- `energy-service` (Port 8081)
- `water-service` (Port 8082)

### Step 4: Verify Services are Running

**Check container status:**
```bash
docker ps
```

**Access Eureka Dashboard:**
```
http://localhost:8761
```
You should see all services registered: ENERGY-SERVICE, WATER-SERVICE, API-GATEWAY

**Access RabbitMQ Management Console:**
```
http://localhost:15672
Username: guest
Password: guest
```

**Test API Gateway health:**
```bash
curl http://localhost:8080/actuator/health
```

### Step 5: Install and Run Frontend

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

The React application will open at:
```
http://localhost:3000
```

**Frontend Architecture:**
```
Frontend (React :3000)
    ↓
API Gateway (:8080)
    ↓
Eureka Service Discovery (:8761)
    ↓
├── Energy Service (:8081) → PostgreSQL
└── Water Service (:8082) → PostgreSQL
```

**Frontend API Connections:**
- **Energy Service (Pompes)**: `GET /api/energy/pompes`
- **Water Service (Réservoirs)**: `GET /api/water/reservoirs`

**Frontend Structure:**
```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── PumpsList.js          # Display electric pumps
│   │   ├── PumpsList.css
│   │   ├── ReservoirsList.js     # Display water reservoirs
│   │   ├── ReservoirsList.css
│   │   ├── EauSection.js         # Water section
│   │   ├── EnergieSection.js     # Energy section
│   │   ├── AlertesSection.js     # Alerts section
│   │   └── Sidebar.js            # Navigation sidebar
│   ├── config/
│   │   └── api.js                # API endpoints configuration
│   ├── services/
│   │   └── apiService.js         # HTTP service wrapper
│   ├── App.js                    # Main component
│   ├── App.css
│   ├── index.js
│   └── index.css
└── package.json
```

**Frontend Features:**
- **Real-time Data Display**: Shows pumps and reservoirs from microservices
- **Auto-refresh**: Manual refresh button to reload data
- **Responsive Design**: Works on desktop and mobile devices
- **Error Handling**: Graceful error messages with retry functionality
- **Visual Feedback**: Color-coded status for pumps and reservoir levels
- **Architecture Overview**: Visual representation of microservices topology

### Step 6: Verify Full Integration

**Test Energy Service through Gateway:**
```bash
curl http://localhost:8080/api/energy/pompes
```

**Test Water Service through Gateway:**
```bash
curl http://localhost:8080/api/water/reservoirs
```

---

## Usage

### Frontend Access

Navigate to `http://localhost:3000` to access the web interface.

**Available Sections:**

1. **Énergie** - Energy management
   - **Pompes Tab**: View all pumps with energy status indicators
     - Green "Normal" - Pump operating within energy limits
     - Red "Excessive" - Pump flagged for overconsumption
   - **Consommations Tab**: View energy consumption records
     - Status column shows Normal (<150 kWh) or Excessive (≥150 kWh)

2. **Eau** - Water management
   - **Réservoirs Tab**: Monitor water reservoir levels
   - **Débits Mesurés Tab**: Track water flow measurements

### API Endpoints (via Gateway)

All requests go through the API Gateway at `http://localhost:8080/api`

#### Energy Service Endpoints

**Pumps Management:**
```bash
# Get all pumps with energy status
GET /api/energy/pompes

# Create new pump
POST /api/energy/pompes
Content-Type: application/json
{
  "reference": "PUMP-001",
  "puissance": 75.0,
  "statut": "ACTIVE"
}

# Update pump
PUT /api/energy/pompes/{id}

# Delete pump
DELETE /api/energy/pompes/{id}
```

**Energy Consumption:**
```bash
# Get all consumption records
GET /api/energy/consommations

# Create consumption (triggers overconsumption check and async event if ≥100 kWh)
POST /api/energy/consommations
Content-Type: application/json
{
  "pompeId": 1,
  "energieUtilisee": 200.0,
  "duree": 5.0,
  "dateMesure": "2026-01-17T10:30:00"
}

# Check pump consumption status (used for sync validation)
GET /api/energy/consommations/check/pompe/{pompeId}
```

#### Water Service Endpoints

**Reservoirs:**
```bash
# Get all reservoirs
GET /api/water/reservoirs

# Create reservoir
POST /api/water/reservoirs
Content-Type: application/json
{
  "nom": "Reservoir A",
  "capaciteTotale": 50000.0,
  "volumeActuel": 35000.0,
  "localisation": "Zone North"
}
```

**Débits Mesurés (with validation):**
```bash
# Create water flow measurement
# Triggers TWO synchronous checks:
# 1. Check if pump has excessive consumption (≥150 kWh)
# 2. Check general energy availability
POST /api/water/debits
Content-Type: application/json
{
  "pompeId": 1,
  "debit": 150.0,
  "unite": "L/min",
  "dateMesure": "2026-01-17T11:00:00"
}

# Get all débits
GET /api/water/debits
```

### Communication Patterns

#### Synchronous Communication (Water → Energy)

**Scenario 1: Pump Consumption Check**
When creating a débit mesuré, Water Service calls Energy Service:
```
Water Service → GET /consommations/check/pompe/{pompeId} → Energy Service
Response: { pompeId: 1, restricted: true/false, message: "..." }
```

**Backend Logs:**
```
[Water Service] ⚡ Checking pompe #1 consumption status...
[Water Service] ⚡ SYNC CALL → Energy Service
[Energy Service] ⚡ SYNC CHECK: Pompe #1 has EXCESSIVE consumption
[Water Service] ⚡ SYNC RESPONSE ← Energy Service: restricted=true
[Water Service] ❌ Cannot start pump #1: Pump restricted
```

**Result:** Frontend receives error alert if pump restricted

#### Asynchronous Communication (Energy → Water)

**Scenario 2: Overconsumption Event**
When energy consumption exceeds 100 kWh, Energy Service publishes event:

```
Energy Service → RabbitMQ → Water Service → Energy Service (callback)
```

**Backend Logs:**
```
[Energy Service] 📤 PUBLISHING OVERCONSUMPTION EVENT to RabbitMQ
[Energy Service] ⚡ Pompe #1: 200.0 kWh (threshold: 100.0 kWh)

[Water Service] 📥 OVERCONSUMPTION EVENT RECEIVED
[Water Service] ⚡ Pompe ID: 1
[Water Service] ⚡ Énergie Utilisée: 200.0 kWh
[Water Service] 🔧 WATER SERVICE ADAPTIVE REACTION
[Water Service] → Analyzing 3 reservoirs for priority irrigation
[Water Service] → Alert sent to operators

[Water Service] Calling Energy Service: PUT /pompes/1/energy-status?status=Overconsumption
[Energy Service] Updated Pompe #1 energyStatus to: Overconsumption
```

**Result:** Pump's `energyStatus` field updates to "Overconsumption" (displayed in red in frontend)

### Testing the Complete Flow

**Test 1: Normal Operation**
1. Create a pump with `puissance: 50.0`
2. Create consumption with `energieUtilisee: 50.0` (below threshold)
3. Create débit mesuré for that pump → ✅ Success
4. Check pump list → energyStatus = "Normal" (green)

**Test 2: Overconsumption Scenario**
1. Create consumption with `energieUtilisee: 200.0` (above 150 kWh threshold)
2. Async event triggers → Water Service receives alert
3. Water Service updates pump status via REST callback
4. Check pump list → energyStatus = "Overconsumption" (red)
5. Try to create débit mesuré for that pump → ❌ Blocked with alert

**Create Test Data:**

When you first run the frontend, the lists will be empty. To test with data:

```bash
# Create a pump
curl -X POST http://localhost:8080/api/energy/pompes -H "Content-Type: application/json" -d "{\"reference\":\"PUMP-001\",\"puissance\":50.0,\"statut\":\"ACTIF\",\"dateMiseEnService\":\"2024-01-01\"}"

# Create a reservoir
curl -X POST http://localhost:8080/api/water/reservoirs -H "Content-Type: application/json" -d "{\"nom\":\"Réservoir Nord\",\"capacite\":10000,\"niveauActuel\":7500,\"localisation\":\"Zone A\"}"
```

---

## Additional Notes

### Architecture Principles

This project demonstrates several key architectural principles:

1. **Microservices Independence**: Each service (Energy, Water) has its own database and can be deployed independently
2. **Service Discovery**: Dynamic service registration eliminates hardcoded URLs
3. **API Gateway Pattern**: Single entry point simplifies client interaction and enables centralized CORS/security
4. **Event-Driven Architecture**: Asynchronous messaging decouples services and enables reactive workflows
5. **Circuit Breaker Preparedness**: RestTemplate with LoadBalancer provides foundation for resilience patterns

### Communication Patterns Explained

**Synchronous (REST)**
- **Use Case**: Real-time validation requiring immediate response
- **Example**: Water Service must know NOW if pump can start
- **Trade-off**: Coupling between services, potential cascading failures
- **Implementation**: RestTemplate with Eureka service discovery

**Asynchronous (RabbitMQ)**
- **Use Case**: Event notifications that don't require immediate response
- **Example**: Alert about overconsumption triggers optimization workflow
- **Trade-off**: Eventual consistency, message delivery guarantees needed
- **Implementation**: Spring AMQP with durable queues and exchanges

### Testing Notes

**Unit Testing:**
```bash
# Run tests for specific service
cd energy-service
mvn test
```

**Integration Testing:**
- Use Postman collection (import from `/docs/postman_collection.json`)
- Test service-to-service communication by monitoring logs
- Verify RabbitMQ message flow in Management Console

**Monitoring Logs:**
```bash
# View real-time logs
docker logs -f energy-service
docker logs -f water-service

# Search for specific events
docker logs water-service | grep "SYNC CALL"
docker logs energy-service | grep "OVERCONSUMPTION"
```

### Current Limitations & Improvements

**Current State:**
- In-memory H2 database (data lost on container restart)
- No authentication/authorization
- Basic error handling
- Single instance per service

**Suggested Improvements:**

1. **Persistence:**
   - Migrate to PostgreSQL or MySQL with Docker volumes
   - Add Flyway/Liquibase for database migrations

2. **Security:**
   - Implement Spring Security with JWT authentication
   - Add OAuth2 for service-to-service communication
   - Enable HTTPS with SSL certificates

3. **Resilience:**
   - Add Resilience4j for circuit breakers and retry logic
   - Implement bulkheads and rate limiting
   - Add timeout configurations

4. **Observability:**
   - Integrate Spring Boot Actuator metrics
   - Add distributed tracing (Zipkin/Jaeger)
   - Implement centralized logging (ELK stack)

5. **Scalability:**
   - Enable horizontal scaling with multiple service instances
   - Add Redis for distributed caching
   - Implement message partitioning in RabbitMQ

6. **Frontend Enhancements:**
   - Add real-time updates via WebSockets
   - Implement data visualization (charts for consumption trends)
   - Add user roles and permissions
   - Optional Docker containerization for production deployment

7. **Production Deployment:**
   - Frontend can be containerized with Dockerfile:
     ```dockerfile
     FROM node:18-alpine AS build
     WORKDIR /app
     COPY package.json .
     RUN npm install
     COPY . .
     RUN npm run build

     FROM nginx:alpine
     COPY --from=build /app/build /usr/share/nginx/html
     EXPOSE 80
     CMD ["nginx", "-g", "daemon off;"]
     ```

### Academic Context

This project serves as a practical implementation of concepts taught in distributed systems and microservices architecture courses:

- **Service-Oriented Architecture (SOA)**: Decomposition of monolithic applications into bounded contexts
- **RESTful API Design**: Adherence to HTTP semantics and resource-based modeling
- **Message-Driven Architecture**: Publish-subscribe pattern for loose coupling
- **Domain-Driven Design (DDD)**: Clear separation between Energy and Water domains
- **Clean Architecture**: Separation of concerns (Controller → Service → Repository)
- **12-Factor App Principles**: Externalized configuration, stateless processes, port binding

### Development Approach

The project was developed incrementally:

1. ✅ **Step 1-4**: Config Server, Eureka, and basic microservices setup
2. ✅ **Step 5-7**: Entity modeling, repositories, and services
3. ✅ **Step 8**: API Gateway with CORS configuration
4. ✅ **Step 9**: Docker containerization and compose orchestration
5. ✅ **Step 10**: RabbitMQ integration and async communication
6. ✅ **Step 11**: React frontend with API integration
7. ✅ **Step 12**: Synchronous communication for real-time validation
8. ✅ **Step 13**: Energy status tracking with async callbacks

---

## Project Structure

```
microService/
├── config-server/          # Centralized configuration service
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── eureka-server/          # Service discovery server
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── api-gateway/            # API Gateway (Spring Cloud Gateway)
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── energy-service/         # Energy management microservice
│   ├── src/
│   │   ├── controller/     # REST endpoints
│   │   ├── service/        # Business logic
│   │   ├── entity/         # JPA entities
│   │   ├── dto/            # Data transfer objects
│   │   ├── repository/     # Data access layer
│   │   ├── event/          # RabbitMQ events
│   │   └── publisher/      # Event publishers
│   ├── pom.xml
│   └── Dockerfile
├── water-service/          # Water management microservice
│   ├── src/
│   │   ├── controller/     # REST endpoints
│   │   ├── service/        # Business logic
│   │   ├── entity/         # JPA entities
│   │   ├── dto/            # Data transfer objects
│   │   ├── repository/     # Data access layer
│   │   ├── client/         # REST clients for sync calls
│   │   ├── listener/       # RabbitMQ consumers
│   │   └── config/         # RabbitMQ configuration
│   ├── pom.xml
│   └── Dockerfile
├── frontend/               # React web application
│   ├── public/
│   ├── src/
│   │   ├── components/     # React components
│   │   ├── services/       # API service layer
│   │   └── App.js
│   ├── package.json
│   └── .gitignore
├── config-repo/            # External configuration repository
│   ├── application.yml     # Global configuration
│   ├── eureka-server/
│   ├── api-gateway/
│   ├── energy-service/
│   └── water-service/
├── docker-compose.yml      # Multi-container orchestration
├── .gitignore
└── README.md
```

---

## License

This project is developed for educational purposes as part of a microservices architecture course.

---

## Contributors

- **Development Team**: [Mohamed fenina]

