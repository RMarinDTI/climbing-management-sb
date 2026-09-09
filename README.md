# Climbing Management API

A backend application built with **Java 21 and Spring Boot** to manage climbing courses.

The project is designed as a practical **Senior Backend Java** learning and portfolio project, demonstrating modern enterprise backend development, REST APIs, persistence, transaction management, concurrency control, relational and NoSQL databases, dynamic queries, aggregation, containerization, security practices, and clean layered architecture.

The application is being developed incrementally, introducing technologies and architectural patterns commonly used in enterprise Java applications.

---

## 🚀 Tech Stack

### Backend

* **Java 21**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Spring Data MongoDB**
* **Spring Boot Actuator**
* **Hibernate**
* **Jakarta Bean Validation**
* **SLF4J / Logging**

### Databases

* **PostgreSQL 17**
* **MongoDB 7**

### Development Tools

* **Maven 3.9+**
* **Git / GitHub**
* **IntelliJ IDEA**
* **Postman**

### Infrastructure

* **Docker**
* **Docker Compose**
* **Docker Secrets**
* **Docker Healthchecks**
* **Docker multi-stage builds**
* **Trivy vulnerability scanning**

### Planned

* GitHub Actions / CI/CD
* Kubernetes
* Advanced testing / Testcontainers
* Spring Security
* Messaging
* Microservices
* System design

---

# 🏗️ Architecture

The application follows a layered architecture:

```text
                    REST API
                       │
                       ▼
                  Controller
                       │
                       ▼
                    Service
                       │
              ┌────────┴────────┐
              ▼                 ▼
          Repository       Custom Repository
              │                 │
              ▼                 ▼
         Spring Data        MongoTemplate
              │                 │
       ┌──────┴──────┐          │
       ▼             ▼          ▼
  PostgreSQL      MongoDB    MongoDB
```

### Main layers

**Controller**

Exposes the REST API and handles HTTP requests and responses.

**Service**

Contains business logic and transaction boundaries.

**Repository**

Provides data access through Spring Data repositories.

**Custom Repository**

Used when standard repository methods are not sufficient, for example dynamic MongoDB queries and aggregation pipelines.

**Entity / Document**

Represents the persistence model for PostgreSQL and MongoDB.

**DTO / Record**

Defines the API contract independently from the persistence model.

---

# 📚 Current Features

## 🌐 REST API

The project provides course management operations using REST.

### PostgreSQL / JPA

```text
GET     /jpa/courses
GET     /jpa/courses/{id}
POST    /jpa/courses
PUT     /jpa/courses/{id}
DELETE  /jpa/courses/{id}
```

Additional query endpoints:

```text
GET /jpa/courses/most-expensive
GET /jpa/courses/difficulty/{difficulty}
GET /jpa/courses/difficulty/{difficulty}/price/{price}
```

---

# 🗄️ Spring Data JPA

The PostgreSQL persistence layer uses:

```text
Spring Data JPA
       ↓
Hibernate
       ↓
JDBC
       ↓
PostgreSQL
```

### Derived Queries

Queries can be generated automatically from repository method names.

Example:

```java
List<CourseEntity> findByDifficulty(Difficulty difficulty);
```

### JPQL

Custom queries can be written using the entity model:

```java
@Query("""
    SELECT c
    FROM CourseEntity c
    WHERE c.difficulty = :difficulty
    AND c.price < :price
    """)
List<CourseEntity> searchCourses(
        @Param("difficulty") Difficulty difficulty,
        @Param("price") Double price
);
```

---

# 🔄 Transactions

The project demonstrates Spring transaction management using:

* `@Transactional`
* Transaction propagation
* `REQUIRED`
* `REQUIRES_NEW`
* Transaction rollback
* Checked vs unchecked exceptions
* `rollbackFor`

Example:

```java
@Transactional
public void updateCourse(...) {
    ...
}
```

The project demonstrates how transaction propagation affects independent operations and rollback behavior.

---

# 🧠 Hibernate Dirty Checking

The project demonstrates Hibernate's Persistence Context and dirty checking.

An existing managed entity can be modified without explicitly calling `save()`:

```java
existing.setPrice(courseRecord.price());
```

Hibernate detects the change and generates the required `UPDATE` statement when the transaction commits.

