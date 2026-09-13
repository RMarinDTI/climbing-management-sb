# Microservices

This document contains the learning material for the microservices evolution of the Climbing Management project.

Progress tracking remains centralized in `ROADMAP.md`.

---

# 1. Monolith vs Microservices

The original application was a single Spring Boot deployment unit:

```text
Client
  │
  ▼
climbing-management-sb
  │
  ├── Course logic
  └── Enrollment logic
```

Even if code is separated into packages, it is still a monolith while everything is built and deployed as one application.

A microservice introduces an independent application and deployment boundary.

```text
One application
      ↓
One process
      ↓
One deployment unit
```

becomes:

```text
Multiple applications
        ↓
Independent processes
        ↓
Independent deployment units
```

A microservice is therefore **not simply another Java package**.

---

# 2. First Service Boundary

The first extracted business capability is:

```text
Enrollment Management
```

The repository now contains two independent Spring Boot applications:

```text
climbing-management-sb/
│
├── pom.xml
├── src/
│
└── services/
    └── enrollment-service/
        ├── pom.xml
        └── src/
```

This is a **monorepo**:

```text
One Git repository
        ↓
Multiple independently runnable applications
```

Important distinction:

```text
Git repository boundary
        ≠
Application boundary
        ≠
Deployment boundary
        ≠
Database boundary
```

---

# 3. Independent Runtime

The applications run independently:

```text
Climbing Management
        ↓
localhost:8080

Enrollment Service
        ↓
localhost:8081
```

Each application has its own:

* `pom.xml`
* Spring Boot `main()` method
* dependencies
* configuration
* HTTP port
* JVM process

Therefore:

```text
Same Git repository
        ≠
Same application
```

Current runtime architecture:

```text
Windows
│
├── JVM #1
│     │
│     └── Climbing Management
│            localhost:8080
│
└── JVM #2
      │
      └── Enrollment Service
             localhost:8081
```

---

# 4. Enrollment API

The Enrollment Service exposes:

```text
POST /enrollments
GET  /enrollments
```

Create example:

```http
POST http://localhost:8081/enrollments
Content-Type: application/json
```

```json
{
  "courseId": 1,
  "studentName": "Angie"
}
```

Example successful response:

```json
{
  "id": "6aa658eb77f2c5799e02750d",
  "courseId": 1,
  "studentName": "Angie"
}
```

Response:

```text
201 Created
```

Retrieve enrollments:

```http
GET http://localhost:8081/enrollments
```

Example:

```json
[
  {
    "id": "6aa658eb77f2c5799e02750d",
    "courseId": 1,
    "studentName": "Angie"
  }
]
```

---

# 5. Evolution from In-Memory Storage

The first implementation deliberately used an in-memory list:

```text
HTTP
  │
  ▼
EnrollmentController
  │
  ▼
EnrollmentService
  │
  ▼
In-memory List
```

This was useful because it allowed the microservice boundary to be established before introducing persistence complexity.

The limitation was:

```text
Application restart
      ↓
In-memory data lost
```

The next step replaced the in-memory list with independent MongoDB persistence.

---

# 6. Independent Persistence

The Enrollment Service now owns its own MongoDB persistence.

```text
Enrollment Service
        │
        ▼
EnrollmentRepository
        │
        ▼
Spring Data MongoDB
        │
        ▼
enrollment_management
        │
        ▼
enrollments
```

Local MongoDB configuration:

```properties
spring.mongodb.uri=mongodb://localhost:27017/enrollment_management
```

The important concept is:

> Each service owns its data.

This does **not** necessarily require one physical database server per service.

For local development, multiple logical databases can exist on the same MongoDB server.

What matters is ownership.

```text
Course application
        │
        └── owns Course data

Enrollment Service
        │
        └── owns Enrollment data
```

Another service should not bypass the owning service and directly manipulate its database.

---

# 7. MongoDB Persistence Model

The Enrollment Service uses:

```text
EnrollmentDocument
├── id          String / ObjectId
├── courseId    Long
└── studentName String
```

Example MongoDB document:

