KUBERNETES COMMANDS — CLIMBING MANAGEMENT
==========================================


1. KUBERNETES CONTEXT
   =====================

# Show the current Kubernetes context.
#
# A context determines which Kubernetes cluster and user
# kubectl commands are currently targeting.
kubectl config current-context

# Show all configured Kubernetes contexts.
kubectl config get-contexts

# Switch to a different Kubernetes context.
kubectl config use-context <context-name>


2. CLUSTER INFORMATION
   ======================

# Show basic information about the Kubernetes cluster.
kubectl cluster-info

# Show Kubernetes client and server versions.
kubectl version

# Show all Kubernetes nodes.
kubectl get nodes

# Show nodes together with additional information such as
# IP addresses, operating system and runtime.
kubectl get nodes -o wide

# Show detailed information about a node.
kubectl describe node <node-name>


3. NAMESPACES
   =============

# List all namespaces.
kubectl get namespaces

# Show resources in the default namespace.
kubectl get all -n default

# Show Pods in a specific namespace.
kubectl get pods -n <namespace>

# Show all Deployments in a specific namespace.
kubectl get deployments -n <namespace>


4. APPLICATION DEPLOYMENT
   =========================

# Apply the Spring Boot Kubernetes Deployment.
kubectl apply -f k8s/app-deployment.yaml

# Show the Deployment.
kubectl get deployment climbing-management

# Show all Deployments.
kubectl get deployments

# Show the Pods created by the Deployment.
kubectl get pods

# Show Pods together with their IP addresses and nodes.
kubectl get pods -o wide

# Show the Deployment hierarchy:
#
# Deployment → ReplicaSet → Pods
#
# This is useful for understanding how Kubernetes manages Pods.
kubectl get deployment,replicaset,pods

# Show detailed Deployment information.
kubectl describe deployment climbing-management

# Show the complete Deployment definition currently stored
# in the Kubernetes API server.
kubectl get deployment climbing-management -o yaml


5. DEPLOYMENT HIERARCHY
   =======================

# Kubernetes Deployments do not create Pods directly.
#
# The hierarchy is:
#
# Deployment
#     │
#     │ manages
#     ▼
# ReplicaSet
#     │
#     │ creates and maintains
#     ▼
# Pods
#
# The Deployment manages the desired state.
# The ReplicaSet ensures the required number of Pods exists.
# The Pods run the actual application containers.

kubectl get deployment,replicaset,pods


6. APPLICATION LOGS AND TROUBLESHOOTING
   =======================================

# Show the logs of a running application Pod.
kubectl logs <pod-name>

# Show the logs from the previous crashed container instance.
#
# Particularly useful with CrashLoopBackOff.
kubectl logs <pod-name> --previous

# Follow application logs in real time.
kubectl logs -f <pod-name>

# Show detailed information about a Pod.
#
# Useful for investigating:
#   - container state
#   - termination reason
#   - exit code
#   - events
#   - probes
#   - volumes
#   - configuration
kubectl describe pod <pod-name>

# Show the complete Pod definition currently stored by Kubernetes.
kubectl get pod <pod-name> -o yaml


7. CRASHLOOPBACKOFF TROUBLESHOOTING
   ===================================

# Show the current Pod status.
kubectl get pods

# Show why a Pod is failing.
kubectl describe pod <pod-name>

# Show logs from the current container.
kubectl logs <pod-name>

# Show logs from the previous crashed container.
kubectl logs <pod-name> --previous

# Show recent Kubernetes events.
kubectl get events --sort-by=.lastTimestamp

# Useful troubleshooting sequence:
#
# 1. Check Pod status
# 2. Describe the Pod
# 3. Check current logs
# 4. Check previous logs
# 5. Check cluster events

kubectl get pods
kubectl describe pod <pod-name>
kubectl logs <pod-name>
kubectl logs <pod-name> --previous
kubectl get events --sort-by=.lastTimestamp


8. CONFIGMAP VERIFICATION
   =========================

# Show all ConfigMaps.
kubectl get configmaps

# Show the contents and metadata of the application ConfigMap.
kubectl describe configmap climbing-management-config

# Show the ConfigMap as YAML.
kubectl get configmap climbing-management-config -o yaml


9. SECRET VERIFICATION
   ======================

# List Secrets without displaying their values.
kubectl get secrets

# Show Secret metadata and available keys.
kubectl describe secret climbing-management-secret

# Show the Secret definition.
#
# Secret values stored in "data" are Base64 encoded.
#
# IMPORTANT:
# Base64 is encoding, NOT encryption.
kubectl get secret climbing-management-secret -o yaml


10. VERIFY CONFIGURATION INSIDE A POD
    =====================================

# Show environment variables inside a running Pod.
kubectl exec <pod-name> -- env

