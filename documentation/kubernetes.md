KUBERNETES COMMANDS — CLIMBING MANAGEMENT
==========================================


### 1. Kubernetes Context
=========================

# Show the current Kubernetes context.
#
# A context determines which Kubernetes cluster and user
# kubectl commands are currently targeting.
kubectl config current-context

# Show all configured Kubernetes contexts.
kubectl config get-contexts

# Switch to a different Kubernetes context.
kubectl config use-context <context-name>


### 2. Cluster Information
==========================

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


### 3. Namespaces
=================

# List all namespaces.
kubectl get namespaces

# Show resources in the default namespace.
kubectl get all -n default

# Show Pods in a specific namespace.
kubectl get pods -n <namespace>

# Show all Deployments in a specific namespace.
kubectl get deployments -n <namespace>


### 4. Application Deployment
============================

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


### 5. Deployment Hierarchy
===========================

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


### 6. Application Logs and Troubleshooting
===========================================

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


### 7. CrashLoopBackOff Troubleshooting
=======================================

# Show the current Pod status.
kubectl get pods

# Show why a Pod is failing.
kubectl describe pod <pod-name>

# Show logs from the current container.
kubectl logs <pod-name>

# Show logs from the previous crashed container.
#
# Particularly important when the container starts,
# crashes and Kubernetes restarts it.
kubectl logs <pod-name> --previous

# Show recent Kubernetes events.
#
# Events are useful for diagnosing:
#   - image pull errors
#   - failed mounts
#   - failed probes
#   - scheduling problems
#   - container startup failures
kubectl get events --sort-by=.lastTimestamp

# A useful troubleshooting sequence is:
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


### 8. ConfigMap Verification
============================

# Show all ConfigMaps.
kubectl get configmaps

# Show the contents and metadata of the application ConfigMap.
kubectl describe configmap climbing-management-config

# Show the ConfigMap as YAML.
kubectl get configmap climbing-management-config -o yaml


### 9. Secret Verification
==========================

# List Secrets without displaying their values.
kubectl get secrets

# Show Secret metadata and available keys.
#
# Secret values are intentionally not displayed.
kubectl describe secret climbing-management-secret

# Show the Secret definition.
#
# Values stored in "data" are Base64 encoded.
#
# IMPORTANT:
# Base64 is encoding, NOT encryption.
#
# Kubernetes Secrets provide a mechanism for handling
# sensitive configuration, but production environments
# may also require encryption at rest and appropriate
# RBAC/access controls.
kubectl get secret climbing-management-secret -o yaml


### 10. Verify Configuration Inside a Pod
=========================================

# Show environment variables inside a running Pod.
kubectl exec <pod-name> -- env

# Show only application-related environment variables.
#
# PowerShell syntax for Windows.
kubectl exec <pod-name> -- env | Select-String "APP_|SPRING_|LOG_LEVEL"

# Open an interactive shell inside the container.
kubectl exec -it <pod-name> -- sh

# Exit the container shell.
exit


### 11. Verify the Kubernetes Secret Volume
===========================================

# List files mounted under /run/secrets.
kubectl exec <pod-name> -- ls -la /run/secrets

# Show the Secret filenames without displaying their values.
kubectl exec <pod-name> -- ls -l /run/secrets

# Verify that the expected Secret file exists.
#
# The command only checks whether the file exists.
# It does not display the password.
kubectl exec <pod-name> -- test -f /run/secrets/spring.datasource.password

# Return success/failure without exposing the password.
kubectl exec <pod-name> -- sh -c "test -f /run/secrets/spring.datasource.password && echo 'Secret file exists' || echo 'Secret file missing'"


### 12. Deployment Rollout
==========================

# Show the current rollout status.
#
# This waits until Kubernetes reports that the Deployment
# has successfully completed its rollout.
kubectl rollout status deployment/climbing-management

# Show Deployment rollout history.
kubectl rollout history deployment/climbing-management

# Restart the Deployment.
#
# Kubernetes creates new Pods using the current Pod template.
kubectl rollout restart deployment/climbing-management

# Undo the latest Deployment revision.
kubectl rollout undo deployment/climbing-management

# Show the Deployment after a rollback.
kubectl get deployment climbing-management


### 13. Understanding Deployment Rollouts
=========================================

