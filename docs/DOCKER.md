# Docker

This document owns Docker learning: images, containers, Compose, networking, volumes, multi-stage builds, hardening, secrets, healthchecks, troubleshooting and Trivy.

Progress belongs only in `ROADMAP.md`.

---

# 1. Big Picture

```text
                    Docker Compose
                         │
          ┌──────────────┼──────────────┐
          ▼              ▼              ▼
     Spring Boot     PostgreSQL       MongoDB
         │               │              │
      :8080            :5432          :27017
```

```text
Image
  ↓
Immutable template/package

Container
  ↓
Running instance of an image
```

---

# 2. Project Containers

```text
Images
│
├── Application image
├── postgres image
└── mongo image

Containers
│
├── app
├── postgres
└── mongo
```

---

# 3. Docker Networking

Compose DNS lets services use:

```text
postgres:5432
mongo:27017
```

```text
Spring Boot
     │
     ├── postgres:5432
     └── mongo:27017
```

Critical concept:

```text
Inside app container:

localhost
   ↓
the app container itself
```

It does not mean PostgreSQL or MongoDB.

---

# 4. Port Mapping

```text
Host port
   │
   ▼
Container port
```

Application:

```text
localhost:8080
      │
      ▼
app:8080
```

---

# 5. Volumes

```text
Database Container
       │
       ▼
    Volume
       │
       ▼
 Persistent Data
```

Container filesystems are disposable; persistent database data should not depend on the container writable layer.

---

# 6. Multi-Stage Dockerfile

```text
Build stage
    │
    ├── Maven
    ├── JDK 21
    ├── Dependencies
    └── Compilation
             │
             ▼
        application JAR
             │
             ▼
Runtime stage
    │
    ├── Java 21 runtime
    ├── Alpine Linux
    └── application JAR
```

Benefits:

- Smaller final image
- Fewer production tools
- Smaller attack surface

---

# 7. Build Cache

```text
pom.xml / Maven metadata
       ↓
Resolve dependencies
       ↓
Cached layer
       ↓
Copy source
       ↓
Compile
```

Dependency layers can be reused when source changes but dependencies do not.

---

# 8. Non-Root User

```dockerfile
RUN addgroup -S spring && adduser -S spring -G spring

USER spring
```

Runtime user:

```text
spring
```

This supports least privilege.

---

# 9. Read-Only Filesystem

```yaml
read_only: true
```

Writable temporary storage:

```yaml
tmpfs:
  - /tmp
```

---

# 10. Docker Secrets

```text
Docker Secret
     │
     ├──────────────► PostgreSQL
     └──────────────► Spring Boot
```

PostgreSQL:

```text
/run/secrets/postgres_password
```

Spring Boot:

```text
/run/secrets/spring.datasource.password
```

Import:

```properties
spring.config.import=optional:configtree:/run/secrets/
```

Mounted filename maps to the Spring property.

---

# 11. Docker Spring Profile

```yaml
SPRING_PROFILES_ACTIVE: docker
```

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/${APP_POSTGRES_DB}

spring.mongodb.uri=mongodb://mongo:27017/${APP_MONGO_DB}
```

Configuration:

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

Secret path:

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

# 12. Healthcheck

Actuator:

```http
GET /actuator/health
```

Example:

```json
{
    "groups": [
        "liveness",
        "readiness"
    ],
    "status": "UP"
}
```

Compose:

```yaml
healthcheck:
  test: ["CMD-SHELL", "wget -q --spider http://localhost:8080/actuator/health || exit 1"]
  interval: 10s
  timeout: 5s
  retries: 5
  start_period: 20s
```

`start_period` prevents normal startup time from causing immediate unhealthy state.

---

# 13. Liveness / Readiness / Startup

```text
Liveness
    ↓
Is the application alive?

Readiness
    ↓
Is it ready for traffic?

Startup
    ↓
Has initialization completed?
```

Kubernetes later uses these concepts as orchestration probes.

---

# 14. Troubleshooting Workflow

Commands:

```bash
docker compose ps
docker compose logs
docker compose exec
docker inspect
```

Flow:

```text
docker compose ps
        ↓
Running?
        ↓
docker compose logs
        ↓
What is failing?
        ↓
docker compose exec app sh
        ↓
Inspect from inside
        ↓
docker inspect
        ↓
Actual applied config/network?
```

DNS:

```bash
getent hosts postgres
getent hosts mongo
```

---

# 15. Deliberate Networking Failure

Correct:

```text
postgres
```

Deliberately changed to:

```text
localhost
```

Result:

```text
app container
    │
    ▼
localhost
    │
    ▼
app container
```

The app could not reach PostgreSQL because PostgreSQL is another container.

Diagnosis used logs, shell inspection, `docker inspect` and DNS lookup.

---

# 16. Trivy

```bash
trivy image climbing-management-sb-app:latest
```

Distinguish:

```text
Application dependencies
        vs
Base OS packages
```

A critical Tomcat dependency was traced:

```text
Spring Boot
    ↓
spring-boot-starter-tomcat
    ↓
tomcat-embed-core
```

After upgrading the vulnerable dependency and rebuilding, the relevant critical application finding was removed.

Workflow:

```text
Scan
  ↓
Identify severity
  ↓
Locate dependency
  ↓
Find fixed version
  ↓
Update
  ↓
Rebuild
  ↓
Rescan
```

---

# 17. Docker Compose Commands

Start:

```bash
docker compose up -d --build
```

Status:

```bash
docker compose ps
```

Logs:

```bash
docker compose logs app
```

Follow logs:

```bash
docker compose logs -f app
```

Shell:

```bash
docker compose exec app sh
```

Stop:

```bash
docker compose down
```

Application:

```text
http://localhost:8080
```

Health:

```text
http://localhost:8080/actuator/health
```

---

# 18. Docker vs Kubernetes

```text
Docker
   ↓
Build/run containers

Kubernetes
   ↓
Orchestrate containers
```

Kubernetes adds desired state, self-healing, services, rolling updates, autoscaling and ingress.

---

# 19. Interview Takeaways

**Image:** immutable package/template.

**Container:** running image instance.

**Compose service name:** DNS name for another service.

**Volume:** persistent data outside disposable container writable layer.

**Multi-stage build:** build tools excluded from final runtime image.

**Non-root/read-only:** defense in depth.

**Compose vs Kubernetes:** local orchestration vs full container orchestration platform.

---

[Back to README](../README.md)
