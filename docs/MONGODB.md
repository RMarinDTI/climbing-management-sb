# MongoDB and Spring Data MongoDB

This document owns the full MongoDB learning section.

Progress belongs only in `ROADMAP.md`.

---

# 1. Why MongoDB Was Added

MongoDB is used alongside PostgreSQL to compare relational and document-oriented persistence.

```text
Application
   │
   ├───────────────────────┐
   ▼                       ▼
PostgreSQL               MongoDB
@Entity                  @Document
JPA/Hibernate            Spring Data MongoDB
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

# 2. MongoDB Documents

```java
@Document(collection = "courses")
public class CourseMongoDocument {
    ...
}
```

The MongoDB model is separate from the JPA entity.

```text
@Entity
   ↓
Relational model

@Document
   ↓
Document model
```

---

# 3. MongoRepository

```java
save(...)
findById(...)
findAll(...)
deleteById(...)
existsById(...)
count()
```

Use repository abstractions for CRUD and stable declarative queries.

---

# 4. Derived Queries

```java
List<CourseMongoDocument> findByDifficulty(
        Difficulty difficulty
);
```

More complex:

```java
List<CourseMongoDocument> findByDifficultyAndPriceLessThan(
        Difficulty difficulty,
        Double price
);
```

---

# 5. Custom @Query

```java
@Query("{ 'difficulty': ?0, 'price': { $lt: ?1 } }")
List<CourseMongoDocument> findCoursesByDifficultyAndMaxPriceQuery(
        Difficulty difficulty,
        Double price
);
```

Operators studied:

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

# 6. Text Search

```java
List<CourseMongoDocument> findByNameContainingIgnoreCase(
        String name
);
```

Simple case-insensitive substring search.

---

# 7. Pagination and Sorting

```java
Page<CourseMongoDocument> findByDifficultyAndPriceLessThanEqual(
        Difficulty difficulty,
        Double price,
        Pageable pageable
);
```

Request:

```http
GET /mongo/courses/search?difficulty=MEDIUM&maxPrice=160&page=1&size=2&sort=price,asc
```

A `Page` includes:

- Current page
- Page size
- Total elements
- Total pages
- Content

---

# 8. DTOs

Request DTO:

```java
public class CourseMongoRequestDto {
    ...
}
```

Response DTO:

```java
public class CourseMongoResponseDto {
    ...
}
```

```text
Persistence model
      ≠
Public API contract
```

---

# 9. MongoTemplate

The custom repository flow:

```text
CourseMongoCustomRepository
        ↓
CourseMongoCustomRepositoryImpl
        ↓
MongoTemplate
```

Use `MongoTemplate` when dynamic query construction or advanced aggregation needs more control.

---

# 10. Dynamic Queries

Optional filters:

```text
name
difficulty
minPrice
maxPrice
```

Examples:

```http
GET /mongo/courses/dynamic-search
```

```http
GET /mongo/courses/dynamic-search?difficulty=MEDIUM
```

```http
GET /mongo/courses/dynamic-search?minPrice=100&maxPrice=160
```

```http
GET /mongo/courses/dynamic-search?difficulty=MEDIUM&maxPrice=160&page=0&size=2&sort=price,asc
```

Criteria:

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

This avoids creating a repository method for every filter combination.

---

# 11. Compound Index

```java
@CompoundIndex(
    name = "difficulty_price_idx",
    def = "{'difficulty': 1, 'price': 1}"
)
```

Automatic index creation:

```properties
spring.data.mongodb.auto-index-creation=true
```

Index design should follow real query patterns.

Compound index field order matters.

---

# 12. explain()

```javascript
db.courses.find({
    difficulty: "MEDIUM",
    price: { $lt: 160 }
}).explain("executionStats")
```

Useful fields:

```text
IXSCAN
COLLSCAN
totalKeysExamined
totalDocsExamined
nReturned
executionTimeMillis
```

`IXSCAN` means index scan.

`COLLSCAN` means collection scan.

The goal is to understand whether the execution plan is appropriate, not blindly force indexes.

---

# 13. Aggregation Pipeline

Mongo shell:

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

Spring Data:

```java
Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(
                Criteria.where("price").gte(100)
        ),
        Aggregation.group("difficulty")
                .avg("price").as("averagePrice")
                .count().as("courseCount"),
        Aggregation.project()
                .and("_id").as("difficulty")
                .and("averagePrice").as("averagePrice")
                .and("courseCount").as("courseCount")
                .andExclude("_id"),
        Aggregation.sort(
                Sort.Direction.DESC,
                "averagePrice"
        )
);
```

Pipeline:

```text
$match
   ↓
$group
   ↓
$project
   ↓
$sort
```

Result:

```java
CourseDifficultyStatsDto
```

---

# 14. $lookup

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

```text
courses._id
      │
      │ matches
      ▼
enrollments.courseId
```

Result:

```java
CourseWithEnrollmentsDto
```

In this use case it behaves similarly to a relational LEFT OUTER JOIN.

Important BSON lesson:

```text
ObjectId ≠ String
```

Matching fields must have compatible types.

---

# 15. Embedded vs Referenced

Embedded:

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

Referenced:

```text
courses
    ↓
courseId
    ↓
enrollments
```

Decision factors:

- Read patterns
- Update patterns
- Ownership
- Sharing
- Document size
- Data growth

---

# 16. MongoDB Transactions

Transactions require a replica set or sharded cluster.

Local setup:

```text
Replica Set: rs0

127.0.0.1:27017
       │
       ▼
    PRIMARY
```

Rollback example:

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

Expected:

```text
Write
  ↓
RuntimeException
  ↓
Rollback
  ↓
No persisted course
```

Multi-collection transaction:

```text
Transaction
    │
    ├── courses
    └── enrollments
```

If the transaction fails, both writes roll back.

---

# 17. MongoDB Endpoints

CRUD:

```text
GET    /mongo/courses
GET    /mongo/courses/{id}
POST   /mongo/courses
PUT    /mongo/courses/{id}
DELETE /mongo/courses/{id}
```

Queries:

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

Create:

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

Dynamic search:

```http
GET /mongo/courses/dynamic-search?difficulty=MEDIUM&minPrice=100&maxPrice=160&page=0&size=2&sort=price,asc
```

Aggregation:

```http
GET /mongo/courses/difficulty-stats
```

Lookup:

```http
GET /mongo/courses/with-enrollments
```

---

# 18. PostgreSQL vs MongoDB

| PostgreSQL | MongoDB |
|---|---|
| Table | Collection |
| Row | Document |
| Column | Field |
| `@Entity` | `@Document` |
| JPA Repository | MongoRepository |
| JPQL | Mongo query |
| JOIN | `$lookup` |
| Relational model | Document model |

---

# 19. Interview Takeaways

**MongoRepository:** high-level repository abstraction.

**MongoTemplate:** more explicit control for dynamic queries/aggregations.

**Index:** extra write/storage cost in exchange for faster relevant reads.

**explain():** inspect execution plan and work performed.

**Embed vs reference:** data modeling decision based on access/update patterns.

**Transactions:** require replica-set or sharded-cluster support.

---

[Back to README](../README.md)