# Deployments normally use a RollingUpdate strategy.
#
# A rolling update gradually replaces old Pods with new Pods.
#
# Conceptually:
#
# Old Pods
#   ↓
# New Pods are created
#   ↓
# New Pods become Ready
#   ↓
# Old Pods are removed
#
# This allows Kubernetes to maintain application availability
# where the Deployment configuration permits it.

# Check rollout status.
kubectl rollout status deployment/climbing-management

# Check rollout history.
kubectl rollout history deployment/climbing-management

# Roll back to the previous revision.
kubectl rollout undo deployment/climbing-management


### 14. Scaling
==============

# Scale the Deployment to three Pods.
kubectl scale deployment climbing-management --replicas=3

# Check the number of Pods.
kubectl get pods

# Scale back to two Pods.
kubectl scale deployment climbing-management --replicas=2

# Show the desired and current replica counts.
kubectl get deployment climbing-management


### 15. Service
==============

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
kubectl get endpointslices \
-l kubernetes.io/service-name=climbing-management-service

# Show the Service definition including its selector.
kubectl get service climbing-management-service -o yaml


### 16. Kubernetes Service Architecture
=======================================

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
# The Service selects Pods using labels.
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


### 17. Service Troubleshooting
==============================

# If a Service exists but traffic does not reach any Pods,
# first check the Service selector.
kubectl describe service climbing-management-service

# Check the Service definition and selector.
kubectl get service climbing-management-service -o yaml

# Check the Pods and their labels.
kubectl get pods --show-labels

# Check EndpointSlices.
kubectl get endpointslices

# Show EndpointSlices belonging to the Service.
kubectl get endpointslices \
-l kubernetes.io/service-name=climbing-management-service

# Important diagnostic concept:
#
# Service
#    │
#    │ selector
#    ▼
# Pod labels
#
# If the selector matches the labels:
#
# Service → EndpointSlice → Pod
#
# If the selector does NOT match:
#
# Service → no endpoints


### 18. Port Forwarding — Spring Boot
=====================================

# Temporarily expose the Kubernetes application locally.
#
# localhost:8080 → Kubernetes Service:8080
kubectl port-forward service/climbing-management-service 8080:8080

# Test the Spring Boot health endpoint from Windows PowerShell.
Invoke-RestMethod http://localhost:8080/actuator/health

# Test the liveness endpoint.
Invoke-RestMethod http://localhost:8080/actuator/health/liveness

# Test the readiness endpoint.
Invoke-RestMethod http://localhost:8080/actuator/health/readiness


### 19. Health Probes
====================

# Show the configured health probes in the Pod definition.
kubectl describe pod <pod-name>

# Watch Pod status while probes are being evaluated.
kubectl get pods -w

# Show application logs if a probe is failing.
kubectl logs <pod-name>


### 20. Health Probe Concepts
============================

# Readiness Probe
#
# Determines whether a Pod is ready to receive traffic.
#
# If readiness fails:
#
# Pod remains running
#       │
#       └── Service stops sending traffic to it
#
# The application itself does not necessarily restart.

# Liveness Probe
#
# Determines whether the application inside the container
# is still functioning correctly.
#
# If liveness repeatedly fails:
#
# Kubernetes can restart the container.

# Startup Probe
#
# Useful for applications that require a long startup time.
#
# It gives the application time to start before
# liveness/readiness checks become relevant.


### 21. StorageClass
====================

# Show all StorageClasses.
kubectl get storageclass

# Show the default StorageClass.
kubectl get storageclass

# Show detailed information about the standard StorageClass.
kubectl describe storageclass standard

# Show the StorageClass as YAML.
kubectl get storageclass standard -o yaml

# Show the reclaim policy.
#
# Example result:
# Delete
kubectl get storageclass standard -o jsonpath="{.reclaimPolicy}"; echo

# The relevant StorageClass configuration may look conceptually like:
#
# StorageClass: standard
# │
# ├── provisioner: ...
# ├── reclaimPolicy: Delete
# └── volumeBindingMode: WaitForFirstConsumer


### 22. PersistentVolumeClaim
=============================

# Show all PersistentVolumeClaims.
kubectl get pvc

# Show the PostgreSQL PVC.
kubectl get pvc postgres-pvc

# Show detailed PVC information.
kubectl describe pvc postgres-pvc

# Show the PVC together with its bound PV.
kubectl get pvc,pv

# Show Pods, PVCs and PVs together.
kubectl get pods,pvc,pv