---

# 🔐 Concurrency Control

Both major JPA locking strategies are demonstrated.

## Optimistic Locking

Implemented using:

```java
@Version
private Long version;
```

Hibernate uses the version field to detect concurrent modifications.

Conceptually:

```sql
UPDATE courses
SET ...
    version = ?
WHERE id = ?
AND version = ?
```

If another transaction has already modified the entity, the version no longer matches and Hibernate detects the conflict.

Optimistic locking is useful when concurrent conflicts are relatively uncommon and we don't want to hold database locks while processing requests.

---

## Pessimistic Locking

Implemented using:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

This requests a database-level lock when selecting the entity.

With PostgreSQL, Hibernate generated SQL similar to:

```sql
SELECT ...
FROM courses
WHERE id = ?
FOR NO KEY UPDATE;
```

The exact SQL depends on the database and Hibernate dialect.

Pessimistic locking is useful when concurrent modifications must be serialized at the database level.

---

# ⚠️ Global Exception Handling

The API uses `@RestControllerAdvice` to centralize exception handling.

Current handlers include:

```text
MethodArgumentNotValidException
        ↓
HTTP 400

CourseNotFoundException
        ↓
HTTP 404

Unexpected Exception
        ↓
HTTP 500
```

Unexpected exceptions are logged internally using SLF4J while exposing only a safe generic message to the client.

Example:

```json
{
    "timestamp": "2026-09-04T10:52:28.462609Z",
    "status": 500,
    "message": "An unexpected error occurred"
}
```

This prevents internal implementation details and stack traces from being exposed through the API.

---

# 🧪 Bean Validation

The API uses Jakarta Bean Validation.

Example:

```java
@NotBlank
private String name;

@NotNull
@Positive
private Double price;

@NotNull
private Difficulty difficulty;
```

Invalid requests are converted into structured `400 BAD_REQUEST` responses.

Example:

```json
{
    "timestamp": "...",
    "status": 400,
    "message": "Validation failed",
    "errors": {
        "name": "must not be blank",
        "price": "must be greater than 0"
    }
}
```

---

# 🍃 MongoDB

MongoDB was introduced as a second persistence technology alongside PostgreSQL.

The MongoDB layer uses:

```text
Spring Data MongoDB
        ↓
MongoRepository / MongoTemplate
        ↓
MongoDB
```

Database:

```text
climbing_management
```

Collections:

```text
courses
enrollments
```

---

# 📦 MongoDB Documents

Courses are represented using a MongoDB document:

```java
@Document(collection = "courses")
public class CourseMongoDocument {
    ...
}
```

The MongoDB model is intentionally separate from the JPA entity.

This demonstrates the difference between:

```text
Relational persistence
        ↓
@Entity
```

and:

```text
MongoDB persistence
        ↓
@Document
```

---

# 🔎 MongoRepository

The project uses `MongoRepository` for standard CRUD operations.

Examples include:

```java
save(...)
findById(...)
findAll(...)
deleteById(...)
existsById(...)
count()
```

Spring Data provides the implementation automatically.

---

# 🔍 MongoDB Queries

The project demonstrates several querying approaches.

## Derived Queries

Example:

```java
List<CourseMongoDocument> findByDifficulty(
        Difficulty difficulty
);
```

More complex example:

```java
List<CourseMongoDocument> findByDifficultyAndPriceLessThan(
        Difficulty difficulty,
        Double price
);
```

---

## Custom `@Query`

Example:

```java
@Query("{ 'difficulty': ?0, 'price': { $lt: ?1 } }")
List<CourseMongoDocument> findCoursesByDifficultyAndMaxPriceQuery(
        Difficulty difficulty,
        Double price
);
```

MongoDB operators demonstrated include:

```text
$gt
$gte
$lt
$lte
$eq
$ne
$in
$nin
$regex
```

---

# 🔎 Text Search

The project also demonstrates derived text queries:

```java
List<CourseMongoDocument> findByNameContainingIgnoreCase(
        String name
);
```

This provides a simple case-insensitive substring search.

---

# 📄 Pagination and Sorting

MongoDB queries support Spring's `Pageable` abstraction.

Example:

```java
Page<CourseMongoDocument> findByDifficultyAndPriceLessThanEqual(
        Difficulty difficulty,
        Double price,
        Pageable pageable
);
```

Example request:

```text
GET /mongo/courses/search?difficulty=MEDIUM&maxPrice=160&page=1&size=2&sort=price,asc
```

The API returns a `Page` containing:

* Current page
* Page size
* Total elements
* Total pages
* Content

---

# 🔄 DTOs

The MongoDB API uses separate DTOs for requests and responses.

### Request DTO

```java
public class CourseMongoRequestDto {
    ...
}
```

Used for:

```text
POST
PUT
```

### Response DTO

```java
public class CourseMongoResponseDto {
    ...
}
```

Used to expose API responses without exposing the persistence model directly.

This keeps the API contract independent from the MongoDB document structure.

---

# 🧩 MongoTemplate

For more complex MongoDB operations, the project uses `MongoTemplate`.

`MongoRepository` is ideal for standard CRUD and well-defined repository queries.

`MongoTemplate` is useful when queries need to be built dynamically.

The project implements a custom repository:

```text
CourseMongoCustomRepository
        ↓
CourseMongoCustomRepositoryImpl
        ↓
MongoTemplate
```

---

# 🔎 Dynamic MongoDB Queries

The project implements a dynamic search where all filters are optional.

Available filters:

```text
name
difficulty
minPrice
maxPrice
```

Example:

```text
GET /mongo/courses/dynamic-search
```

```text
GET /mongo/courses/dynamic-search?difficulty=MEDIUM
```

```text
GET /mongo/courses/dynamic-search?minPrice=100&maxPrice=160
```

```text
GET /mongo/courses/dynamic-search?difficulty=MEDIUM&maxPrice=160&page=0&size=2&sort=price,asc
```

The query is dynamically constructed using:

```java
Query
Criteria
MongoTemplate
```

Example:

```java
if (difficulty != null) {
    query.addCriteria(
        Criteria.where("difficulty").is(difficulty)
    );
}

if (minPrice != null) {
    query.addCriteria(
        Criteria.where("price").gte(minPrice)
    );
}
```

This avoids creating a separate repository method for every possible combination of filters.

---

# 📊 MongoDB Indexes

The project demonstrates MongoDB indexing and compound indexes.

A compound index is configured using:

```java
@CompoundIndex(
    name = "difficulty_price_idx",
    def = "{'difficulty': 1, 'price': 1}"
)
```

The index supports queries involving:

```text
difficulty
price
```

MongoDB index creation is enabled through:

```properties
spring.data.mongodb.auto-index-creation=true
```

The project also covers why **compound index field order matters** and how index design should be based on real query patterns.

---

# 🔬 MongoDB `explain()`

The project demonstrates how to analyze query execution.

Example:

```javascript
db.courses.find({
    difficulty: "MEDIUM",
    price: { $lt: 160 }
}).explain("executionStats")
```

Important execution statistics include:

```text
IXSCAN
COLLSCAN
totalKeysExamined
totalDocsExamined
nReturned
executionTimeMillis
```

This demonstrates how to verify whether MongoDB is using the expected index instead of scanning the entire collection.

---

# 📈 MongoDB Aggregation

The project demonstrates MongoDB aggregation pipelines using `MongoTemplate`.

Example aggregation:

```javascript
db.courses.aggregate([
    {
        $match: {
            price: { $gte: 100 }
        }
    },
    {
        $group: {
            _id: "$difficulty",
            averagePrice: { $avg: "$price" },
            courseCount: { $sum: 1 }
        }
    },
    {
        $project: {
            _id: 0,
            difficulty: "$_id",
            averagePrice: 1,
            courseCount: 1
        }
    },
    {
        $sort: {
            averagePrice: -1
        }
    }
])
```

The equivalent Spring Data implementation uses:

```java
Aggregation.newAggregation(...)
```

with stages such as:

```text
Aggregation.match()
Aggregation.group()
Aggregation.project()
Aggregation.sort()
```

The results are mapped to:

```java
CourseDifficultyStatsDto
```

This demonstrates server-side filtering, grouping, calculations, projection and sorting.

---

# 🔗 MongoDB `$lookup`

The project also demonstrates relationships between MongoDB collections.

