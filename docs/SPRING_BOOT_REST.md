# Spring Boot and REST API

This document owns the learning material related to Spring Boot, layered architecture, REST, DTOs, validation, exception handling, logging and core API examples.

Project progress is tracked only in `ROADMAP.md`.

---

# 1. Layered Architecture

```text
REST API
   │
   ▼
Controller
   │
   ▼
Service
   │
   ├────────────────────┐
   ▼                    ▼
JPA Repository     Mongo Repository
```

## Controller

Responsible for:

- Defining endpoints
- Reading path/query/body parameters
- Validating input
- Delegating to the Service
- Returning HTTP responses

The Controller should not contain persistence logic.

## Service

Responsible for:

- Business logic
- Transaction boundaries
- Coordination
- Calling repositories

## Repository

Responsible for persistence access.

Spring Data can generate many implementations automatically.

---

# 2. DTOs and Persistence Models

```text
HTTP Request
    │
    ▼
Request DTO
    │
    ▼
Service
    │
    ▼
Entity / Document
    │
    ▼
Database
```

Response path:

```text
Database
    │
    ▼
Entity / Document
    │
    ▼
Response DTO
    │
    ▼
HTTP Response
```

The API contract and persistence model can therefore evolve independently.

---

# 3. REST API — PostgreSQL Courses

```text
GET     /jpa/courses
GET     /jpa/courses/{id}
POST    /jpa/courses
PUT     /jpa/courses/{id}
DELETE  /jpa/courses/{id}
```

Additional queries:

```text
GET /jpa/courses/most-expensive
GET /jpa/courses/difficulty/{difficulty}
GET /jpa/courses/difficulty/{difficulty}/price/{price}
```

---

# 4. Create Course Example

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

# 5. HTTP Status Codes

```text
200 OK
    Existing resource successfully returned or updated

201 CREATED
    New resource successfully created

204 NO CONTENT
    Successful operation with no response body

400 BAD REQUEST
    Invalid request or validation failure

404 NOT FOUND
    Requested resource does not exist

500 INTERNAL SERVER ERROR
    Unexpected server-side error
```

---

# 6. Bean Validation

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

Flow:

```text
Incoming JSON
    │
    ▼
DTO validation
    │
    ├── valid ─────► Controller / Service
    │
    └── invalid ───► HTTP 400
```

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

Validation is useful for structural constraints such as required values, positive numbers and formats.

---

# 7. Global Exception Handling

The project centralizes REST errors with:

```java
@RestControllerAdvice
```

Conceptually:

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

Example unexpected response:

```json
{
    "timestamp": "2026-09-04T10:52:28.462609Z",
    "status": 500,
    "message": "An unexpected error occurred"
}
```

Detailed exceptions are logged internally while the client receives a safe public message.

Do not expose stack traces, SQL, file paths or internal infrastructure details.

---

# 8. Logging

SLF4J conceptually:

```java
private static final Logger log =
        LoggerFactory.getLogger(MyClass.class);
```

```text
Internal log
    ↓
Detailed diagnostic context

HTTP response
    ↓
Safe public error
```

---

# 9. Spring Boot Actuator

Endpoints used in the project:

```text
/actuator/health
/actuator/health/liveness
/actuator/health/readiness
```

They are later reused by Docker healthchecks and Kubernetes probes.

---

# 10. Maven Commands

Tests:

```bash
mvn clean test
```

Build:

```bash
mvn clean package
```

Run locally:

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

API:

```text
http://localhost:8080
```

---

# 11. Interview Takeaways

## Controller vs Service

Controller = HTTP boundary.

Service = business logic and transaction orchestration.

## Entity vs DTO

Entity/document = persistence model.

DTO = boundary/API contract.

## Why `@RestControllerAdvice`?

Consistency, centralized logging and less duplicated exception mapping.

## Why Bean Validation?

Reject structurally invalid input early and declaratively.

---

# 12. Practical Verification Loop

```text
1. Compile
2. Run tests
3. Start app
4. Call endpoint
5. Test success
6. Test invalid input
7. Test not-found
8. Check logs
9. Commit
10. Verify CI
```

---

[Back to README](../README.md)
