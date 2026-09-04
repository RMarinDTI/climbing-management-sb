# Climbing Management API

A backend application built with **Java 21 and Spring Boot** to manage climbing courses, designed as a practical project to demonstrate modern backend development, persistence, transactions, concurrency control, and REST API design.

The project is being developed incrementally, introducing different technologies and architectural patterns commonly used in enterprise Java applications.

## 🚀 Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Hibernate**
* **PostgreSQL**
* **Bean Validation**
* **Maven**
* **Git / GitHub**
* **Postman**

Upcoming technologies:

* **MongoDB**
* **Docker**
* **Kubernetes**

## 🏗️ Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

### Main layers

**Controller**

Exposes the REST API and handles HTTP requests and responses.

**Service**

Contains business logic and transaction boundaries.

**Repository**

Provides data access through Spring Data.

**Entity / Document**

Represents the persistence model.

**DTO / Record**

Defines the API contract independently from the persistence model.

## 📚 Current Features

### REST API

Course management endpoints:

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

## 🔎 Spring Data JPA

The project demonstrates different approaches to database querying.

### Derived Queries

Queries are generated automatically from repository method names.

Example:

```java
List<CourseEntity> findByDifficulty(Difficulty difficulty);
```

### JPQL

Custom queries using the entity model:

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

## 🔄 Transactions

The project demonstrates Spring transaction management with:

* `@Transactional`
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

The project also demonstrates how transaction propagation affects independent operations.

## 🧠 Hibernate Dirty Checking

Existing managed entities are modified without explicitly calling `save()`:

```java
existing.setPrice(courseRecord.price());
```

Hibernate detects the modification through the Persistence Context and generates the required `UPDATE` when the transaction commits.

## 🔐 Concurrency Control

Both major JPA locking strategies are demonstrated.

### Optimistic Locking

Implemented with:

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

### Pessimistic Locking

Implemented with:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

This requests a database-level lock on the selected row.

With PostgreSQL, Hibernate generated SQL similar to:

```sql
SELECT ...
FROM courses
WHERE id = ?
FOR NO KEY UPDATE;
```

The exact SQL depends on the database and Hibernate dialect.

## ⚠️ Global Exception Handling

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

## 🧪 Validation

The API uses Jakarta Bean Validation.

Example:

```java
@NotBlank
String name

@Positive
Double price
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

## 🗄️ Database

### PostgreSQL

PostgreSQL is currently the main relational database.

The application uses:

```text
Spring Data JPA
        ↓
Hibernate
        ↓
JDBC
        ↓
PostgreSQL
```

The project intentionally keeps PostgreSQL as the relational persistence layer while MongoDB is introduced as an additional persistence technology.

## 🔜 Roadmap

The project is being expanded progressively.

### Completed

* [x] Spring Boot project setup
* [x] REST API
* [x] DTOs / Java Records
* [x] Bean Validation
* [x] JPA / Hibernate
* [x] Spring Data repositories
* [x] Derived Queries
* [x] JPQL
* [x] Transactions
* [x] Dirty Checking
* [x] Transaction propagation
* [x] Rollback rules
* [x] Optimistic Locking
* [x] Pessimistic Locking
* [x] Global Exception Handling
* [x] SLF4J logging

### In Progress

* [ ] MongoDB
* [ ] Spring Data MongoDB
* [ ] PostgreSQL vs MongoDB comparison
* [ ] Docker
* [ ] Containerized application
* [ ] Kubernetes
* [ ] Microservices
* [ ] Messaging
* [ ] Automated testing
* [ ] Integration testing
* [ ] CI/CD

## 🎯 Project Goals

The main goal of this project is to build a realistic backend application while demonstrating practical knowledge of enterprise Java technologies.

Key areas include:

* REST API design
* Clean separation of responsibilities
* Persistence and ORM
* Transaction management
* Concurrency control
* Exception handling
* Relational and NoSQL databases
* Containerization
* Kubernetes
* Distributed systems

The project is also used as a practical learning environment for **Senior Backend Java development and technical interview preparation**.

## 🛠️ Running the Project

### Requirements

* Java 21
* Maven 3.9+
* PostgreSQL 17

Clone the repository:

```bash
git clone <repository-url>
cd climbing-management-sb
```

Build the project:

```bash
mvn clean install
```

Run the application:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## 📮 Example Request

Create a course:

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

Response:

```json
{
    "id": 1,
    "name": "Sport Climbing",
    "price": 120.0,
    "difficulty": "EASY"
}
```

## 👨‍💻 Author

**Rubén Marín**

Backend Java Developer

Technologies explored in this project include:

`Java` · `Spring Boot` · `Spring Data JPA` · `Hibernate` · `PostgreSQL` · `MongoDB` · `Docker` · `Kubernetes`