# Show application-related environment variables.
#
# PowerShell syntax for Windows.
kubectl exec <pod-name> -- env | Select-String "APP_|SPRING_|LOG_LEVEL"

# Open an interactive shell inside the container.
kubectl exec -it <pod-name> -- sh

# Exit the container shell.
exit


11. VERIFY THE KUBERNETES SECRET VOLUME
    =======================================

# List files mounted under /run/secrets.
kubectl exec <pod-name> -- ls -la /run/secrets

# Show Secret filenames without displaying their values.
kubectl exec <pod-name> -- ls -l /run/secrets

# Verify that the expected Secret file exists.
kubectl exec <pod-name> -- test -f /run/secrets/spring.datasource.password

# Return success/failure without exposing the password.
kubectl exec <pod-name> -- sh -c "test -f /run/secrets/spring.datasource.password && echo 'Secret file exists' || echo 'Secret file missing'"


12. DEPLOYMENT ROLLOUT
    ======================

# Show the current rollout status.
kubectl rollout status deployment/climbing-management

# Show Deployment rollout history.
kubectl rollout history deployment/climbing-management

# Restart the Deployment.
kubectl rollout restart deployment/climbing-management

# Undo the latest Deployment revision.
kubectl rollout undo deployment/climbing-management

# Show the Deployment after a rollback.
kubectl get deployment climbing-management


13. UNDERSTANDING DEPLOYMENT ROLLOUTS
    ====================================

# Deployments normally use a RollingUpdate strategy.
#
# A rolling update gradually replaces old Pods with new Pods.
#
# Old Pods
#    ↓
# New Pods are created
#    ↓
# New Pods become Ready
#    ↓
# Old Pods are removed
#
# This allows Kubernetes to maintain application availability
# where the Deployment configuration permits it.

kubectl rollout status deployment/climbing-management
kubectl rollout history deployment/climbing-management
kubectl rollout undo deployment/climbing-management


14. SCALING
    ==========

# Manually scale the Deployment to three Pods.
kubectl scale deployment climbing-management --replicas=3

# Check the number of Pods.
kubectl get pods

# Manually scale back to two Pods.
kubectl scale deployment climbing-management --replicas=2

# Show desired and current replica counts.
kubectl get deployment climbing-management

# IMPORTANT:
#
# When an HPA manages the Deployment, manual scaling can
# be overridden by the HPA according to:
#
#   minReplicas
#   maxReplicas
#   CPU/memory metrics


15. SERVICE
    ==========

# Apply the application Service.
kubectl apply -f k8s/app-service.yaml

# Show all Services.
kubectl get services

# Show the application Service.
kubectl get service climbing-management-service

# Show detailed Service information.
kubectl describe service climbing-management-service

# Show EndpointSlices.
kubectl get endpointslices

# Show EndpointSlices belonging to the application Service.
#
# PowerShell: keep this command on one line.
kubectl get endpointslices -l kubernetes.io/service-name=climbing-management-service

# Show the Service definition including its selector.
kubectl get service climbing-management-service -o yaml


16. KUBERNETES SERVICE ARCHITECTURE
    ===================================

# A Service provides a stable network endpoint for a group of Pods.
#
# Pods are ephemeral:
# their IP addresses can change when Pods are recreated.
#
# A Service provides a stable abstraction in front of them.
#
# Architecture:
#
#                 Kubernetes cluster
#                        │
#                        ▼
#          ┌──────────────────────────┐
#          │ climbing-management      │
#          │ Service                  │
#          │ ClusterIP                │
#          └────────────┬─────────────┘
#                       │
#                  Service selector
#                       │
#             ┌─────────┼─────────┐
#             ▼         ▼         ▼
#          App Pod   App Pod   App Pod
#
# Service
#    ↓
# selector
#    ↓
# Pod labels
#    ↓
# EndpointSlice
#    ↓
# Pod IPs


17. SERVICE TROUBLESHOOTING
    ===========================

# If a Service exists but traffic does not reach any Pods,
# first check the Service selector.
kubectl describe service climbing-management-service

# Check the Service definition and selector.
kubectl get service climbing-management-service -o yaml

# Check Pods and their labels.
kubectl get pods --show-labels

# Check EndpointSlices.
kubectl get endpointslices

# Show EndpointSlices belonging to the Service.
#
# PowerShell: keep this command on one line.
kubectl get endpointslices -l kubernetes.io/service-name=climbing-management-service

# Important diagnostic model:
#
# Service
#    │
#    │ selector
#    ▼
# Pod labels
#
# If the selector matches:
#
# Service → EndpointSlice → Pod
#
# If the selector does NOT match:
#
# Service → no endpoints


18. PORT FORWARDING — SPRING BOOT
    ================================

# Temporarily expose the Kubernetes application locally.
#
# localhost:8080 → Kubernetes Service:8080
kubectl port-forward service/climbing-management-service 8080:8080