```text
_id
ObjectId('6aa658eb77f2c5799e02750d')

courseId
Long('1')

studentName
"Angie"

_class
"com.rubenmarin.enrollmentservice.document.EnrollmentDocument"
```

The `_class` field is added by Spring Data MongoDB by default as type metadata.

---

# 8. Different Services Can Use Different ID Types

The Enrollment Service uses a MongoDB-generated identifier:

```text
Enrollment ID
      ↓
String / ObjectId
```

The Course application currently uses numeric identifiers:

```text
Course ID
      ↓
Long
```

Therefore:

```text
Enrollment
├── id          String
├── courseId    Long
└── studentName String
```

This is perfectly valid.

Different microservices do not need identical identifier strategies.

The Enrollment Service stores `courseId` as an external reference to data owned by the Course application.

---

# 9. Database Ownership

A poor service boundary would allow both services to directly access the same data model:

```text
Course Service ──────┐
                     ▼
                Shared Data
                     ▲
Enrollment Service ──┘
```

The preferred direction is:

```text
Course Service
      │
      ▼
Course Data


Enrollment Service
      │
      ▼
Enrollment Data
```

If Enrollment Service needs information about a course, it asks the owning service through its API.

It does not directly query the Course database.

---

# 10. Service-to-Service Communication

Before creating an enrollment, the Enrollment Service verifies that the requested course exists.

Architecture:

```text
POST /enrollments
        │
        ▼
EnrollmentController
        │
        ▼
EnrollmentService
        │
        ▼
CourseClient
        │
        │ HTTP
        ▼
Course Application
```

Local configuration:

```properties
course-service.base-url=http://localhost:8080
```

Enrollment Service:

```text
localhost:8081
```

Course application:

```text
localhost:8080
```

This is the first real network boundary between the two applications.

---

# 11. RestClient

The Enrollment Service uses Spring `RestClient` for synchronous HTTP communication.

Conceptually:

```text
Enrollment Service
        │
        │ HTTP GET
        ▼
Course Application
        │
        ├── 2xx
        │    ↓
        │ course exists
        │
        └── 404
             ↓
        course missing
```

In Spring Boot 4.1, the Enrollment Service includes the REST client infrastructure required to inject:

```java
RestClient.Builder
```

The client is built from:

```java
RestClient.Builder
        ↓
baseUrl
        ↓
RestClient
```

---

# 12. Monolith Call vs Microservice Call

Inside a monolith, this could be:

```text
EnrollmentService
        │
        ▼
CourseService
        │
        ▼
Normal Java method call
```

With separate services:

```text
Enrollment Service
        │
        ▼
Network
        │
        ▼
HTTP
        │
        ▼
Course Application
```

This distinction is fundamental.

A local Java call normally does not depend on:

* DNS
* Network availability
* TCP connections
* HTTP status codes
* Timeouts
* Remote service health

A microservice call does.

---

# 13. Successful Enrollment Flow

When the course exists:

```text
POST /enrollments
        │
        ▼
EnrollmentController
        │
        ▼
EnrollmentService
        │
        ▼
CourseClient
        │
        │ HTTP
        ▼
Course Application
        │
        ▼
2xx response
        │
        ▼
EnrollmentRepository
        │
        ▼
MongoDB
        │
        ▼
201 Created
```

The Enrollment Service only persists the enrollment after confirming that the Course exists.

---

# 14. Missing Course Flow

When the course does not exist:

```text
POST /enrollments
        │
        ▼
CourseClient
        │
        ▼
Course Application
        │
        ▼
404 Not Found
        │
        ▼
courseExists() = false
        │
        ▼
CourseNotFoundException
        │
        ▼
GlobalExceptionHandler
        │
        ▼
404 Not Found
```

Example:

```json
{
  "timestamp": "2026-09-13T08:50:30.565190300Z",
  "status": 404,
  "message": "Course not found: 2"
}
```

This is expected business behavior.

It should not result in:

```text
500 Internal Server Error
```

A `500` should normally represent an unexpected server-side failure.

---

# 15. Exception Translation

The remote Course application returns its HTTP result.

The Enrollment Service then translates that result into its own business meaning.