### 23. PersistentVolume
========================

# Show all PersistentVolumes.
kubectl get pv

# Show detailed information about a PV.
kubectl describe pv <pv-name>

# Show the PV as YAML.
kubectl get pv <pv-name> -o yaml


### 24. PostgreSQL Persistent Storage Architecture
==================================================

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
# The PostgreSQL container mounts the volume.

# Complete architecture:
#
#             PVC
#              │
#              │ requests storage
#              ▼
#         StorageClass
#              │
#              │ dynamically provisions
#              ▼
#             PV
#              │
#              │ is bound to
#              ▼
#             PVC
#              │
#              │ is consumed by
#              ▼
#        PostgreSQL Pod
#              │
#              │ volumeMount
#              ▼
# /var/lib/postgresql/data


### 25. PostgreSQL Deployment
============================

# Apply the PostgreSQL Deployment.
kubectl apply -f k8s/postgres-deployment.yaml

# Show the PostgreSQL Deployment.
kubectl get deployment postgres

# Show PostgreSQL Pods.
kubectl get pods -l app=postgres

# Show detailed PostgreSQL Pod information.
kubectl describe pod <postgres-pod-name>

# Check the PostgreSQL Deployment rollout.
kubectl rollout status deployment/postgres

# Show PostgreSQL logs.
kubectl logs <postgres-pod-name>


### 26. Verify PostgreSQL PVC Mount
===================================

# Show detailed information about the PostgreSQL Pod.
kubectl describe pod <postgres-pod-name>

# Under "Mounts", verify:
#
# /var/lib/postgresql/data from postgres-storage
#
# Under "Volumes", verify:
#
# postgres-storage:
#   Type: PersistentVolumeClaim
#   ClaimName: postgres-pvc


### 27. PostgreSQL Storage Verification
======================================

# Show Pods, PVC and PV together.
kubectl get pods,pvc,pv

# Expected relationship:
#
# PostgreSQL Pod
#      │
#      │ uses
#      ▼
# postgres-pvc
#      │
#      │ bound to
#      ▼
# dynamically-created PV


### 28. Reclaim Policy
======================

# Show the StorageClass reclaim policy.
kubectl get storageclass standard -o jsonpath="{.reclaimPolicy}"; echo

# Example:
#
# Delete
#
# The reclaimPolicy belongs to the StorageClass.
# It is not normally configured directly in the PVC.
#
# Delete means that when a dynamically provisioned PV
# is released, Kubernetes can delete the associated
# storage resource according to the provisioner's behavior.
#
# This is common for local development environments.

# Another possible policy is:
#
# Retain
#
# Retain preserves the PV/storage resource after release
# and requires manual handling.


### 29. Volume Access Modes
===========================

# ReadWriteOnce (RWO)
#
# The volume can be mounted as read-write by workloads
# on one node at a time.
#
# Suitable for our single-node local PostgreSQL setup.

# ReadOnlyMany (ROX)
#
# The volume can be mounted read-only by multiple nodes,
# where supported by the underlying storage.

# ReadWriteMany (RWX)
#
# The volume can be mounted read-write by multiple nodes,
# where supported by the underlying storage.
#
# Support depends on the storage backend.


### 30. Dry Run
==============

# Client-side dry run.
#
# Kubernetes does NOT send the request to the API server.
# kubectl validates the manifest locally.
#
# Useful for catching basic manifest/configuration problems
# before applying the resource.
kubectl apply --dry-run=client -f k8s/postgres-deployment.yaml

# Server-side dry run.
#
# The request is sent to the Kubernetes API server.
# The API server validates the request,
# but the resource is NOT persisted.
#
# This can perform validation using server-side knowledge
# that client-side validation does not have.
kubectl apply --dry-run=server -f k8s/postgres-deployment.yaml

# Interview distinction:
#
# --dry-run=client
#     → local validation
#
# --dry-run=server
#     → API server validation without persistence


### 31. Deployment Rollout Commands — PostgreSQL
================================================

# Check PostgreSQL Deployment rollout status.
kubectl rollout status deployment/postgres

# Show PostgreSQL Deployment rollout history.
kubectl rollout history deployment/postgres

# Roll back the PostgreSQL Deployment.
kubectl rollout undo deployment/postgres


### 32. Useful Resource Inspection Commands
===========================================