# Test the Spring Boot health endpoint.
Invoke-RestMethod http://localhost:8080/actuator/health

# Test liveness.
Invoke-RestMethod http://localhost:8080/actuator/health/liveness

# Test readiness.
Invoke-RestMethod http://localhost:8080/actuator/health/readiness


19. HEALTH PROBES
    ================

# Show the configured health probes.
kubectl describe pod <pod-name>

# Watch Pod status.
kubectl get pods -w

# Show application logs if a probe is failing.
kubectl logs <pod-name>


20. HEALTH PROBE CONCEPTS
    =========================

# READINESS PROBE
#
# Determines whether a Pod is ready to receive traffic.
#
# If readiness fails:
#
# Pod remains running
#       │
#       └── Service stops sending traffic to it
#
# The application is NOT necessarily restarted.


# LIVENESS PROBE
#
# Determines whether the application inside the container
# is still functioning correctly.
#
# If liveness repeatedly fails:
#
# Kubernetes can restart the container.


# STARTUP PROBE
#
# Useful for applications that require a long startup time.
#
# It gives the application time to start before
# liveness/readiness checks become relevant.


21. STORAGECLASS
    ===============

# Show all StorageClasses.
kubectl get storageclass

# Show detailed information about the standard StorageClass.
kubectl describe storageclass standard

# Show the StorageClass as YAML.
kubectl get storageclass standard -o yaml

# Show the reclaim policy.
kubectl get storageclass standard -o jsonpath="{.reclaimPolicy}"; echo

# Conceptually:
#
# StorageClass
#   ├── provisioner
#   ├── reclaimPolicy
#   └── volumeBindingMode


22. PERSISTENTVOLUMECLAIM
    =========================

# Show all PersistentVolumeClaims.
kubectl get pvc

# Show the PostgreSQL PVC.
kubectl get pvc postgres-pvc

# Show detailed PVC information.
kubectl describe pvc postgres-pvc

# Show PVC together with its bound PV.
kubectl get pvc,pv

# Show Pods, PVCs and PVs together.
kubectl get pods,pvc,pv


23. PERSISTENTVOLUME
    ====================

# Show all PersistentVolumes.
kubectl get pv

# Show detailed information about a PV.
kubectl describe pv <pv-name>

# Show the PV as YAML.
kubectl get pv <pv-name> -o yaml


24. POSTGRESQL PERSISTENT STORAGE ARCHITECTURE
    ==============================================

# Provisioning:
#
# PVC → StorageClass → PV
#
# The PVC requests storage.
# The StorageClass defines how storage should be provisioned.
# Kubernetes dynamically provisions a PV.

# Consumption:
#
# Pod → PVC → PV
#
# The Pod consumes the PVC.
# The PVC is bound to the PV.
# PostgreSQL mounts the volume.

# Complete architecture:
#
#             PVC
#              │
#              ▼
#         StorageClass
#              │
#              ▼
#             PV
#              │
#              ▼
#             PVC
#              │
#              ▼
#        PostgreSQL Pod
#              │
#              ▼
# /var/lib/postgresql/data


25. POSTGRESQL DEPLOYMENT
    =========================

# Apply PostgreSQL Deployment.
kubectl apply -f k8s/postgres-deployment.yaml

# Show PostgreSQL Deployment.
kubectl get deployment postgres

# Show PostgreSQL Pods.
kubectl get pods -l app=postgres

# Show detailed PostgreSQL Pod information.
kubectl describe pod <postgres-pod-name>

# Check PostgreSQL rollout.
kubectl rollout status deployment/postgres


26. VERIFY POSTGRESQL PVC MOUNT
    ================================

# Show PostgreSQL Pod YAML.
kubectl get pod <postgres-pod-name> -o yaml

# Inspect the volume mounts.
kubectl describe pod <postgres-pod-name>

# Open a shell inside PostgreSQL.
kubectl exec -it <postgres-pod-name> -- bash

# Check the PostgreSQL data directory.
ls -la /var/lib/postgresql/data

# Exit.
exit


27. STORAGE VERIFICATION
    ========================

# Show storage resources.
kubectl get pvc,pv

# Show PostgreSQL Pods.
kubectl get pods -l app=postgres

# Delete the PostgreSQL Pod.
#
# The Deployment recreates it.
kubectl delete pod <postgres-pod-name>

# Check the new Pod.
kubectl get pods -l app=postgres

# Check the PVC.
kubectl get pvc postgres-pvc

# The data should survive Pod recreation because
# the data is stored in persistent storage rather than
# the container filesystem.


28. RECLAIM POLICY
    ==================

# Show reclaim policies.
kubectl get pv