Two collections are used:

```text
courses
enrollments
```

The project uses MongoDB's:

```text
$lookup
```

operator to perform a join-like operation.

Example:

```javascript
db.courses.aggregate([
    {
        $lookup: {
            from: "enrollments",
            localField: "_id",
            foreignField: "courseId",
            as: "enrollments"
        }
    }
])
```

Conceptually:

```text
courses._id
      │
      │ matches
      ▼
enrollments.courseId
```

The result is mapped to:

```java
CourseWithEnrollmentsDto
```

The `$lookup` behaves similarly to a **LEFT OUTER JOIN** in relational databases: courses without matching enrollments are still returned with an empty `enrollments` array.

The project also demonstrates the importance of matching MongoDB BSON types when using `$lookup`, such as:

```text
ObjectId ≠ String
```

---

# 🧱 Embedded vs Referenced Documents

The project demonstrates the conceptual difference between two common MongoDB modelling strategies.

### Embedded

Related data is stored directly inside the parent document.

```json
{
    "name": "Via Ferrata",
    "students": [
        {
            "name": "John"
        }
    ]
}
```

### Referenced

Related data is stored in another collection and linked using an identifier.

```text
courses
    ↓
courseId
    ↓
enrollments
```

The appropriate strategy depends on:

* Data relationships
* Read patterns
* Update patterns
* Document size
* Data growth
* Whether related data is shared

---

# 💳 MongoDB Transactions

The project demonstrates MongoDB transactions using Spring's `@Transactional`.

MongoDB transactions require a **replica set or sharded cluster**.

For local development, the project uses a **single-node replica set**, which is sufficient for transaction support.

Example:

```text
Replica Set: rs0

127.0.0.1:27017
       │
       ▼
    PRIMARY
```

The project demonstrates rollback after a failed operation:

```java
@Transactional
public void createCourseWithFailure() {
    CourseMongoDocument course =
        new CourseMongoDocument(
            "Transactional Course",
            200.0,
            Difficulty.HARD
        );

    courseMongoRepository.save(course);

    throw new RuntimeException(
        "Simulated transaction failure"
    );
}
```

It also demonstrates a transaction spanning multiple MongoDB collections:

```text
Transaction
    │
    ├── courses
    │
    └── enrollments
```

If the transaction fails after both writes, both operations are rolled back.

This demonstrates MongoDB transaction behaviour and the importance of configuring MongoDB as a replica set for transactional workloads.

---

# 🌐 MongoDB Endpoints

Current MongoDB course endpoints include:

```text
GET    /mongo/courses
GET    /mongo/courses/{id}
POST   /mongo/courses
PUT    /mongo/courses/{id}
DELETE /mongo/courses/{id}
```

Query examples:

```text
GET /mongo/courses/difficulty/{difficulty}

GET /mongo/courses/difficulty-lt-price/{difficulty}/{price}

GET /mongo/courses/difficulty-max-price-query?difficulty=EASY&price=100

GET /mongo/courses/minimum-price?price=90

GET /mongo/courses/difficultyIn?difficulties=EASY,MEDIUM

GET /mongo/courses/difficultyIn-query?difficulties=EASY,MEDIUM

GET /mongo/courses/difficultyNotIn-query?difficulties=HARD,MEDIUM

GET /mongo/courses/name-contains?name=ferr

GET /mongo/courses/search?difficulty=MEDIUM&maxPrice=160&page=0&size=2&sort=price,asc

GET /mongo/courses/dynamic-search

GET /mongo/courses/difficulty-stats

GET /mongo/courses/with-enrollments
```

---

# 🐳 Docker

Docker containerizes the complete backend environment and its infrastructure.

The application can run as a multi-container stack using Docker Compose:

```text
                    Docker Compose
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
       Spring Boot    PostgreSQL      MongoDB
          │
          │
     port 8080
```

PostgreSQL and MongoDB communicate with the application through the Docker Compose network using service names:

```text
postgres:5432
mongo:27017
```

The application exposes port `8080` to the host.

---

## Dockerfile

The application uses a **multi-stage Docker build**.

