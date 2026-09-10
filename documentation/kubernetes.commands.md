### Kubernetes client and cluster

kubectl version --client

# Show available Kubernetes contexts and the currently selected context.
kubectl config get-contexts

# Show information about the current cluster.
kubectl cluster-info

# Show Kubernetes nodes.
kubectl get nodes


### Pods — basic lifecycle

# List Pods in the current namespace.
kubectl get pods

# Create a temporary Pod directly.
kubectl run nginx --image=nginx:alpine

# Show detailed information about the Pod.
kubectl describe pod nginx

# Show the Pod together with its IP and node.
kubectl get pod nginx -o wide

# Delete the Pod.
kubectl delete pod nginx


### Deployment and ReplicaSet

# Create the Deployment defined in the YAML file.
kubectl apply -f k8s/nginx-deployment.yaml

# Show Deployments.
kubectl get deployments

# Show ReplicaSets.
kubectl get replicasets

# Show Pods managed by the Deployment.
kubectl get pods

# Delete one Pod to demonstrate Kubernetes self-healing.
# The Deployment/ReplicaSet automatically creates a replacement.
kubectl delete pod nginx-cd54446c4-8rkbg

# Show Pod IPs and the node where each Pod is running.
kubectl get pods -o wide


### Services

# Create the Service defined in the YAML file.
kubectl apply -f k8s/nginx-service.yaml

# Show Services.
kubectl get services

# Show EndpointSlices.
kubectl get endpointslices

# Show EndpointSlices belonging to nginx-service.
kubectl get endpointslices \
  -l kubernetes.io/service-name=nginx-service

# Show detailed Service information.
kubectl describe service nginx-service


### Test the ClusterIP from inside Kubernetes

# Start a temporary curl container.
# --rm = automatically delete it when it exits.
# -it = interactive terminal.
kubectl run curl-test \
  --image=curlimages/curl:latest \
  --rm -it -- sh

# Inside the container:
curl http://nginx-service

# Exit the temporary container:
exit


### Port forwarding

# Temporarily forward a local machine port to the Kubernetes Service.
#
# localhost:8080 → nginx-service:80
#
# This is mainly useful for local development/debugging.
kubectl port-forward service/nginx-service 8080:80


### Cleanup

kubectl delete service nginx-service
kubectl delete deployment nginx


### ConfigMap

# Create the ConfigMap.
kubectl apply -f k8s/app-config.yaml

# List ConfigMaps.
kubectl get configmaps

# Show detailed ConfigMap information.
kubectl describe configmap climbing-management-config

# Create the temporary test Pod that consumes the ConfigMap.
kubectl apply -f k8s/config-test.yaml

# Show the Pod output and verify the injected environment variables.
kubectl logs config-test


### Secret

# Create the Kubernetes Secret.
kubectl apply -f k8s/app-secret.yaml

# Create the temporary test Pod that consumes the Secret.
kubectl apply -f k8s/secret-test.yaml

# Verify that the Secret was injected.
kubectl logs secret-test

# List Secrets.
kubectl get secrets

# Delete the temporary test Pod.
kubectl delete pod secret-test