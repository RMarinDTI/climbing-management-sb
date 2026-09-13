# CI/CD with GitHub Actions

This document owns the CI/CD learning material.

Progress belongs only in `ROADMAP.md`.

---

# 1. Overall Flow

```text
Continuous Integration
        ↓
Build + Test + Package
        ↓
Docker Image
        ↓
GHCR
        ↓
Continuous Delivery
        ↓
Deployment Approval
        ↓
Deployment
```

---

# 2. CI Pipeline

```text
Git push
    ↓
GitHub Actions
    ↓
Checkout
    ↓
Java 21
    ↓
PostgreSQL service
    ↓
MongoDB service
    ↓
Maven build + tests
    ↓
JAR
    ↓
Upload artifact
    ↓
Docker build
    ↓
Tag image
    ↓
Push GHCR
```

Workflow:

```text
.github/workflows/ci.yml
```

---

# 3. CI Service Containers

PostgreSQL:

```yaml
image: postgres:17
```

Database:

```text
climbing_management
```

MongoDB:

```yaml
image: mongo:8
```

Why?

The GitHub runner does not use the developer's local databases.

Healthchecks make CI wait for service readiness.

---

# 4. Maven Lifecycle

CI uses:

```bash
./mvnw package
```

Comparison:

```text
mvn test
    ↓
compile + tests

mvn package
    ↓
compile + test + JAR

mvn verify
    ↓
package + extra verification

mvn install
    ↓
package + local Maven repository install
```

---

# 5. JAR Artifact

```text
target/*.jar
        ↓
GitHub Actions Artifact
```

Name:

```text
climbing-management-jar
```

Distinction:

```text
JAR
 ↓
Application build artifact

Docker image
 ↓
Container deployment artifact
```

---

# 6. GitHub Container Registry

Repository:

```text
ghcr.io/rmarintech/climbing-management-sb
```

Mutable tag:

```text
ghcr.io/rmarintech/climbing-management-sb:latest
```

Immutable source-based tag:

```text
ghcr.io/rmarintech/climbing-management-sb:<commit-sha>
```

---

# 7. latest vs SHA

```text
latest
  ↓
Mutable reference

commit SHA
  ↓
Immutable reference to exact build source
```

Traceability:

```text
Git commit
    ↓
CI
    ↓
Docker image
    ↓
same commit SHA tag
```

---

# 8. GHCR Flow

```text
GitHub Repository
        │
        ▼
GitHub Actions
        │
        ├── Build
        ├── Test
        └── Package
              │
              ▼
         Docker Image
              │
        ┌─────┴─────┐
        ▼           ▼
      latest     commit SHA
        │           │
        └─────┬─────┘
              ▼
             GHCR
```

Authentication can use `GITHUB_TOKEN` with package write permission.

---

# 9. Continuous Delivery

```text
CI succeeds
     ↓
CD workflow
     ↓
Production environment
     ↓
Required approval
     ↓
Deployment
```

A GitHub Environment can enforce required reviewers.

Important concept:

> A successful build is not automatically permission to deploy to production.

---

# 10. Build Once, Deploy Many

Bad:

```text
Build A
  ↓
Test A
  ↓
Rebuild
  ↓
Deploy B
```

Preferred:

```text
Build A
  ↓
Test A
  ↓
Deploy A
```

Environment differences should generally come from configuration rather than rebuilding the artifact.

---

# 11. Promotion

```text
Immutable Image
    │
    ├── Development
    ├── Staging
    └── Production
```

Promote the same tested image.

---

# 12. CI Troubleshooting

```text
Checkout?
  ↓
Java setup?
  ↓
Service containers healthy?
  ↓
Compile?
  ↓
Tests?
  ↓
JAR?
  ↓
Docker build?
  ↓
Registry auth?
  ↓
Push?
```

CD:

```text
CI succeeded?
  ↓
Image exists?
  ↓
Correct tag?
  ↓
Approval?
  ↓
Deployment credentials?
  ↓
Target environment healthy?
```

---

# 13. Interview Takeaways

**CI:** continuously build/test integrated changes.

**Continuous Delivery:** keep validated software releasable/deployable under controlled policy.

**Service containers:** reproducible external dependencies for CI tests.

**Immutable tags:** exact source-to-deployment traceability.

**Build once, deploy many:** promote the exact artifact that passed validation.

---

[Back to README](../README.md)