# Typical policies:
#
# Retain
# Delete
#
# DELETE:
# The storage resource may be deleted when the PVC is deleted.
#
# RETAIN:
# The underlying storage is preserved and requires
# manual intervention/recovery.


29. VOLUME ACCESS MODES
    =======================

# Common access modes:
#
# ReadWriteOnce (RWO)
# -------------------
# Volume can be mounted read-write by one node.
#
# ReadOnlyMany (ROX)
# ------------------
# Volume can be mounted read-only by multiple nodes.
#
# ReadWriteMany (RWX)
# -------------------
# Volume can be mounted read-write by multiple nodes.
#
# Important:
# The actual supported access modes depend on the storage
# backend/provisioner.


30. RESOURCE REQUESTS AND LIMITS
    ================================

# Show Pod resource configuration.
kubectl describe pod <pod-name>

# Show resource information.
kubectl get pod <pod-name> -o yaml

# Concept:
#
# requests = resources Kubernetes reserves for scheduling
#
# limits = maximum resources the container can consume
#
# Example:
#
# requests:
#   cpu: 250m
#   memory: 512Mi
#
# limits:
#   cpu: 500m
#   memory: 1Gi
#
# CPU:
#
# 1000m = 1 CPU core
# 500m  = 0.5 CPU
# 250m  = 0.25 CPU


31. METRICS SERVER
    ==================

# Check whether Metrics Server is installed.
kubectl get deployment metrics-server -n kube-system

# Check Metrics Server Pod.
kubectl get pods -n kube-system -l k8s-app=metrics-server

# Check node metrics.
kubectl top nodes

# Check Pod metrics.
kubectl top pods

# Check application Pod CPU/memory.
kubectl top pods -l app=climbing-management

# Metrics Server provides resource usage metrics to Kubernetes.
#
# HPA can consume these metrics to make scaling decisions.


32. HORIZONTAL POD AUTOSCALER
    =============================

# Show all HPAs.
kubectl get hpa

# Show the application HPA.
kubectl get hpa climbing-management-hpa

# Show detailed HPA information.
kubectl describe hpa climbing-management-hpa

# Show HPA YAML.
kubectl get hpa climbing-management-hpa -o yaml

# Example configuration:
#
# minReplicas: 2
# maxReplicas: 5
# targetCPUUtilizationPercentage: 70
#
# The HPA manages the Deployment's desired replica count.

# Current CPU target:
#
# CPU request = 250m
# Target = 70%
#
# 250m × 70% = 175m
#
# A Pod consuming around 175m CPU is at approximately
# 70% CPU utilization relative to its request.


33. HPA LOAD TESTING
    ====================

# Watch the HPA.
kubectl get hpa -w

# Watch Pods.
kubectl get pods -w

# Watch CPU usage.
kubectl top pods -l app=climbing-management

# Generate load from a temporary Pod.
kubectl run load-generator `
  --rm -it `
--restart=Never `
  --image=busybox `
-- /bin/sh

# From inside the temporary Pod, repeatedly call the application.
while true; do wget -q -O- http://climbing-management-service:8080/actuator/health; done

# Alternatively, use a dedicated load-testing tool/container
# when more realistic traffic is required.

# Expected behavior:
#
# CPU increases
#     ↓
# HPA observes CPU usage
#     ↓
# desired replicas increase
#     ↓
# Deployment creates more Pods
#     ↓
# Service distributes traffic across Pods


34. HPA INTERVIEW MENTAL MODEL
    ==============================

# HPA does NOT create Pods directly.
#
# HPA:
#     ↓
# changes Deployment replica count
#     ↓
# Deployment
#     ↓
# ReplicaSet
#     ↓
# Pods
#
# Example:
#
# Current:
#   2 replicas
#   CPU = 146%
#   Target = 70%
#
# Approximation:
#
# desiredReplicas =
#     currentReplicas × currentUtilization / targetUtilization
#
# 2 × 146 / 70 ≈ 4.17
#
# Kubernetes rounds according to its autoscaling algorithm
# and respects min/max limits.
#
# If maxReplicas = 5:
#
# desired replicas cannot exceed 5.


35. DRY RUN
    ==========

# Validate a manifest without actually applying it.
kubectl apply -f k8s/app-deployment.yaml --dry-run=client

# Show what Kubernetes would create.
kubectl apply -f k8s/app-deployment.yaml --dry-run=client -o yaml

# Useful before committing Kubernetes manifests.


36. POSTGRESQL ROLLOUT
    ======================

# Check PostgreSQL rollout.
kubectl rollout status deployment/postgres

# Check PostgreSQL Pod.
kubectl get pods -l app=postgres

# Check PostgreSQL logs.
kubectl logs deployment/postgres

# Describe PostgreSQL Deployment.
kubectl describe deployment postgres


37. RESOURCE INSPECTION
    =======================

