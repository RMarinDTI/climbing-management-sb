### Application Deployment

# Apply the Spring Boot Kubernetes Deployment.
kubectl apply -f k8s/app-deployment.yaml

# Show the Deployment.
kubectl get deployment climbing-management

# Show all Deployments.
kubectl get deployments

# Show the Pods created by the Deployment.
kubectl get pods

# Show Pods with their IP addresses and nodes.
kubectl get pods -o wide

# Show the Deployment hierarchy:
# Deployment → ReplicaSet → Pods.
kubectl get deployment,replicaset,pods

# Show detailed Deployment information.
kubectl describe deployment climbing-management


### Application Logs and Troubleshooting

# Show the logs of a running application Pod.
kubectl logs <pod-name>

# Show the logs from the previous crashed container.
#
# Particularly useful with CrashLoopBackOff.
kubectl logs <pod-name> --previous

# Follow the application logs in real time.
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

# Show the complete Deployment definition currently stored by Kubernetes.
kubectl get deployment climbing-management -o yaml


### CrashLoopBackOff Troubleshooting

# Show the current Pod status.
kubectl get pods

# Show why a Pod is failing.
kubectl describe pod <pod-name>

# Show logs from the current container.
kubectl logs <pod-name>

# Show logs from the previous crashed container.
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


### ConfigMap Verification

# Show all ConfigMaps.
kubectl get configmaps

# Show the contents of the application ConfigMap.
kubectl describe configmap climbing-management-config

# Show the ConfigMap as YAML.
kubectl get configmap climbing-management-config -o yaml


### Secret Verification

# List Secrets without exposing their values.
kubectl get secrets

# Show Secret metadata and available keys.
#
# Secret values are intentionally not displayed.
kubectl describe secret climbing-management-secret

# Show the Secret definition.
#
# Values stored in "data" are Base64 encoded,
# not encrypted by Base64 itself.
kubectl get secret climbing-management-secret -o yaml


### Verify Configuration Inside a Pod

# Show environment variables inside a running Pod.
kubectl exec <pod-name> -- env

# Show only application-related environment variables.
kubectl exec <pod-name> -- env | Select-String "APP_|SPRING_|LOG_LEVEL"

# Open a shell inside the container.
kubectl exec -it <pod-name> -- sh

# Exit the container shell.
exit


### Verify the Kubernetes Secret Volume

# List the files mounted under /run/secrets.
kubectl exec <pod-name> -- ls -la /run/secrets

# Show the Secret filenames without displaying their values.
kubectl exec <pod-name> -- ls -l /run/secrets

# Verify that the expected Secret file exists.
kubectl exec <pod-name> -- test -f /run/secrets/spring.datasource.password

# Return success/failure without exposing the password.
kubectl exec <pod-name> -- sh -c "test -f /run/secrets/spring.datasource.password && echo 'Secret file exists' || echo 'Secret file missing'"


### Deployment Rollout

# Show the current rollout status.
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


### Scaling

# Scale the Deployment to three Pods.
kubectl scale deployment climbing-management --replicas=3

# Check the number of running Pods.
kubectl get pods

# Scale back to two Pods.
kubectl scale deployment climbing-management --replicas=2


### Service

# Apply the application Service.
kubectl apply -f k8s/app-service.yaml

# Show Services.
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


### Port Forwarding — Spring Boot

# Temporarily expose the Kubernetes application locally.
#
# localhost:8080 → Kubernetes Service:8080
kubectl port-forward service/climbing-management-service 8080:8080

# Test the Spring Boot health endpoint from Windows.
Invoke-RestMethod http://localhost:8080/actuator/health

# Test the liveness endpoint.
Invoke-RestMethod http://localhost:8080/actuator/health/liveness

# Test the readiness endpoint.
Invoke-RestMethod http://localhost:8080/actuator/health/readiness


### Health Probes

# Show the configured health probes in the Pod definition.
kubectl describe pod <pod-name>

# Watch Pod status while probes are being evaluated.
kubectl get pods -w

# Show the application logs if a probe is failing.
kubectl logs <pod-name>


### Kubernetes Context

# Show the current Kubernetes context.
kubectl config current-context

# Show all configured contexts.
kubectl config get-contexts

# Switch to a different context.
kubectl config use-context <context-name>


### Cluster Information

# Show cluster information.
kubectl cluster-info

# Show Kubernetes client and server versions.
kubectl version

# Show Kubernetes nodes.
kubectl get nodes

# Show detailed node information.
kubectl get nodes -o wide

# Show all resources in the current namespace.
kubectl get all


### Useful Namespace Commands

# List namespaces.
kubectl get namespaces

# Show resources in the default namespace.
kubectl get all -n default

# Show Pods in a specific namespace.
kubectl get pods -n <namespace>


### Cleanup Application

# Delete the Spring Boot Deployment.
kubectl delete deployment climbing-management

# Delete the Spring Boot Service.
kubectl delete service climbing-management-service

# Delete the application ConfigMap.
kubectl delete configmap climbing-management-config

# Delete the application Secret.
kubectl delete secret climbing-management-secret


### Important Kubernetes Diagnostic Commands

# Show recent cluster events.
kubectl get events --sort-by=.lastTimestamp

# Show all resources in the namespace.
kubectl get all

# Show resource details.
kubectl describe <resource-type> <resource-name>

# Get the resource as YAML.
kubectl get <resource-type> <resource-name> -o yaml

# Execute a command inside a running container.
kubectl exec <pod-name> -- <command>

# Open an interactive shell inside a container.
kubectl exec -it <pod-name> -- sh

# Follow logs continuously.
kubectl logs -f <pod-name>

# Read logs from the previous container instance.
kubectl logs <pod-name> --previous