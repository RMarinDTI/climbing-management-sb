# Climbing Management API

A backend application built with **Java 21 and Spring Boot** to manage climbing courses.

The project is designed as a practical **Senior Backend Java** learning and portfolio project, demonstrating modern enterprise backend development, REST APIs, persistence, transaction management, concurrency control, relational and NoSQL databases, dynamic queries, aggregation, and clean layered architecture.

The application is being developed incrementally, introducing technologies and architectural patterns commonly used in enterprise Java applications.

---

## 🚀 Tech Stack

### Backend

* **Java 21**
* **Spring Boot**
* **Spring Web**
* **Spring Data JPA**
* **Spring Data MongoDB**
* **Hibernate**
* **Jakarta Bean Validation**
* **SLF4J / Logging**

### Databases

* **PostgreSQL**
* **MongoDB**

### Development Tools

* **Maven**
* **Git / GitHub**
* **IntelliJ IDEA**
* **Postman**

### Planned

* **Docker**
* **Kubernetes**
* Automated testing
* Integration testing
* CI/CD
* Messaging
* Microservices

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
              ▼                 ▼
         PostgreSQL          MongoDB
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

## 🗄️ Spring Data JPA

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

Collection:

```text
courses
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

---

# 🔬 MongoDB `explain()`

The project also demonstrates how to analyze query execution.

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

Example successful execution:

```text
winningPlan → IXSCAN
nReturned → 3
totalKeysExamined → 3
totalDocsExamined → 3
```

This demonstrates that MongoDB is using the compound index rather than scanning the entire collection.

---

# 📈 MongoDB Aggregation

The project demonstrates MongoDB aggregation pipelines using both MongoDB directly and `MongoTemplate`.

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

Example response:

```json
[
    {
        "difficulty": "HARD",
        "averagePrice": 166.66666666666666,
        "courseCount": 3
    },
    {
        "difficulty": "MEDIUM",
        "averagePrice": 135.0,
        "courseCount": 3
    }
]
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

Example enrollment:

```json
{
    "courseId": ObjectId("6a9ada74aed9b79d82d16295"),
    "studentName": "John"
}
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

with nested:

```java
EnrollmentDto
```

Example API response:

```json
{
    "id": "6a9ada74aed9b79d82d16295",
    "name": "Via Ferrata",
    "price": 90.0,
    "difficulty": "EASY",
    "enrollments": [
        {
            "studentName": "John"
        },
        {
            "studentName": "Anna"
        }
    ]
}
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

# 🌐 MongoDB Endpoints

Current MongoDB course endpoints include:

```text
GET    /mongo/courses
GET    /mongo/courses/{id}
POST   /mongo/courses
PUT    /mongo/courses/{id}
DELETE  /mongo/courses/{id}
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

## In Progress

* [ ] MongoDB transactions
* [ ] Final MongoDB interview Q&A
* [ ] MongoDB code cleanup

## Upcoming

* [ ] Docker
* [ ] Containerized application
* [ ] Docker Compose
* [ ] Kubernetes
* [ ] Kubernetes deployments and services
* [ ] Microservices
* [ ] Messaging
* [ ] Automated testing
* [ ] Unit testing
* [ ] Integration testing
* [ ] CI/CD

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
* Dynamic queries
* Aggregation pipelines
* Collection joins using `$lookup`
* Containerization
* Kubernetes
* Distributed systems

The project is also used as a practical learning environment for **Senior Backend Java development and technical interview preparation**.

---

# 🛠️ Running the Project

## Requirements

* Java 21
* Maven 3.9+
* PostgreSQL 17
* MongoDB 8+
* Git

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
Interview questions
   ↓
Code cleanup
   ↓
Git commit
```

This approach makes the project both a functional backend application and a practical **Senior Java interview preparation environment**.

---

# 👨‍💻 Author

**Rubén Marín**

Backend Java Developer

Technologies explored in this project include:

`Java` · `Spring Boot` · `Spring Data JPA` · `Hibernate` · `PostgreSQL` · `Spring Data MongoDB` · `MongoDB` · `MongoTemplate` · `Docker` · `Kubernetes`