# Show all resources in the default namespace.
kubectl get all

# Show resources with labels.
kubectl get all --show-labels

# Show application-related resources.
kubectl get deployment,service,pods,configmap,secret

# Show storage resources.
kubectl get pvc,pv,storageclass

# Show autoscaling resources.
kubectl get hpa


38. GENERAL DIAGNOSTICS
    =======================

# Pods.
kubectl get pods

# Pods with more information.
kubectl get pods -o wide

# Deployments.
kubectl get deployments

# Services.
kubectl get services

# EndpointSlices.
kubectl get endpointslices

# Events.
kubectl get events --sort-by=.lastTimestamp

# Resource usage.
kubectl top pods
kubectl top nodes

# Complete overview.
kubectl get all


39. COMBINED COMMANDS
    =====================

# Application overview.
kubectl get deployment,service,pods

# Application + storage.
kubectl get deployment,service,pods,pvc,pv

# Application + autoscaling.
kubectl get deployment,service,pods,hpa

# Complete application overview.
kubectl get deployment,service,pods,configmap,secret,hpa,pvc


40. CLEANUP APPLICATION
    =======================

# Delete the application Deployment.
kubectl delete deployment climbing-management

# Delete the application Service.
kubectl delete service climbing-management-service

# Delete application ConfigMap.
kubectl delete configmap climbing-management-config

# Delete application Secret.
kubectl delete secret climbing-management-secret

# Delete HPA.
kubectl delete hpa climbing-management-hpa

# Delete application Ingress.
kubectl delete ingress climbing-management-ingress


41. CLEANUP POSTGRESQL
    ======================

# Delete PostgreSQL Deployment.
kubectl delete deployment postgres

# Delete PostgreSQL Service.
kubectl delete service postgres

# Delete PostgreSQL PVC.
kubectl delete pvc postgres-pvc

# IMPORTANT:
#
# Deleting a PVC may trigger the StorageClass reclaim policy.
#
# If the PV uses "Delete", the underlying dynamically
# provisioned storage may also be deleted.


42. KUBERNETES ARCHITECTURE — APPLICATION
    =========================================

# High-level application architecture:
#
#                  Ingress
#                     │
#                     ▼
#             Ingress Controller
#                     │
#                     ▼
#                Service
#                     │
#              EndpointSlice
#                     │
#          ┌──────────┼──────────┐
#          ▼          ▼          ▼
#        Pod        Pod        Pod
#         │          │          │
#         └──────────┼──────────┘
#                    │
#              Spring Boot
#                 Docker
#                 container


43. KUBERNETES ARCHITECTURE — POSTGRESQL
    ========================================

# PostgreSQL architecture:
#
# Spring Boot Pods
#       │
#       ▼
# PostgreSQL Service
#       │
#       ▼
# PostgreSQL Pod
#       │
#       ▼
# PostgreSQL container
#       │
#       ▼
# volumeMount
#       │
#       ▼
# PVC
#       │
#       ▼
# PV
#       │
#       ▼
# StorageClass / storage backend


44. DIAGNOSTIC MENTAL MODELS
    ============================

# APPLICATION DOES NOT START
#
# Pod
#  ↓
# describe
#  ↓
# logs
#  ↓
# previous logs
#  ↓
# events


# SERVICE DOES NOT WORK
#
# Service
#  ↓
# selector
#  ↓
# Pod labels
#  ↓
# EndpointSlice
#  ↓
# Pod IP
#  ↓
# application port


# POD DOES NOT RECEIVE TRAFFIC
#
# readiness probe
#  ↓
# EndpointSlice
#  ↓
# Service
#
# A Pod that is not Ready should not receive normal
# Service traffic.


# DATABASE DATA DISAPPEARS
#
# Check:
#
# Pod
#  ↓
# volumeMount
#  ↓
# PVC
#  ↓
# PV
#  ↓
# StorageClass
#
# The container filesystem itself is ephemeral.


# APPLICATION DOES NOT SCALE
#
# Check:
#
# HPA
#  ↓
# Metrics Server
#  ↓
# Pod metrics
#  ↓
# resource requests
#
# HPA CPU utilization is calculated relative to
# the CPU request.


45. MOST IMPORTANT INTERVIEW CONCEPTS
    =====================================

# POD
#
# Smallest deployable unit in Kubernetes.
# Usually contains one main application container.


# DEPLOYMENT
#
# Manages stateless application Pods.
# Controls desired replica count and rollout strategy.


# REPLICASET
#
# Ensures the desired number of Pod replicas exists.
# Usually managed automatically by a Deployment.


# SERVICE
#
# Provides a stable network endpoint for Pods.


# ENDPOINTSLICE
#
# Stores the actual network endpoints behind a Service.


# CONFIGMAP
#
# Stores non-sensitive configuration.


