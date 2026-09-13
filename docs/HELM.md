# Helm

This document owns Helm learning.

Kubernetes fundamentals belong in `KUBERNETES.md`.
Progress belongs only in `ROADMAP.md`.

---

# 1. Why Helm

Raw Kubernetes YAML can become repetitive.

```text
app-deployment.yaml
app-service.yaml
app-config.yaml
postgres-deployment.yaml
postgres-service.yaml
mongo-deployment.yaml
mongo-service.yaml
...
```

Helm adds:

- Packaging
- Templating
- Values
- Release management
- Upgrade history
- Rollback

---

# 2. Helm Does Not Replace Kubernetes

```text
Helm
  │
  ▼
Render Kubernetes YAML
  │
  ▼
Kubernetes API
  │
  ▼
Deployments / Services / Pods / ...
```

Kubernetes still runs the actual workloads.

---

# 3. Chart Structure

Project chart:

```text
helm/climbing-management/
```

Core structure:

```text
helm/
└── climbing-management/
    ├── Chart.yaml
    ├── values.yaml
    └── templates/
```

---

# 4. Chart.yaml

Example:

```yaml
apiVersion: v2
name: climbing-management
description: Helm chart for the Climbing Management API
type: application
version: 0.1.0
appVersion: "1.0.0"
```

```text
version
   ↓
Chart package version

appVersion
   ↓
Application version metadata
```

---

# 5. values.yaml

Example:

```yaml
replicaCount: 2

image:
  repository: ghcr.io/rmarintech/climbing-management-sb
  tag: latest
  pullPolicy: IfNotPresent

service:
  type: ClusterIP
  port: 8080
```

Flow:

```text
values.yaml
    │
    ▼
Templates
    │
    ▼
Rendered Kubernetes YAML
```

---

# 6. Template Example

Template:

```yaml
replicas: {{ .Values.replicaCount }}
```

Value:

```yaml
replicaCount: 2
```

Rendered output:

```yaml
replicas: 2
```

---

# 7. Release

```text
Chart + Values
      ↓
helm install
      ↓
Release
      ↓
Kubernetes resources
```

A release has revision history.

---

# 8. Lint

```bash
helm lint ./helm/climbing-management
```

Useful before deploying.

---

# 9. Render Without Installing

```bash
helm template climbing-management ./helm/climbing-management
```

Inspect:

- Names
- Labels/selectors
- Image tags
- Config
- Resources
- Ingress
- Namespace-related output

Workflow:

```text
Edit
  ↓
helm lint
  ↓
helm template
  ↓
Inspect
  ↓
Install/upgrade
```

---

# 10. Install

```bash
helm install climbing-management ./helm/climbing-management
```

Namespace:

```bash
helm install climbing-management ./helm/climbing-management \
  --namespace <namespace> \
  --create-namespace
```

```text
Install
  ↓
Render
  ↓
Kubernetes API
  ↓
Release revision 1
```

---

# 11. List Releases

```bash
helm list
```

All namespaces:

```bash
helm list -A
```

---

# 12. Inspect Release

Values:

```bash
helm get values climbing-management
```

All computed values:

```bash
helm get values climbing-management --all
```

Manifest:

```bash
helm get manifest climbing-management
```

This answers:

> What did Helm actually install?

---

# 13. Upgrade

```bash
helm upgrade climbing-management ./helm/climbing-management
```

```text
Revision 1
   ↓
Change chart/values
   ↓
helm upgrade
   ↓
Revision 2
```

---

# 14. Upgrade or Install

Useful for automation:

```bash
helm upgrade --install climbing-management ./helm/climbing-management
```

```text
Release exists?
    │
    ├── yes → upgrade
    └── no  → install
```

---

# 15. Override Image Tag

```bash
helm upgrade --install climbing-management ./helm/climbing-management \
  --set image.tag=<commit-sha>
```

CI/CD connection:

```text
Git SHA
  ↓
Docker image tag
  ↓
Helm image.tag
  ↓
Deployment
  ↓
Exact immutable image
```

---

# 16. Environment Values

Possible structure:

```text
values.yaml
values-dev.yaml
values-staging.yaml
values-prod.yaml
```

Usage:

```bash
helm upgrade --install climbing-management ./helm/climbing-management \
  -f ./helm/climbing-management/values-prod.yaml
```

The templates remain shared; environment differences go into values.

---

# 17. History

```bash
helm history climbing-management
```

```text
Revision 1
    ↓
Revision 2
    ↓
Revision 3
```

---

# 18. Rollback

```bash
helm rollback climbing-management <revision>
```

Example:

```bash
helm rollback climbing-management 2
```

Concept:

```text
Current revision
      ↓
Problem
      ↓
Rollback
      ↓
Restore previous release configuration
```

---

# 19. Uninstall

```bash
helm uninstall climbing-management
```

---

# 20. Helm vs kubectl

Raw:

```text
kubectl apply -f ...
kubectl apply -f ...
kubectl apply -f ...
```

Helm:

```text
helm upgrade --install
        │
        ▼
Chart + Values
        │
        ▼
Rendered resources
        │
        ▼
Kubernetes
```

Still use `kubectl` for:

- Pods
- Logs
- Events
- Services
- Describe
- Runtime troubleshooting

---

# 21. Namespace-Aware Helm

```bash
helm list -n <namespace>
```

```bash
helm upgrade --install climbing-management ./helm/climbing-management \
  -n <namespace> \
  --create-namespace
```

Then inspect the same namespace:

```bash
kubectl get pods -n <namespace>
kubectl get services -n <namespace>
```

Checking the wrong namespace is a common source of confusion.

---

# 22. Troubleshooting

```bash
helm lint ./helm/climbing-management
helm template climbing-management ./helm/climbing-management
helm list -A
helm history climbing-management -n <namespace>
helm get values climbing-management -n <namespace>
helm get manifest climbing-management -n <namespace>
```

Then:

```bash
kubectl get pods -n <namespace>
kubectl describe pod <pod-name> -n <namespace>
kubectl logs <pod-name> -n <namespace>
```

Flow:

```text
Chart valid?
  ↓
helm lint

Rendered YAML valid?
  ↓
helm template

Values correct?
  ↓
helm get values

Installed manifest correct?
  ↓
helm get manifest

Runtime healthy?
  ↓
kubectl
```

---

# 23. Helm + CI/CD + Kubernetes

```text
Git commit
    ↓
GitHub Actions
    ↓
Build + Test
    ↓
Docker image
    ↓
GHCR:<commit-sha>
    ↓
Helm
    ↓
image.tag=<commit-sha>
    ↓
Kubernetes rolling update
```

This ties together the CI/CD, Docker, Helm and Kubernetes sections.

---

# 24. Interview Takeaways

**Chart:** package containing chart metadata, values and Kubernetes templates.

**Release:** installed chart instance with revision history.

**values.yaml:** configurable data.

**Template:** resource structure consuming values.

**Why Helm:** parameterization, packaging and release lifecycle.

**Helm vs kubectl:** Helm manages releases/templates; kubectl inspects and operates Kubernetes resources.

---

[Back to README](../README.md)
