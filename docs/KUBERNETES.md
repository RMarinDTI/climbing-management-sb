# Kubernetes

This document owns Kubernetes learning.

Helm belongs in `HELM.md`.
Progress belongs only in `ROADMAP.md`.

---

# 1. Current Kubernetes Picture

```text
                    Kubernetes Cluster
                           │
             ┌─────────────┼─────────────┐
             │             │             │
             ▼             ▼             ▼
       Spring Boot     PostgreSQL      MongoDB
       Deployment      Deployment      Deployment
          │               │             │
       2+ Pods           1 Pod          1 Pod
          │               │             │
          ▼               ▼             ▼
       Service          Service       Service
       :8080            :5432         :27017
```

External flow:

```text
Client
   │
   ▼
NGINX Ingress Controller
   │
   ▼
Ingress rule
   │
   ▼
climbing-management-service
   │
   ├──► App Pod #1
   └──► App Pod #2
```

---

# 2. Cluster / Node / Pod / Container

```text
Cluster
  │
  ▼
Node
  │
  ▼
Pod
  │
  ▼
Container
```

Pod = smallest Kubernetes deployable unit.

---

# 3. Deployment / ReplicaSet / Pod

```text
Deployment
    │
    ▼
ReplicaSet
    │
    ▼
Pods
```

Desired-state example:

```text
Desired: 2
Actual: 1
    ↓
Controller reconciliation
    ↓
Replacement Pod
    ↓
Actual: 2
```

This is self-healing/desired-state management.

---

# 4. Replicas and HPA

Baseline:

```yaml
replicas: 2
```

HPA:

```text
Minimum: 2
Maximum: 5
CPU target: 70%
```

---

# 5. Service

```text
climbing-management-service:8080
            │
      ┌─────┴─────┐
      ▼           ▼
   App Pod      App Pod
    :8080        :8080
```

```yaml
type: ClusterIP
```

```yaml
selector:
  app: climbing-management
```

The Service provides stable access while Pods remain ephemeral.

---

# 6. EndpointSlices

Kubernetes dynamically tracks matching Ready Pods through EndpointSlices.

Conceptually:

```text
Service selector
      ↓
Ready matching Pods
      ↓
EndpointSlice
      ↓
Traffic destinations
```

A Pod failing readiness can disappear from Service endpoints without being deleted.

---

# 7. Service Discovery

```text
postgres:5432
mongo:27017
```

```text
Spring Boot Pod
      │
      ├── postgres:5432
      │       ↓
      │   PostgreSQL Service
      │       ↓
      │   PostgreSQL Pod
      │
      └── mongo:27017
              ↓
          MongoDB Service
              ↓
          MongoDB Pod
```

Service names resolve through Kubernetes DNS.

---

# 8. ConfigMap

Name:

```text
climbing-management-config
```

Contains values such as:

```text
SPRING_PROFILES_ACTIVE
APP_NAME
LOG_LEVEL
APP_MONGO_DB
APP_POSTGRES_USER
APP_POSTGRES_DB
```

Injection:

```yaml
envFrom:
  - configMapRef:
      name: climbing-management-config
```

---

# 9. Secret

Name:

```text
climbing-management-secret
```

Mounted password:

```text
/run/secrets/spring.datasource.password
```

Least privilege:

```yaml
automountServiceAccountToken: false
```

The app does not need Kubernetes API access.

---

# 10. Startup Probe

Question:

```text
Has application initialization completed?
```

Startup probes prevent slow startup from being confused with a dead application.

---

# 11. Readiness Probe

Question:

```text
Should this Pod receive traffic?
```

Failure:

```text
Pod
  ↓
Removed from Service endpoints
```

The container is not necessarily restarted.

---

# 12. Liveness Probe

Question:

```text
Should this container be restarted?
```

Repeated failure:

```text
Liveness failure
       ↓
Container restart
```

Interview summary:

> Startup protects initialization, readiness controls traffic, liveness controls restart behavior.

---

# 13. Rolling Updates

```text
Old version
   │
   ├── Pod A
   └── Pod B
        ↓
Rolling update
        ↓
   ┌────┴────┐
   ▼         ▼
New Pod   New Pod
```

Readiness prevents traffic from reaching a new Pod before it can serve requests.

---

# 14. Persistent Storage

```text
PostgreSQL Pod
      │
      ▼
PersistentVolumeClaim
      │
      ▼
PersistentVolume
      │
      ▼
StorageClass
      │
      ▼
Provisioned storage
```

```text
StorageClass
    ↓
How storage is provisioned

PV
    ↓
Provisioned storage

PVC
    ↓
Request for storage

Pod
    ↓
Mounts PVC
```

Access mode:

```text
ReadWriteOnce
```

Appropriate for the local single-node PostgreSQL learning setup.

---

# 15. Resource Requests and Limits