# SECRET
#
# Stores sensitive configuration.
#
# IMPORTANT:
# Base64 encoding is not encryption.


# PVC
#
# PersistentVolumeClaim.
# Requests persistent storage.


# PV
#
# PersistentVolume.
# Represents persistent storage available to Kubernetes.


# STORAGECLASS
#
# Defines how persistent storage can be dynamically provisioned.


# HPA
#
# Horizontal Pod Autoscaler.
# Adjusts Deployment replica count based on metrics.


# METRICS SERVER
#
# Provides resource usage metrics to Kubernetes.


# INGRESS
#
# Provides Layer 7 HTTP/HTTPS routing into the cluster.


# INGRESS CONTROLLER
#
# Actual component that implements Ingress behavior.
#
# In this project:
# NGINX Ingress Controller.


46. ESSENTIAL KUBERNETES INTERVIEW COMMAND SET
    =============================================

kubectl get pods
kubectl get pods -o wide
kubectl describe pod <pod>
kubectl logs <pod>
kubectl logs <pod> --previous

kubectl get deployments
kubectl describe deployment <deployment>
kubectl rollout status deployment/<deployment>
kubectl rollout history deployment/<deployment>
kubectl rollout undo deployment/<deployment>

kubectl get services
kubectl describe service <service>

kubectl get endpointslices
kubectl get endpointslices -l kubernetes.io/service-name=<service>

kubectl get configmaps
kubectl get secrets

kubectl get pvc
kubectl get pv
kubectl get storageclass

kubectl get hpa
kubectl describe hpa <hpa>

kubectl top pods
kubectl top nodes

kubectl get events --sort-by=.lastTimestamp

kubectl port-forward service/<service> 8080:8080

kubectl get ingress
kubectl describe ingress <ingress>


47. CORE KUBERNETES MENTAL MODEL
    ================================

# Kubernetes continuously compares:
#
# DESIRED STATE
#      vs
# CURRENT STATE
#
# Example:
#
# Desired:
#   3 replicas
#
# Current:
#   2 replicas
#
# Kubernetes detects the difference.
#
# Deployment / ReplicaSet creates another Pod.
#
# Current state becomes:
#   3 replicas
#
# This reconciliation loop is one of the most important
# concepts in Kubernetes.


48. TEMPORARY POSTGRESQL CLIENT POD
    ===================================

# Create a temporary PostgreSQL client Pod.
#
# Useful for testing PostgreSQL Service networking
# from inside the Kubernetes cluster.
kubectl run postgres-client `
  --rm -it `
--restart=Never `
  --image=postgres:17 `
-- bash

# Once inside the temporary Pod:
#
# psql syntax:
#
# psql -h <service-name> -U <username> -d <database>

# Example:
psql -h postgres -U postgres -d climbing_management

# Exit psql:
\q

# Exit the container:
exit


49. KUBERNETES SERVICE NETWORKING VERIFICATION
    ==============================================

# Start a temporary PostgreSQL client.
kubectl run postgres-client `
  --rm -it `
--restart=Never `
  --image=postgres:17 `
-- bash

# From inside the client Pod:
psql -h postgres -U postgres -d climbing_management

# The important concept:
#
# The PostgreSQL client does NOT need the PostgreSQL Pod IP.
#
# It uses:
#
# postgres
#    ↓
# Kubernetes DNS
#    ↓
# postgres Service
#    ↓
# EndpointSlice
#    ↓
# PostgreSQL Pod


50. COMPLETE LOCAL KUBERNETES NETWORKING MENTAL MODEL
    =====================================================

# Application request:
#
# Browser / curl
#       │
#       ▼
# Ingress
#       │
#       ▼
# Ingress Controller
#       │
#       ▼
# Service
#       │
#       ▼
# EndpointSlice
#       │
#       ▼
# Spring Boot Pod
#       │
#       ▼
# Spring Boot container
#       │
#       ▼
# PostgreSQL Service
#       │
#       ▼
# EndpointSlice
#       │
#       ▼
# PostgreSQL Pod
#       │
#       ▼
# PostgreSQL container
#       │
#       ▼
# Persistent Volume


51. METRICS SERVER / HPA QUICK REFERENCE
    ========================================

# Metrics Server:
kubectl get pods -n kube-system -l k8s-app=metrics-server
kubectl top nodes
kubectl top pods

# HPA:
kubectl get hpa
kubectl describe hpa climbing-management-hpa

# Watch HPA:
kubectl get hpa -w

# Watch Pods:
kubectl get pods -w

# Watch CPU:
kubectl top pods -l app=climbing-management

# HPA architecture:
#
# Metrics Server
#       │
#       ▼
# CPU / memory metrics
#       │
#       ▼
# HPA
#       │
#       ▼
# Deployment replicas
#       │
#       ▼
# ReplicaSet
#       │
#       ▼
# Pods


