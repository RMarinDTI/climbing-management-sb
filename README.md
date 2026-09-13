# Climbing Management API

A backend application built with **Java 21 and Spring Boot** to manage climbing courses.

The project is designed as a practical **Senior Backend Java** learning and portfolio project, demonstrating modern enterprise backend development, REST APIs, persistence, transaction management, concurrency control, relational and NoSQL databases, dynamic queries, aggregation, containerization, CI/CD, Kubernetes orchestration, security practices, and clean layered architecture.

The application is being developed incrementally, introducing technologies and architectural patterns commonly used in enterprise Java applications.

---

# 🚀 Tech Stack

## Backend

- Java 21
- Spring Boot 4.1
- Spring Web
- Spring Data JPA
- Spring Data MongoDB
- Spring Boot Actuator
- Hibernate
- Jakarta Bean Validation
- SLF4J

## Databases

- PostgreSQL 17
- MongoDB 7

## Infrastructure

- Docker
- Docker Compose
- Kubernetes
- Helm

## CI/CD

- GitHub Actions
- GitHub Container Registry (GHCR)

## Development Tools

- Maven 3.9+
- Git / GitHub
- IntelliJ IDEA
- Postman
- Docker Desktop
- kubectl
- Helm
- Trivy

---

# 🏗️ Current Technical Picture

```text
Client
  │
  ▼
Spring Boot REST API
  │
  ▼
Service Layer
  │
  ├───────────────────┐
  ▼                   ▼
PostgreSQL           MongoDB
JPA / Hibernate      Spring Data MongoDB

Application
  │
  ▼
Docker Image
  │
  ▼
GHCR
  │
  ▼
Kubernetes
  │
  ▼
Helm-managed deployment
```

The current learning phase is **Microservices Architecture**.

---

# 📚 Detailed Documentation

Detailed learning material is split by technology so examples are not duplicated between files.

| Topic | Documentation |
|---|---|
| Spring Boot, REST, validation, exceptions | [SPRING_BOOT_REST.md](docs/SPRING_BOOT_REST.md) |
| PostgreSQL, JPA, transactions, locking | [POSTGRESQL_JPA.md](docs/POSTGRESQL_JPA.md) |
| MongoDB | [MONGODB.md](docs/MONGODB.md) |
| Docker | [DOCKER.md](docs/DOCKER.md) |
| CI/CD | [CICD.md](docs/CICD.md) |
| Kubernetes | [KUBERNETES.md](docs/KUBERNETES.md) |
| Helm | [HELM.md](docs/HELM.md) |
| Course/project progress | [ROADMAP.md](docs/ROADMAP.md) |
| How to maintain these docs | [MAINTENANCE.md](docs/MAINTENANCE.md) |

---

# 🗺️ Current Position

```text
Java / Spring Boot          ✅
        ↓
PostgreSQL / JPA            ✅
        ↓
Transactions / Locking      ✅
        ↓
MongoDB                     ✅
        ↓
Docker                      ✅
        ↓
CI/CD                       ✅
        ↓
Kubernetes                  ✅
        ↓
Namespaces                  ✅
        ↓
Helm                        ✅
        ↓
Microservices               🚧 CURRENT
```

For detailed progress, use **only** [ROADMAP.md](docs/ROADMAP.md).

---

# 🎯 Learning Approach

Each major topic is learned using the same practical loop:

```text
Theory
   ↓
Implementation
   ↓
Inspection
   ↓
API / Behaviour Testing
   ↓
Troubleshooting
   ↓
Interview-Level Understanding
   ↓
Code Cleanup
   ↓
Git Commit
   ↓
CI/CD Validation
```

The detailed examples for each step live in the corresponding topic document rather than in this README.

---

# 🎯 Project Goal

The goal is not only to create a working API.

The project is intended to demonstrate and reinforce the skills expected from a **Senior Backend Java Developer**, including:

- REST API design
- Layered architecture
- Persistence
- Transaction management
- Concurrency control
- Relational and NoSQL databases
- Containerization
- CI/CD
- Kubernetes
- Helm
- Distributed systems
- Microservices
- Event-driven architecture
- Security
- Testing
- System design

---

# 👨‍💻 Author

**Rubén Marín**

Backend Java Developer

Technologies explored in this project include:

`Java` · `Spring Boot` · `Spring Data JPA` · `Hibernate` · `PostgreSQL` · `Spring Data MongoDB` · `MongoDB` · `MongoTemplate` · `Docker` · `Docker Compose` · `GitHub Actions` · `GitHub Container Registry` · `Kubernetes` · `Helm`
