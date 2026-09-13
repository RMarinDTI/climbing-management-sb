# PostgreSQL, Spring Data JPA and Hibernate

This document owns PostgreSQL persistence, Spring Data JPA, Hibernate, derived queries, JPQL, transactions, Persistence Context, Dirty Checking and locking.

Progress belongs only in `ROADMAP.md`.

---

# 1. Persistence Stack

```text
Java
 │
 ▼
@Entity
 │
 ▼
Spring Data JPA
 │
 ▼
Hibernate
 │
 ▼
JDBC
 │
 ▼
PostgreSQL
```

JPA is the specification. Hibernate is the ORM implementation used by the project.

---

# 2. Entity

```java
@Entity
public class CourseEntity {
    ...
}
```

Important distinction:

```text
Relational persistence
        ↓
@Entity

MongoDB persistence
        ↓
@Document
```

---

# 3. Repository Operations

Typical Spring Data operations:

```java
save(...)
findById(...)
findAll(...)
deleteById(...)
existsById(...)
count()
```

---

# 4. Derived Queries

```java
List<CourseEntity> findByDifficulty(Difficulty difficulty);
```

Flow:

```text
Repository method name
        ↓
Spring Data parses it
        ↓
Query generated
        ↓
Hibernate
        ↓
SQL
```

Use derived queries while the method name remains clear.

---

# 5. JPQL

JPQL queries entities and fields rather than tables and columns.

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

```text
SQL
 ↓
Tables + columns

JPQL
 ↓
Entities + fields
```

Hibernate translates JPQL to SQL.

---

# 6. Transactions

```java
@Transactional
public void updateCourse(...) {
    ...
}
```

Successful transaction:

```text
BEGIN
  operation A
  operation B
COMMIT
```

Failure:

```text
BEGIN
  operation A
  operation B
  ERROR
ROLLBACK
```

---

# 7. Propagation — REQUIRED

```text
Existing transaction?
    │
    ├── YES ─► Join it
    │
    └── NO  ─► Create one
```

`REQUIRED` is the normal default.

---

# 8. Propagation — REQUIRES_NEW

```text
Transaction A
     │
     ├── suspended
     │
     ▼
Transaction B
     │
  commit/rollback
     │
     ▼
Transaction A resumes
```

The inner transaction is independent.

That changes atomicity and should be used intentionally.

---

# 9. Rollback Rules

Unchecked exception:

```text
RuntimeException
      ↓
Rollback by default
```

Checked exception when explicit rollback is required:

```java
@Transactional(rollbackFor = Exception.class)
```

The important topic is understanding the transaction boundary, not merely memorizing the annotation.

---

# 10. Persistence Context

```text
Database
   │
   ▼
Entity loaded
   │
   ▼
Persistence Context
   │
   ▼
Managed Entity
```

Hibernate tracks managed entities.

---

# 11. Dirty Checking

A managed entity can be modified without an explicit `save()`:

```java
existing.setPrice(courseRecord.price());
```

Flow:

```text
Entity loaded
    ↓
Managed by Hibernate
    ↓
Field changed
    ↓
Transaction commit
    ↓
Hibernate detects change
    ↓
UPDATE generated
```

Interview takeaway:

> `save()` is not what causes every JPA update. Managed entity changes can be flushed through Dirty Checking.

---

# 12. Optimistic Locking

```java
@Version
private Long version;
```

Conceptual SQL:

```sql
UPDATE courses
SET ...,
    version = ?
WHERE id = ?
AND version = ?
```

Scenario:

```text
A reads version 5
B reads version 5

A writes
  ↓
version 6

B writes expecting version 5
  ↓
No matching version
  ↓
Conflict detected
```

Useful when conflicts are relatively uncommon.

```text
Allow concurrency
      ↓
Detect conflict on write
```

---

# 13. Pessimistic Locking

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

PostgreSQL/Hibernate generated SQL similar to:

```sql
SELECT ...
FROM courses
WHERE id = ?
FOR NO KEY UPDATE;
```

Scenario:

```text
Transaction A
    ↓
Locks row
    │
Transaction B
    ↓
Waits
    │
A commits
    ↓
Lock released
    ↓
B continues
```

Useful when conflicting modifications must be serialized.

---

# 14. Optimistic vs Pessimistic

| Optimistic | Pessimistic |
|---|---|
| Detect conflict | Prevent concurrent modification |
| Usually version field | DB row lock |
| No long lock | Holds lock |
| Good for rarer conflicts | Good when conflict likely |
| May fail at write | Other transaction waits |

---

# 15. API Endpoints

```text
GET     /jpa/courses
GET     /jpa/courses/{id}
POST    /jpa/courses
PUT     /jpa/courses/{id}
DELETE  /jpa/courses/{id}
```

Queries:

```text
GET /jpa/courses/most-expensive
GET /jpa/courses/difficulty/{difficulty}
GET /jpa/courses/difficulty/{difficulty}/price/{price}
```

---

# 16. Transaction Test Pattern

```text
1. Inspect initial DB state
2. Execute operation
3. Force failure after a write
4. Inspect DB
5. Confirm commit/rollback
```

Concurrency test:

```text
1. Start request A
2. Pause A inside transaction
3. Start request B
4. Observe conflict/blocking
5. Complete A
6. Observe B
7. Inspect final row/version
```

---

# 17. Interview Takeaways

**Dirty Checking:** Hibernate detects changes to managed entities.

**Persistence Context:** lifecycle/context in which entities are managed.

**Optimistic locking:** allow concurrency, detect stale writes.

**Pessimistic locking:** lock DB row and force competing transaction to wait.

**REQUIRED:** join existing transaction or create one.

**REQUIRES_NEW:** suspend current transaction and start independent one.

---

[Back to README](../README.md)