```text
Build stage
    │
    ├── Maven
    ├── JDK 21
    ├── Dependency resolution
    └── Application compilation
             │
             ▼
        application JAR
             │
             ▼
Runtime stage
    │
    ├── Java 21 JRE
    ├── Alpine Linux
    └── application JAR
```

The Maven build environment is not included in the final runtime image.

This reduces:

* Image size
* Attack surface
* Number of unnecessary production dependencies

The dependency layer is also separated from the source-code layer to improve Docker build-cache reuse.

---

# 🔐 Docker Security

The container is hardened using several production-oriented practices.

### Non-root user

The Spring Boot application does not run as root.

```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring

USER spring
```

The container was verified to run as:

```text
spring
```

### Read-only root filesystem

The application container uses:

```yaml
read_only: true
```

Temporary writable storage is provided through:

```yaml
tmpfs:
  - /tmp
```

This reduces the ability of a compromised application to modify the container filesystem.

---

# 🔑 Docker Secrets

Database credentials are not passed to the Spring Boot application as environment variables.

Instead, Docker Secrets are used.

Conceptually:

```text
Docker Secret
     │
     ├──────────────► PostgreSQL
     │
     └──────────────► Spring Boot
```

PostgreSQL receives the secret through:

```text
/run/secrets/postgres_password
```

The Spring Boot application receives it through:

```text
/run/secrets/spring.datasource.password
```

Spring Boot imports the mounted secret using:

```properties
spring.config.import=optional:configtree:/run/secrets/
```

This maps the mounted filename to:

```text
spring.datasource.password
```

The database password is therefore not exposed as an application environment variable.

---

# 🌐 Docker Networking

Docker Compose provides an internal DNS service.

Containers communicate using service names rather than `localhost`.

For example:

```text
Spring Boot
     │
     ├── postgres:5432
     │
     └── mongo:27017
```

Inside the Spring Boot container:

```text
localhost
```

refers to the Spring Boot container itself.

It does **not** refer to PostgreSQL or MongoDB.

This distinction is essential when troubleshooting containerized applications.

---

# ❤️ Docker Healthchecks

Spring Boot Actuator is used to expose application health information.

The health endpoint is:

```text
GET /actuator/health
```

Example response:

```json
{
    "groups": [
        "liveness",
        "readiness"
    ],
    "status": "UP"
}
```

Docker uses the endpoint as its container healthcheck:

```yaml
healthcheck:
  test: ["CMD-SHELL", "wget -q --spider http://localhost:8080/actuator/health || exit 1"]
  interval: 10s
  timeout: 5s
  retries: 5
  start_period: 20s
```

The `start_period` prevents normal application startup time from immediately causing the container to become unhealthy.

The project also demonstrates the distinction between:

```text
Liveness
    ↓
Is the application alive?

Readiness
    ↓
Is the application ready to receive traffic?
```

These concepts will become particularly important when the application is deployed to Kubernetes.

---

# 🩺 Docker Troubleshooting

The project includes hands-on troubleshooting exercises covering:

```text
docker compose ps
docker compose logs
docker compose exec
docker inspect
```

The troubleshooting workflow is:

```text
docker compose ps
        ↓
Is the container running?
        ↓
docker compose logs
        ↓
What is the application reporting?
        ↓
docker compose exec app sh
        ↓
Can we inspect the container from inside?
        ↓
docker inspect
        ↓
What configuration was actually applied?
```

Docker DNS can also be verified from inside the application container:

```bash
getent hosts postgres
getent hosts mongo
```

A deliberate failure was introduced by changing the PostgreSQL hostname from:

```text
postgres
```

to:

```text
localhost
```

The resulting failure demonstrated that `localhost` inside a container refers to the container itself rather than another Compose service.

The issue was diagnosed through application logs, container inspection and Docker DNS verification.

---

# 🔍 Docker Vulnerability Scanning

The project uses **Trivy** to scan Docker images for known vulnerabilities.

Example:

```bash
trivy image climbing-management-sb-app:latest
```

The project demonstrated the difference between vulnerabilities in:

```text
Application dependencies
        vs
Base operating-system packages
```

A vulnerability scan initially detected critical vulnerabilities in:

```text
org.apache.tomcat.embed:tomcat-embed-core
```

The dependency was traced through the Maven dependency tree:

```text
Spring Boot
    ↓
spring-boot-starter-tomcat
    ↓
tomcat-embed-core
```

The Tomcat version was explicitly overridden to the fixed release.

The application JAR subsequently reported:

```text
CRITICAL: 0
```

Remaining operating-system vulnerabilities were identified separately as Alpine base-image findings.

This demonstrates a practical vulnerability-management workflow:

```text
Scan
  ↓
Identify severity
  ↓
Locate dependency
  ↓
Determine fixed version
  ↓
Update dependency
  ↓
Rebuild image
  ↓
Rescan
```

---

# 🧪 Docker + Spring Boot Configuration

Docker-specific Spring configuration is activated using:

```yaml
SPRING_PROFILES_ACTIVE: docker
```

The Docker profile uses Compose service names:

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/${APP_POSTGRES_DB}

spring.mongodb.uri=mongodb://mongo:27017/${APP_MONGO_DB}
```

The configuration flow is:

```text
.env
 │
 ├── ENV_POSTGRES_DB
 ├── ENV_POSTGRES_USER
 ├── ENV_MONGO_DB
 └── ENV_APP_PORT
        │
        ▼
Docker Compose
        │
        ▼
Spring Boot environment
        │
        ▼
application-docker.properties
```

Secrets follow a separate path:

```text
Docker Secret
      │
      ▼
/run/secrets/
      │
      ▼
Spring Boot configtree
      │
      ▼
spring.datasource.password
```

---

# 🗺️ Roadmap

The project is being developed progressively.

## Completed

* [x] Spring Boot project setup
* [x] Java 21 configuration
* [x] REST API
* [x] Layered architecture
* [x] DTOs / Java Records
* [x] Bean Validation
* [x] Global Exception Handling
* [x] SLF4J logging
* [x] PostgreSQL
* [x] Spring Data JPA
* [x] Hibernate
* [x] Derived Queries
* [x] JPQL
* [x] `@Transactional`
* [x] Transaction propagation
* [x] `REQUIRED`
* [x] `REQUIRES_NEW`
* [x] Rollback rules
* [x] Hibernate Dirty Checking
* [x] Optimistic Locking
* [x] Pessimistic Locking
* [x] MongoDB
* [x] Spring Data MongoDB
* [x] MongoRepository
* [x] MongoDB operators
* [x] MongoDB pagination
* [x] MongoDB sorting
* [x] MongoDB DTOs
* [x] MongoDB validation
* [x] MongoDB indexes
* [x] Compound indexes
* [x] MongoDB `explain()`
* [x] MongoTemplate
* [x] Dynamic MongoDB queries
* [x] MongoDB aggregation
* [x] Aggregation DTO mapping
* [x] MongoDB `$lookup`
* [x] Embedded vs referenced document modelling
* [x] MongoDB replica set configuration
* [x] MongoDB transactions
* [x] MongoDB transaction rollback
* [x] Multi-collection MongoDB transactions
* [x] MongoDB interview comparison with PostgreSQL
* [x] Docker fundamentals
* [x] Docker images and containers
* [x] Dockerfile
* [x] Docker image layers and build cache
* [x] Docker port mapping
* [x] Docker environment variables
* [x] Docker volumes
* [x] Docker networks
* [x] PostgreSQL container
* [x] MongoDB container
* [x] Docker Compose
* [x] Multi-container application
* [x] Docker Compose service discovery
* [x] Multi-stage Docker builds
* [x] Alpine-based runtime image
* [x] Non-root container user
* [x] Read-only container filesystem
* [x] Docker tmpfs
* [x] Docker Secrets
* [x] Spring Boot Docker profile
* [x] Spring Boot Actuator
* [x] Docker healthchecks
* [x] Liveness / readiness concepts
* [x] Docker troubleshooting
* [x] Container inspection with `docker inspect`
* [x] Docker DNS troubleshooting
* [x] Trivy vulnerability scanning
* [x] Dependency vulnerability remediation

## Current

* [ ] CI/CD
* [ ] GitHub Actions

## Upcoming

* [ ] Docker image publishing
* [ ] Container registry
* [ ] Kubernetes
* [ ] Kubernetes Deployments
* [ ] Kubernetes Services
* [ ] Kubernetes health probes
* [ ] Advanced REST API design
* [ ] Spring Security
* [ ] Unit testing
* [ ] Integration testing
* [ ] Testcontainers
* [ ] Messaging
* [ ] Microservices
* [ ] System design
* [ ] Senior Backend Java interview preparation

---

# 🎯 Project Goals

The main goal of this project is to build a realistic backend application while demonstrating practical knowledge of enterprise Java technologies.

Key areas include:

* REST API design
* Layered architecture
* Separation of responsibilities
* DTO-based API contracts
* Persistence and ORM
* Transaction management
* Transaction propagation
* Rollback behaviour
* Hibernate dirty checking
* Optimistic locking
* Pessimistic locking
* Exception handling
* Bean Validation
* Relational databases
* NoSQL databases
* MongoDB data modelling
* MongoDB indexing
* Query performance analysis
* Dynamic queries
* Aggregation pipelines
* Collection joins using `$lookup`
* MongoDB transactions
* Containerization
* Docker Compose
* Docker security
* Docker Secrets
* Healthchecks
* Vulnerability scanning
* CI/CD
* Kubernetes
* Distributed systems
* Messaging
* Microservices

The project is also used as a practical learning environment for **Senior Backend Java development and technical interview preparation**.

---

# 🛠️ Running the Project

## Requirements

### Local development

* Java 21
* Maven 3.9+
* PostgreSQL 17
* MongoDB 7+
* Git

### Docker development

* Docker Desktop
* Docker Compose

The recommended development approach is to run the infrastructure and application through Docker Compose.

Clone the repository:

```bash
git clone <repository-url>
cd climbing-management-sb
```

---

## Maven

Run the test suite:

```bash
mvn clean test
```

Build the application:

```bash
mvn clean package
```

Run the application locally:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

---

## Docker Compose

Start the complete environment:

```bash
docker compose up -d --build
```

Check the containers:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs app
```