52. COMPLETE LOCAL CLEANUP
    ===========================

# WARNING:
#
# These commands remove the Kubernetes resources created
# during this course.
#
# Do not run them unless you intentionally want to reset
# the local Kubernetes environment.

# Application:
kubectl delete deployment climbing-management
kubectl delete service climbing-management-service
kubectl delete configmap climbing-management-config
kubectl delete secret climbing-management-secret
kubectl delete hpa climbing-management-hpa
kubectl delete ingress climbing-management-ingress

# PostgreSQL:
kubectl delete deployment postgres
kubectl delete service postgres
kubectl delete pvc postgres-pvc

# Check remaining resources:
kubectl get all
kubectl get pvc,pv
kubectl get ingress
kubectl get hpa


53. INGRESS
    ===========

# Ingress provides HTTP/HTTPS routing from outside the
# Kubernetes application layer to Services inside the cluster.
#
# Ingress is a Kubernetes API resource.
#
# IMPORTANT:
#
# Ingress itself does not process network traffic.
#
# An Ingress Controller is required to actually implement
# the routing rules.
#
# In this project:
#
# Ingress
#    ↓
# NGINX Ingress Controller
#    ↓
# Kubernetes Service
#    ↓
# Spring Boot Pods


# Check available IngressClasses.
kubectl get ingressclass

# An IngressClass identifies which Ingress Controller
# should process an Ingress resource.
#
# In this project:
#
# ingressClassName: nginx


# Install the NGINX Ingress Controller.
#
# Docker Desktop Kubernetes / local development:
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.13.3/deploy/static/provider/cloud/deploy.yaml


# Check the Ingress Controller Pods.
kubectl get pods -n ingress-nginx

# Check the Ingress Controller Services.
kubectl get svc -n ingress-nginx

# Check the IngressClass.
kubectl get ingressclass


# Apply the application Ingress.
kubectl apply -f k8s/app-ingress.yaml

# Show Ingress resources.
kubectl get ingress

# Show detailed Ingress information.
kubectl describe ingress climbing-management-ingress

# Show the Ingress definition as YAML.
kubectl get ingress climbing-management-ingress -o yaml


# Current application Ingress:
#
# climbing-management.local
#          │
#          │ /
#          ▼
# climbing-management-service:8080
#
# The corresponding manifest is:
#
# apiVersion: networking.k8s.io/v1
# kind: Ingress
#
# metadata:
#   name: climbing-management-ingress
#
# spec:
#   ingressClassName: nginx
#
#   rules:
#     - host: climbing-management.local
#
#       http:
#         paths:
#           - path: /
#             pathType: Prefix
#
#             backend:
#               service:
#                 name: climbing-management-service
#                 port:
#                   number: 8080


# INGRESS VS SERVICE
#
# Service:
#   Layer 4 networking
#   Stable endpoint for Pods
#   TCP/IP networking
#   Internal service discovery
#
# Ingress:
#   Layer 7 networking
#   HTTP/HTTPS routing
#   Host/path-based routing
#   Routes traffic to Services
#
# Example:
#
# https://api.example.com/courses
#                │
#                ▼
#             Ingress
#                │
#                ▼
#       climbing-management-service
#                │
#                ▼
#             Spring Boot


# INGRESS CONTROLLER
#
# The Ingress Controller watches Kubernetes Ingress resources
# and configures the actual reverse proxy/load balancer.
#
# In this project:
#
# Kubernetes Ingress
#        │
#        ▼
# NGINX Ingress Controller
#        │
#        ▼
# Kubernetes Service
#
# The NGINX Controller runs inside the ingress-nginx namespace.


# Show the NGINX Controller.
kubectl get pods -n ingress-nginx

# Show its Service.
kubectl get service -n ingress-nginx


# LOCAL DOCKER DESKTOP TESTING
#
# Docker Desktop Kubernetes may expose the Ingress Controller
# through an internal Docker/Kubernetes address that is not
# directly reachable from Windows.
#
# For local development, kubectl port-forward can provide
# a reliable bridge from Windows to the Ingress Controller.


# Forward Windows localhost:8081 to the NGINX HTTP port.
kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8081:80


# In another PowerShell terminal:
#
# The Host header is important because the Ingress rule uses:
#
# climbing-management.local
#
# Therefore the request must contain that Host header.
curl.exe -H "Host: climbing-management.local" http://localhost:8081/actuator/health


# Expected response:
#
# {"status":"UP"}
#
# This verifies the complete path:
#
# Windows
#    │
#    ▼
# localhost:8081
#    │
#    │ kubectl port-forward
#    ▼
# ingress-nginx-controller Service
#    │
#    ▼
# NGINX Ingress Controller
#    │
#    ▼
# climbing-management-ingress
#    │
#    ▼
# climbing-management-service:8080
#    │
#    ▼
# Spring Boot Pod
#    │
#    ▼
# /actuator/health