```text
Course Application
        │
        ▼
404
        │
        ▼
CourseClient
        │
        ▼
false
        │
        ▼
EnrollmentService
        │
        ▼
CourseNotFoundException
        │
        ▼
Enrollment API
        │
        ▼
404
```

This is an important microservices principle:

> Each service owns its API behavior even when the failure originated in another service.

---

# 16. Local Configuration Profiles

Environment-specific connectivity belongs in profile-specific configuration.

For local development:

```text
application-local.properties
```

contains values such as:

```properties
spring.mongodb.uri=mongodb://localhost:27017/enrollment_management

course-service.base-url=http://localhost:8080
```

This keeps local infrastructure configuration separate from environment-independent application settings.

Later environments may use different addresses:

```text
Local
   ↓
localhost

Docker
   ↓
Docker service names

Kubernetes
   ↓
Kubernetes Service DNS
```

---

# 17. MongoDB in Docker vs Local Applications

MongoDB currently runs inside Docker.

Applications running directly on Windows reach it through:

```text
localhost:27017
```

Applications running inside the Docker network would normally use:

```text
mongo:27017
```

Conceptually:

```text
Windows application
        │
        ▼
localhost:27017
        │
        ▼
Docker port mapping
        │
        ▼
MongoDB container
```

Inside Docker:

```text
Application container
        │
        ▼
mongo:27017
        │
        ▼
MongoDB container
```

This distinction will become important again when the Enrollment Service is containerized.

---

# 18. MongoDB Compass and Replica Set

MongoDB is configured as a replica set.

Inside Docker, the replica-set member may identify itself using a hostname such as:

```text
mongo:27017
```

That hostname works inside Docker networking but may not be resolvable from Windows.

For MongoDB Compass, a local direct connection can be used:

```text
mongodb://localhost:27017/enrollment_management?replicaSet=rs0&directConnection=true
```

`directConnection=true` tells the client to connect directly to the specified MongoDB instance instead of relying on replica-set member discovery.

---

# 19. Testing Configuration

The local application configuration should not automatically be assumed by tests.

For example:

```text
application-local.properties
```

is only loaded when the `local` profile is active.

Tests that require configuration such as:

```properties
course-service.base-url=http://localhost:8080
```

can provide that property directly through the test configuration.

Example:

```java
@SpringBootTest(properties = {
        "course-service.base-url=http://localhost:8080"
})
class EnrollmentServiceApplicationTests {
}
```

This allows the Spring context to create `CourseClient` during tests without requiring the local runtime profile.

---

# 20. Current Architecture

The project now looks like:

```text
                         Client
                           │
              ┌────────────┴────────────┐
              ▼                         ▼
   Climbing Management API      Enrollment Service
       Spring Boot                  Spring Boot
          :8080                       :8081
              │                         │
              │                         ▼
              │                EnrollmentRepository
              │                         │
              │                         ▼
              │                 MongoDB
              │             enrollment_management
              │
              ◄──────── HTTP ──────────┘
                   course validation
```

The Enrollment Service now has:

* Independent runtime
* Independent Maven build
* Independent API
* Independent persistence
* Its own MongoDB database ownership
* Service-to-service REST communication
* Remote 404 translation
* Separate local configuration

---

# 21. Important Distributed-System Lesson

A normal local Java method call can fail because of application logic.

A remote service call introduces additional failure modes:

```text
Course not found
Connection refused
Network unavailable
Timeout
DNS failure
Remote service returns 500
Malformed response
Slow response
Remote service restarts
```

Therefore:

```text
Remote call
    ≠
Local method call
```

Once a system becomes distributed, the network becomes part of the architecture.

---

# 22. Current Next Step

The next milestone is to test what happens when the Course application is completely unavailable.

```text
Enrollment Service
        │
        ▼
CourseClient
        │
        ▼
Course Application DOWN
```

This introduces:

* Connection failures
* Dependency availability
* Failure isolation
* Proper error mapping
* Timeouts
* Resilience concepts

This is where the project starts moving from simply having multiple services to understanding **distributed-system behavior**.

---

[Back to README](../README.md)