```text
CPU request:     250m
Memory request:  512Mi

CPU limit:       500m
Memory limit:    1Gi
```

```text
Request
   ↓
Scheduling baseline / reserved expectation

Limit
   ↓
Maximum allowed consumption
```

---

# 16. CPU and HPA

```text
CPU request = 250m
HPA target  = 70%

250m × 70% = 175m
```

About `175m` average CPU relative to the request corresponds to 70%.

---

# 17. Metrics Server

```bash
kubectl top nodes
kubectl top pods
```

```text
Kubelet
   │
   ▼
Metrics Server
   │
   ▼
Metrics API
   │
   ▼
HPA
```

---

# 18. HPA

```text
Target: climbing-management Deployment
Minimum replicas: 2
Maximum replicas: 5
CPU target: 70%
```

```text
Metrics Server
      │
      ▼
     HPA
      │
      ▼
Deployment replica count
      │
      ▼
Pods
```

The HPA changes Deployment replicas, not Pods directly.

---

# 19. Simplified HPA Calculation

```text
desired replicas =
current replicas × current utilization / target utilization
```

Example:

```text
2 × 146 / 70
≈ 4.17
```

Observed exercise:

```text
2 replicas
    ↓
4 replicas
    ↓
5 replicas
```

---

# 20. Ingress

```text
Client
   │
   ▼
NGINX Ingress Controller
   │
   ▼
Ingress rule
   │
   ▼
climbing-management-service:8080
   │
   ├──► Pod #1
   └──► Pod #2
```

Host:

```text
climbing-management.local
```

Manifest:

```text
k8s/app-ingress.yaml
```

---

# 21. Ingress vs Controller

```text
Ingress
   ↓
Routing declaration

Ingress Controller
   ↓
Component implementing routing
```

An Ingress resource requires a controller to actually handle traffic.

---

# 22. Local Ingress Testing

```bash
kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8081:80
```

```bash
curl.exe -H "Host: climbing-management.local" http://localhost:8081/actuator/health
```

Expected:

```text
up
```

Flow:

```text
Windows host
     │
     ▼
localhost:8081
     │
     ▼
port-forward
     │
     ▼
NGINX Ingress Controller
     │
     ▼
Ingress
     │
     ▼
Service
     │
     ▼
Spring Boot Pod
```

Port-forward is for development/debugging, not production architecture.

---

# 23. Local Service Testing

```bash
kubectl port-forward service/climbing-management-service 8080:8080
```

```text
localhost:8080
      │
      ▼
port-forward
      │
      ▼
Service
      │
      ├──► Pod #1
      └──► Pod #2
```

Endpoints:

```text
http://localhost:8080/actuator/health
http://localhost:8080/actuator/health/liveness
http://localhost:8080/actuator/health/readiness
```

---

# 24. Database Services

PostgreSQL:

```text
Deployment
   ↓
Service
   ↓
postgres:5432
   ↓
Pod
   ↓
PVC
```

MongoDB:

```text
Deployment
   ↓
Service
   ↓
mongo:27017
   ↓
Pod
```

Both database Services use `ClusterIP`.

---

# 25. Namespaces

```text
Kubernetes Cluster
│
├── development
├── staging
└── production
```

Namespaces provide logical organization/isolation inside a cluster.

They are not physically separate clusters.

---

# 26. Apply Raw Manifests

```bash
kubectl apply -f k8s/app-config.yaml
kubectl apply -f k8s/app-secret.yaml
```

```bash
kubectl apply -f k8s/postgres-pvc.yaml
```

```bash
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml
```

```bash
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml
```

```bash
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
```

---

# 27. Inspection Commands

```bash
kubectl get pods
kubectl get services
kubectl get endpointslices
kubectl get pvc
kubectl get pv
kubectl get storageclass
kubectl top pods
kubectl top nodes
kubectl get deployment climbing-management
kubectl get hpa
kubectl rollout status deployment/climbing-management
```

Troubleshooting:

```bash
kubectl describe pod <pod-name>
kubectl logs <pod-name>
kubectl logs -f <pod-name>
```

---

# 28. Troubleshooting Flow

```text
kubectl get pods
      ↓
Desired state?
      ↓
kubectl describe
      ↓
Events/probes/scheduling?
      ↓
kubectl logs
      ↓
Application error?
      ↓
Service + EndpointSlices
      ↓
Networking/selectors/readiness?
      ↓
PVC/PV
      ↓
Storage?
      ↓
kubectl top
      ↓
Resources?
```

---

# 29. Interview Takeaways

**Deployment:** manages desired rollout/replicas.

**ReplicaSet:** maintains Pod count.

**Service:** stable internal networking.

**Ingress:** HTTP routing to Services.

**Readiness:** traffic eligibility.

**Liveness:** restart decision.

**PVC:** workload storage request.

**PV:** provisioned storage.

**HPA:** changes workload replica count based on metrics.

---

[Back to README](../README.md)