# TEST INGRESS FROM INSIDE KUBERNETES
#
# Start a temporary curl Pod.
kubectl run curl-test `
  --rm -it `
--restart=Never `
  --image=curlimages/curl:8.10.1 `
-- sh


# From inside the curl Pod:
curl -v -H "Host: climbing-management.local" http://ingress-nginx-controller.ingress-nginx.svc.cluster.local/actuator/health


# The request path is:
#
# curl-test Pod
#      │
#      ▼
# ingress-nginx-controller.ingress-nginx.svc.cluster.local
#      │
#      ▼
# NGINX Ingress Controller
#      │
#      ▼
# Ingress rule
#      │
#      ▼
# climbing-management-service
#      │
#      ▼
# Spring Boot Pod


# Exit the temporary curl Pod.
exit


# INGRESS TROUBLESHOOTING
#
# 1. Check the Ingress resource.
kubectl get ingress
kubectl describe ingress climbing-management-ingress

# 2. Check the IngressClass.
kubectl get ingressclass

# 3. Check the Ingress Controller Pods.
kubectl get pods -n ingress-nginx

# 4. Check the Ingress Controller Service.
kubectl get svc -n ingress-nginx

# 5. Check application Service.
kubectl get service climbing-management-service

# 6. Check EndpointSlices.
kubectl get endpointslices -l kubernetes.io/service-name=climbing-management-service

# 7. Check application Pods.
kubectl get pods

# 8. Check NGINX Controller logs.
kubectl logs -n ingress-nginx deployment/ingress-nginx-controller

# 9. Check application logs.
kubectl logs <application-pod>


# IMPORTANT INGRESS INTERVIEW MENTAL MODEL
#
# Ingress is a routing rule.
#
# Ingress Controller is the component that implements
# that routing.
#
# Service provides stable access to Pods.
#
# EndpointSlice contains the actual Pod endpoints.
#
# Therefore:
#
# Client
#   ↓
# Ingress Controller
#   ↓
# Ingress rule
#   ↓
# Service
#   ↓
# EndpointSlice
#   ↓
# Pod


# INGRESS VS GATEWAY API
#
# Ingress:
#   - Older Kubernetes HTTP routing API
#   - Simple and widely supported
#   - Host/path routing
#
# Gateway API:
#   - Newer Kubernetes networking API
#   - More expressive and extensible
#   - Separates infrastructure concerns from routing rules
#   - Supports richer traffic management
#
# Interview answer:
#
# "Ingress is the traditional Kubernetes API for HTTP/HTTPS
# routing into the cluster. Gateway API is the newer and more
# expressive successor designed to support more advanced
# traffic-management use cases."


# LOCAL DEVELOPMENT NOTE
#
# kubectl port-forward is only a development/testing mechanism.
#
# It is NOT normally used as a production ingress architecture.
#
# Production traffic would typically look more like:
#
# Internet
#    ↓
# Cloud Load Balancer
#    ↓
# Ingress Controller
#    ↓
# Ingress / Gateway
#    ↓
# Service
#    ↓
# Pods


# FINAL KUBERNETES NETWORKING ARCHITECTURE
#
#                         INTERNET / CLIENT
#                                │
#                                ▼
#                       Load Balancer / Ingress
#                                │
#                                ▼
#                    NGINX Ingress Controller
#                                │
#                                ▼
#                         Ingress Rule
#                                │
#                                ▼
#                    climbing-management-service
#                                │
#                                ▼
#                          EndpointSlice
#                                │
#                 ┌──────────────┼──────────────┐
#                 ▼              ▼              ▼
#              Pod 1           Pod 2           Pod 3
#                 │              │              │
#                 └──────────────┼──────────────┘
#                                │
#                         Spring Boot
#                                │
#                                ▼
#                       PostgreSQL Service
#                                │
#                                ▼
#                          EndpointSlice
#                                │
#                                ▼
#                         PostgreSQL Pod
#                                │
#                                ▼
#                            PVC / PV
#                                │
#                                ▼
#                       Persistent Storage


# END OF KUBERNETES COMMAND REFERENCE
#
# Topics covered:
#
# Kubernetes cluster/context
# Nodes
# Namespaces
# Pods
# Deployments
# ReplicaSets
# Services
# EndpointSlices
# ConfigMaps
# Secrets
# Health probes
# Rollouts
# Scaling
# PersistentVolumes
# PersistentVolumeClaims
# StorageClasses
# Resource requests/limits
# Metrics Server
# HPA
# Ingress
# NGINX Ingress Controller
# Kubernetes networking
# Persistent PostgreSQL
# Troubleshooting
# Kubernetes interview mental models