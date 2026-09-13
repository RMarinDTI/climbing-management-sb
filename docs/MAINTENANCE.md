# Documentation Maintenance

The goal is to preserve deep learning material without making every milestone require edits to many files.

---

# Golden Rule

Progress lives only in:

```text
docs/ROADMAP.md
```

Learning content lives in exactly one topic file:

```text
Spring / REST       → SPRING_BOOT_REST.md
PostgreSQL / JPA    → POSTGRESQL_JPA.md
MongoDB             → MONGODB.md
Docker              → DOCKER.md
CI/CD               → CICD.md
Kubernetes          → KUBERNETES.md
Helm                → HELM.md
```

Do not duplicate the detailed examples in `README.md`.

---

# Normal Milestone Update

Most milestones require only:

```text
1. Relevant topic file
2. ROADMAP.md
```

Example:

```text
New Kubernetes concept learned
        ↓
KUBERNETES.md
        +
ROADMAP.md
```

---

# When README.md Changes

Only when:

- Major project phase changes
- Tech stack changes
- Documentation navigation changes
- High-level architecture changes

A new query example does **not** require README changes.

---

# Recommended Topic Pattern

For a new concept:

```text
What is it?
   ↓
Why do we use it?
   ↓
Project example
   ↓
Diagram / flow
   ↓
How we tested it
   ↓
Common failure / troubleshooting
   ↓
Interview takeaway
```

This preserves learning value instead of reducing documentation to a checklist.

---

# Example Milestone

Suppose a future Microservices step adds service-to-service REST communication.

Update the future Microservices document with:

- Theory
- Diagram
- Code example
- Configuration
- Test request
- Failure scenario
- Interview takeaway

Then tick:

```md
- [x] Inter-service REST communication
```

in `ROADMAP.md`.

Nothing else is required unless the high-level architecture changed enough to justify a README update.

---

# Git Workflow

```bash
git status
git add .
git commit -m "docs: document <milestone>"
git push
```

When code and learning docs are committed together, prefer the implementation milestone as the commit message.

---

# Two Audiences

## Quick reviewer / recruiter

Read:

```text
README.md
ROADMAP.md
```

## Learning / interview preparation

Read the deep topic files.

This keeps the root readable while preserving the examples and explanations that make the project useful as a study notebook.

---

[Back to README](../README.md)