# Show a resource in table format.
kubectl get <resource-type> <resource-name>

# Show a resource with additional information.
kubectl get <resource-type> <resource-name> -o wide

# Show the complete resource definition as YAML.
kubectl get <resource-type> <resource-name> -o yaml

# Show detailed information and events for a resource.
kubectl describe <resource-type> <resource-name>

# Examples:
kubectl describe pod <pod-name>
kubectl describe deployment climbing-management
kubectl describe service climbing-management-service
kubectl describe pvc postgres-pvc


### 33. General Diagnostic Commands
===================================

# Show recent Kubernetes events.
kubectl get events --sort-by=.lastTimestamp

# Show the main workload and networking resources
# in the current namespace.
#
# IMPORTANT:
# "all" does NOT literally mean every Kubernetes resource type.
# For example, ConfigMaps, Secrets and PVCs are not necessarily
# included.
kubectl get all

# Execute a command inside a running container.
kubectl exec <pod-name> -- <command>

# Open an interactive shell inside a container.
kubectl exec -it <pod-name> -- sh

# Follow logs continuously.
kubectl logs -f <pod-name>

# Read logs from the previous container instance.
kubectl logs <pod-name> --previous


### 34. Useful Combined Commands
================================

# Show Pods, PVCs and PVs together.
kubectl get pods,pvc,pv

# Show Deployment, ReplicaSet and Pods together.
kubectl get deployment,replicaset,pods

# Show Services and EndpointSlices.
kubectl get services,endpointslices

# Show all Pods with their labels.
kubectl get pods --show-labels

# Show Pods with IP addresses and nodes.
kubectl get pods -o wide


### 35. Cleanup — Application
============================

# Delete the Spring Boot Deployment.
kubectl delete deployment climbing-management

# Delete the Spring Boot Service.
kubectl delete service climbing-management-service

# Delete the application ConfigMap.
kubectl delete configmap climbing-management-config

# Delete the application Secret.
kubectl delete secret climbing-management-secret


### 36. Cleanup — PostgreSQL
============================

# Delete the PostgreSQL Deployment.
kubectl delete deployment postgres

# Delete the PostgreSQL PVC.
#
# WARNING:
# The associated PV/storage may also be deleted depending
# on the StorageClass reclaim policy.
kubectl delete pvc postgres-pvc

# Show the remaining PersistentVolumes.
kubectl get pv


### 37. Kubernetes Architecture — Application
=============================================

# Complete application architecture:
#
#
#                  Kubernetes Cluster
#
#                         │
#                         ▼
#              ┌─────────────────────┐
#              │ Application Service │
#              │     ClusterIP       │
#              └──────────┬──────────┘
#                         │
#                    selector
#                         │
#              ┌──────────┼──────────┐
#              ▼          ▼          ▼
#           App Pod    App Pod    App Pod
#              │
#              │
#              ▼
#       Spring Boot Container
#
#
# Deployment manages the Pods:
#
# Deployment
#      │
#      ▼
# ReplicaSet
#      │
#      ▼
#    Pods


### 38. Kubernetes Architecture — PostgreSQL
============================================

# PostgreSQL storage architecture:
#
#
# PostgreSQL Pod
#      │
#      │ volumeMount
#      ▼
# postgres-storage
#      │
#      │ references
#      ▼
# postgres-pvc
#      │
#      │ bound to
#      ▼
# PersistentVolume
#      ▲
#      │ dynamically provisioned by
#      │
# StorageClass
#
#
# Provisioning:
#
# PVC → StorageClass → PV
#
# Consumption:
#
# Pod → PVC → PV


### 39. Kubernetes Diagnostic Mental Models
===========================================

# Deployment troubleshooting:
#
# Deployment
#     ↓
# ReplicaSet
#     ↓
# Pod
#     ↓
# Container
#     ↓
# Application logs


# Service troubleshooting:
#
# Service
#     ↓
# Selector
#     ↓
# Pod labels
#     ↓
# EndpointSlice
#     ↓
# Pod IP


# Storage troubleshooting:
#
# Pod
#     ↓
# volumeMount
#     ↓
# Pod volume
#     ↓
# PVC
#     ↓
# PV
#     ↓
# StorageClass


# Pod failure troubleshooting:
#
# Pod status
#     ↓
# kubectl describe pod
#     ↓
# kubectl logs
#     ↓
# kubectl logs --previous
#     ↓
# kubectl get events


