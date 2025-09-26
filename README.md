# Professional Networking System

A comprehensive microservices-based professional networking platform built with Spring Boot, featuring real-time messaging, distributed tracing, and cloud-native deployment capabilities.

## 🏗️ Architecture Overview

This system follows a microservices architecture pattern with the following core components:

### Core Services
- **API Gateway** (Port 9000) - Entry point for all client requests with JWT authentication
- **User Service** (Port 9020) - User management and authentication
- **Posts Service** (Port 9010) - Content creation and management
- **Connection Service** (Port 9030) - Professional networking and relationships
- **Notification Service** - Real-time notifications and messaging

### Infrastructure Components
- **Discovery Server** (Eureka) - Service registration and discovery
- **Distributed Tracing** (Zipkin) - Request tracing across services
- **Centralized Logging** (ELK Stack) - Elasticsearch, Logstash, Kibana
- **Message Broker** (Kafka) - Asynchronous communication
- **Caching Layer** (Redis) - Performance optimization
- **Databases**:
    - PostgreSQL for Users, Posts, and Notifications
    - Neo4j for Connection relationships

## 🚀 Features

- **User Management**: Registration, authentication, profile management
- **Professional Networking**: Connect with professionals, manage relationships
- **Content Sharing**: Create, share, and interact with posts
- **Real-time Notifications**: Instant updates for connections and activities
- **Distributed Architecture**: Scalable microservices design
- **Cloud-Native**: Kubernetes-ready deployment
- **Observability**: Comprehensive logging, monitoring, and tracing

## 🛠️ Technology Stack

### Backend
- **Java 17** with **Spring Boot 3.5.5**
- **Spring Cloud Gateway** for API routing
- **Spring Data JPA** for relational data
- **Spring Data Neo4j** for graph relationships
- **Spring Security** with JWT authentication
- **Spring Cloud Eureka** for service discovery

### Databases
- **PostgreSQL** - User data, posts, notifications
- **Neo4j** - Connection relationships and graph queries
- **Redis** - Caching and session management

### Infrastructure
- **Apache Kafka** - Event streaming and messaging
- **Zipkin** - Distributed tracing
- **ELK Stack** - Centralized logging
- **Docker** - Containerization
- **Kubernetes** - Container orchestration
- **Google Kubernetes Engine (GKE)** - Cloud deployment

## 📋 Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven 3.8+
- kubectl (for Kubernetes deployment)

## 🚀 Quick Start

### Local Development with Docker Compose

1. **Clone the repository**
```bash
  git clone <repository-url>
cd Professional-Networking-System
```

2. **Start all services**
```bash
  docker-compose up -d
```

3. **Access the application**
- API Gateway: http://localhost:9000
- Kibana (Logging): http://localhost:5601
- Kafka UI: http://localhost:8080

### Kubernetes Deployment

1. **Deploy to Kubernetes**
```bash
  cd k8s
./deploy-all.sh
```

2. **Check deployment status**
```bash
  kubectl get pods
kubectl get services
```

## 🔧 Service Configuration

### Environment Variables

Each service can be configured using environment variables:

```bash
  # Database Configuration
DB_SERVICE=<database-host>
DB_NAME=<database-name>
DB_USER=<username>
DB_PASSWORD=<password>

# Neo4j Configuration (Connection Service)
NEO4J_URI=neo4j://connections-db:7687
NEO4J_USERNAME=neo4j
NEO4J_PASSWORD=password
```

### Service Profiles

- **Default Profile**: Local development with Docker Compose
- **k8s Profile**: Kubernetes deployment with service discovery disabled

## 📊 Monitoring & Observability

### Distributed Tracing
- **Zipkin**: http://localhost:9411
- Traces requests across all microservices
- 100% sampling rate for development

### Centralized Logging
- **Kibana**: http://localhost:5601
- **Elasticsearch**: Stores all application logs
- **Filebeat**: Log collection and forwarding

### Health Checks
All services expose health endpoints:
```
GET /{service-context}/actuator/health
```

## 🔄 CI/CD Pipeline

### GitHub Actions Workflow

The project includes automated CI/CD pipeline (`.github/workflows/build_and_deploy.yml`):

1. **Build Stage**:
    - Builds all microservices using Maven
    - Creates Docker images
    - Pushes to Docker Hub

2. **Deploy Stage**:
    - Deploys to Google Kubernetes Engine
    - Applies Kubernetes manifests
    - Updates running services

### Deployment Triggers
- **Push to `develop` branch**: Automatic deployment
- **Manual trigger**: Via GitHub Actions UI

## 🗄️ Database Schema

### Neo4j Relationships (Connection Service)
```cypher
// Create user nodes
CREATE (u:User {id: 'user123', name: 'John Doe'})

// Create connections
MATCH (a:User {id: 'user1'}), (b:User {id: 'user2'})
CREATE (a)-[r:CONNECTED_TO {since: date()}]->(b)

// Find 2nd degree connections
MATCH (a:User)-[:CONNECTED_TO]->(b:User)-[:CONNECTED_TO]->(c:User)
WHERE a.id = 'user123'
RETURN c
```

## 🔐 Security

- **JWT Authentication**: Stateless authentication across services
- **Redis Session Management**: Distributed session storage
- **Service-to-Service Communication**: Internal network isolation
- **Input Validation**: Comprehensive request validation

## 📈 Performance Optimization

- **Redis Caching**: User sessions and frequently accessed data
- **Connection Pooling**: Optimized database connections
- **Async Processing**: Kafka-based event handling
- **Load Balancing**: Kubernetes service load balancing

## 🐳 Docker Images

All services are available as Docker images:
- `abhaykumar255/professional-system_api-gateway:latest`
- `abhaykumar255/professional-system_user-service:latest`
- `abhaykumar255/professional-system_posts-service:latest`
- `abhaykumar255/professional-system_connection-service:latest`
- `abhaykumar255/professional-system_notification-service:latest`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions, please open an issue in the GitHub repository.