Follow application logs:

```bash
docker compose logs -f app
```

Stop the environment:

```bash
docker compose down
```

The application will be available at:

```text
http://localhost:8080
```

Healthcheck:

```text
http://localhost:8080/actuator/health
```

Expected response:

```json
{
    "groups": [
        "liveness",
        "readiness"
    ],
    "status": "UP"
}
```

---

# 📮 Example Requests

## Create a PostgreSQL course

```http
POST /jpa/courses
Content-Type: application/json
```

```json
{
    "name": "Sport Climbing",
    "price": 120.0,
    "difficulty": "EASY"
}
```

Example response:

```json
{
    "id": 1,
    "name": "Sport Climbing",
    "price": 120.0,
    "difficulty": "EASY"
}
```

---

## Create a MongoDB course

```http
POST /mongo/courses
Content-Type: application/json
```

```json
{
    "name": "Via Ferrata",
    "price": 90.0,
    "difficulty": "EASY"
}
```

---

## Dynamic MongoDB search

```http
GET /mongo/courses/dynamic-search?difficulty=MEDIUM&minPrice=100&maxPrice=160&page=0&size=2&sort=price,asc
```

---

## MongoDB aggregation

```http
GET /mongo/courses/difficulty-stats
```

Returns statistics grouped by course difficulty.

---

## MongoDB `$lookup`

```http
GET /mongo/courses/with-enrollments
```

Returns courses together with their related enrollments.

---

# 📌 Learning Approach

The project is intentionally developed incrementally.

Each major technology is introduced through:

```text
Theory
   ↓
Implementation
   ↓
Database inspection
   ↓
API testing
   ↓
Concurrency / behaviour testing
   ↓
Troubleshooting
   ↓
Interview questions
   ↓
Code cleanup
   ↓
Git commit
```

Each major milestone is committed to Git so the repository provides a clear history of the technologies and concepts implemented.

The result is both a functional backend application and a practical **Senior Backend Java interview preparation environment**.

---

# 👨‍💻 Author

**Rubén Marín**

Backend Java Developer

Technologies explored in this project include:

`Java` · `Spring Boot` · `Spring Data JPA` · `Hibernate` · `PostgreSQL` · `Spring Data MongoDB` · `MongoDB` · `MongoTemplate` · `Docker` · `Docker Compose` · `GitHub Actions` · `Kubernetes`