### 40. Most Important Kubernetes Interview Concepts
====================================================

# Deployment
#
# Manages the desired state of application Pods.
# Usually manages ReplicaSets, which manage Pods.


# ReplicaSet
#
# Ensures that the desired number of Pod replicas exists.


# Pod
#
# The smallest deployable unit in Kubernetes.
# Contains one or more containers that share networking
# and storage context.


# Service
#
# Provides a stable network endpoint for a group of Pods.
# Uses label selectors to determine which Pods receive traffic.


# ClusterIP
#
# The default Kubernetes Service type.
# Makes the Service reachable from inside the cluster.


# ConfigMap
#
# Stores non-sensitive configuration data.


# Secret
#
# Stores sensitive configuration data.
# Kubernetes Secret values in "data" are Base64 encoded,
# which is NOT the same as encryption.


# Readiness Probe
#
# Determines whether a Pod should receive traffic.


# Liveness Probe
#
# Determines whether a container should be restarted
# because the application is considered unhealthy.


# PersistentVolumeClaim
#
# A request for persistent storage made by a workload.


# PersistentVolume
#
# Represents storage available to Kubernetes workloads.


# StorageClass
#
# Defines how storage should be dynamically provisioned.


# Rolling Update
#
# Gradually replaces old Pods with new Pods during
# a Deployment update.


# EndpointSlice
#
# Stores network endpoints corresponding to Pods selected
# by a Service.


# Namespace
#
# Provides logical isolation and organization of resources
# inside a Kubernetes cluster.


# kubectl
#
# Command-line client used to communicate with the
# Kubernetes API server.


### 41. Essential Interview Command Set
=======================================

# If asked for the commands you use most often,
# these are the core ones to remember:

kubectl get pods
kubectl get pods -o wide
kubectl describe pod <pod-name>
kubectl logs <pod-name>
kubectl logs <pod-name> --previous
kubectl get events --sort-by=.lastTimestamp

kubectl get deployments
kubectl describe deployment <deployment-name>
kubectl rollout status deployment/<deployment-name>
kubectl rollout history deployment/<deployment-name>
kubectl rollout undo deployment/<deployment-name>

kubectl get services
kubectl describe service <service-name>
kubectl get endpointslices

kubectl get configmaps
kubectl describe configmap <configmap-name>

kubectl get secrets
kubectl describe secret <secret-name>

kubectl get pvc
kubectl get pv
kubectl get storageclass

kubectl exec <pod-name> -- <command>
kubectl exec -it <pod-name> -- sh

kubectl get all

kubectl apply -f <file>
kubectl delete -f <file>

kubectl scale deployment <deployment-name> --replicas=<number>

kubectl port-forward service/<service-name> <local-port>:<service-port>


### 42. Core Kubernetes Mental Model
====================================

# Kubernetes continuously compares:
#
# DESIRED STATE
#      │
#      │
#      ▼
# Kubernetes controllers
#      │
#      │ reconcile
#      ▼
# CURRENT STATE
#
# Example:
#
# Deployment says:
# replicas: 3
#
# If only 2 Pods are running,
# Kubernetes creates another Pod.
#
# If 4 Pods are somehow running,
# Kubernetes removes one.
#
# Kubernetes continuously works to make
# the actual state match the desired state.


kubectl run postgres-client --rm -it --restart=Never --image=postgres:17 -- bash
# kubectl run postgres-client → creates a temporary Pod.
# --rm → deletes it when we exit.
# -it → gives us an interactive terminal.
# --restart=Never → creates a Pod directly, not a Deployment.
# --image=postgres:17 → uses the PostgreSQL image.
# -- bash → opens a Bash shell inside the container.


# Verify Kubernetes Service networking from inside the cluster:
#
# 1. Create a temporary PostgreSQL client Pod:
kubectl run postgres-client --rm -it --restart=Never --image=postgres:17 -- bash

# 2. Verify Kubernetes DNS resolves the Service:
getent hosts postgres

# 3. Verify PostgreSQL is reachable through the Service:
pg_isready -h postgres -p 5432

# If pg_isready reports:
#
# postgres:5432 - accepting connections
#
# then DNS + Service routing + PostgreSQL connectivity are working.
#
# 4. Exit the temporary Pod:
exit
#
# --rm automatically removes the temporary Pod.