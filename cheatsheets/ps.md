POWERSHELL COMMANDS — CLIMBING MANAGEMENT
=========================================


### Check PowerShell version

$PSVersionTable.PSVersion


### Show the current directory

Get-Location


### List files and directories

Get-ChildItem


### List files with details, including hidden files

Get-ChildItem -Force


### Change directory

Set-Location <path>

# Short alias:

cd <path>


### Go to the parent directory

Set-Location ..


### Go to the user's home directory

Set-Location ~


### Clear the terminal

Clear-Host

# Short alias:

cls


### Create a directory

New-Item -ItemType Directory <directory-name>


### Create a file

New-Item <file-name>


### Remove a file

Remove-Item <file>


### Remove a directory

Remove-Item <directory>


### Remove a directory and everything inside it
#
# WARNING:
# Permanently deletes the directory and its contents.

Remove-Item <directory> -Recurse


### Copy a file

Copy-Item <source> <destination>


### Copy a directory

Copy-Item <source> <destination> -Recurse


### Move a file

Move-Item <source> <destination>


### Rename a file

Rename-Item <old-name> <new-name>


### Show the contents of a text file

Get-Content <file>


### Show the last lines of a file

Get-Content <file> -Tail 20


### Follow a file as it changes
#
# Useful for watching application logs.

Get-Content <file> -Wait


### Search for a word in the whole project
#
# Searches recursively through all files.
#
# Shows:
#
#   - file
#   - line number
#   - matching line

Get-ChildItem -Recurse -File | Select-String "word"


### Search for a word ignoring .git, target and node_modules
#
# Useful for Java/Maven projects.
#
# Avoids:
#
#   - .git
#   - target
#   - node_modules

Get-ChildItem -Recurse -File |
Where-Object { $_.FullName -notmatch '\\(\.git|target|node_modules)\\' } |
Select-String "word"


### Search only Java files

Get-ChildItem -Recurse -Filter *.java |
Select-String "word"


### Search only YAML files

Get-ChildItem -Recurse -Include *.yml,*.yaml -File |
Select-String "word"


### Search only Markdown files

Get-ChildItem -Recurse -Filter *.md |
Select-String "word"


### Search for an exact phrase

Get-ChildItem -Recurse -File |
Select-String "spring.datasource.url"


### Search using a regular expression
#
# Matches:
#
#   Spring
#   Mongo
#   Postgres

Get-ChildItem -Recurse -File |
Select-String "Spring|Mongo|Postgres"


### Case-sensitive search
#
# By default Select-String is case-insensitive.
# -CaseSensitive makes the search case-sensitive.

Get-ChildItem -Recurse -File |
Select-String "MongoDB" -CaseSensitive


### Show only matching file names
#
# Useful when you only want to know which files contain the word.

Get-ChildItem -Recurse -File |
Select-String "ConfigMap" |
Select-Object -ExpandProperty Path -Unique


### Count matches

(Get-ChildItem -Recurse -File |
Select-String "ConfigMap").Count


### PowerShell aliases
#
# Common shorter alternatives:
#
# Get-ChildItem  → gci
# Set-Location   → cd
# Get-Content    → gc
# Select-String  → sls
# Clear-Host     → cls
# Copy-Item      → cp
# Move-Item      → mv
# Remove-Item    → rm


### Search using PowerShell aliases

gci -Recurse -File | sls "ConfigMap"


### Search the current directory only

Get-ChildItem -File | Select-String "word"


### Find files by name

Get-ChildItem -Recurse -Filter "*Docker*"


### Find Java files

Get-ChildItem -Recurse -Filter "*.java"


### Find YAML files

Get-ChildItem -Recurse -Include "*.yml","*.yaml" -File


### Find files modified recently

Get-ChildItem -File |
Sort-Object LastWriteTime -Descending


### Find the 10 most recently modified files

Get-ChildItem -Recurse -File |
Sort-Object LastWriteTime -Descending |
Select-Object -First 10


### Check whether a command exists

Get-Command <command>


### Check where an executable is located

Get-Command java


### Check Java version

java -version


### Check Maven version

mvn -version


### Check Git version

git --version


### Check Docker version

docker --version


### Check Docker Compose version

docker compose version


### Check kubectl version

kubectl version --client


### Show environment variables

Get-ChildItem Env:


### Show one environment variable

$env:PATH


### Read an environment variable

$env:JAVA_HOME


### Set an environment variable for the current PowerShell session
#
# The value exists only in this terminal session.

$env:MY_VARIABLE = "value"


### Remove an environment variable from the current session

Remove-Item Env:MY_VARIABLE


### Run a program

.\program.exe


### Run a script from the current directory

.\script.ps1


### Run a command and show its exit code
#
# $LASTEXITCODE contains the exit code
# returned by the last native executable.

mvn test

$LASTEXITCODE


### Test whether a command succeeded
#
# Native programs normally use exit code 0 for success.

if ($LASTEXITCODE -eq 0) {
Write-Host "Command succeeded"
}


### Test whether a file exists

Test-Path <file>


### Test whether a directory exists

Test-Path <directory>


### Create a file with specific content

Set-Content <file> "Hello"


### Append text to a file

Add-Content <file> "Another line"


### Read JSON from a file

Get-Content <file>.json | ConvertFrom-Json


### Convert an object to JSON

<object> | ConvertTo-Json


### Get running processes

Get-Process


### Find a specific process

Get-Process | Where-Object { $_.ProcessName -like "*java*" }


### Stop a process
#
# WARNING:
# Terminates the selected process.

Stop-Process -Name <process-name>


### Find a process using a specific port

Get-NetTCPConnection -LocalPort 8080


### Find which process owns a port

Get-NetTCPConnection -LocalPort 8080 |
Select-Object LocalAddress,LocalPort,State,OwningProcess


### Find the process by PID

Get-Process -Id <PID>


### Test whether a TCP port is reachable

Test-NetConnection localhost -Port 8080


### Test PostgreSQL port

Test-NetConnection localhost -Port 5432


### Test MongoDB port

Test-NetConnection localhost -Port 27017


### Test an HTTP endpoint

Invoke-WebRequest http://localhost:8080


### Get only the HTTP response content

(Invoke-WebRequest http://localhost:8080).Content


### Call a REST API

Invoke-RestMethod http://localhost:8080/actuator/health


### Call a REST API with GET
#
# The backtick (`) is PowerShell's line-continuation character.

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/courses" `
-Method Get


### Send JSON with POST
#
# Convert the PowerShell object to JSON first.

$body = @{
name  = "Sport Climbing"
price = 90
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/courses" `
-Method Post `
    -ContentType "application/json" `
-Body $body


### Start a local Spring Boot application

mvn spring-boot:run


### Start Spring Boot with the local profile

mvn spring-boot:run "-Dspring-boot.run.profiles=local"


### Stop a foreground application

Ctrl + C


### Run Maven tests

mvn test


### Run Maven package

mvn clean package


### Run Docker Compose

docker compose up -d


### Stop Docker Compose

docker compose down


### Check running Compose services

docker compose ps


### Show application logs

docker compose logs app


### Follow application logs

docker compose logs -f app


### Show only the last 30 application log lines

docker compose logs app --tail 30


### Execute a command inside a running container

docker compose exec app sh


### Check Docker health status

docker inspect climbing-management-sb-app-1 --format '{{.State.Health.Status}}'


### Check Docker Compose configuration

docker compose config


### Check Kubernetes cluster

kubectl get nodes


### Check Kubernetes Pods

kubectl get pods


### Check Pods with more information

kubectl get pods -o wide


### Check Kubernetes Services

kubectl get services


### Check Deployments

kubectl get deployments


### Check ReplicaSets

kubectl get replicasets


### Check ConfigMaps

kubectl get configmaps


### Check Secrets

kubectl get secrets


### Check PersistentVolumeClaims

kubectl get pvc


### Check PersistentVolumes

kubectl get pv


### Check StorageClasses

kubectl get storageclass


### Check Horizontal Pod Autoscalers

kubectl get hpa


### Check Ingress resources

kubectl get ingress


### Check all common Kubernetes resources

kubectl get all


### Describe a Kubernetes resource

kubectl describe pod <pod-name>

kubectl describe deployment <deployment-name>

kubectl describe service <service-name>

kubectl describe ingress <ingress-name>


### Show Kubernetes Pod logs

kubectl logs <pod-name>


### Follow Kubernetes Pod logs

kubectl logs -f <pod-name>


### Show logs from a specific container
#
# Useful when a Pod contains multiple containers.

kubectl logs <pod-name> -c <container-name>


### Execute a command inside a Kubernetes Pod

kubectl exec -it <pod-name> -- sh


### Apply a Kubernetes manifest

kubectl apply -f <file>.yaml


### Delete a Kubernetes manifest

kubectl delete -f <file>.yaml


### Delete a Kubernetes Pod

kubectl delete pod <pod-name>


### Show Kubernetes resource YAML

kubectl get deployment <deployment-name> -o yaml


### Show Kubernetes resource as JSON

kubectl get deployment <deployment-name> -o json


### Port-forward a Kubernetes Service
#
# Creates a temporary tunnel from your local machine
# to the Kubernetes Service.
#
# Example:
#
# Local:
#
#   localhost:8080
#
# Kubernetes Service:
#
#   port 80

kubectl port-forward service/<service-name> 8080:80


### Port-forward a Pod

kubectl port-forward pod/<pod-name> 8080:8080


### Check Kubernetes events
#
# Useful for troubleshooting Pods that fail to start.

kubectl get events --sort-by=.lastTimestamp


### Check Pod status and events

kubectl describe pod <pod-name>


### Check EndpointSlices
#
# EndpointSlices show the actual Pod endpoints
# behind a Kubernetes Service.

kubectl get endpointslices -l kubernetes.io/service-name=<service-name>


### Check Kubernetes namespaces

kubectl get namespaces


### Show the current Kubernetes context

kubectl config current-context


### List Kubernetes contexts

kubectl config get-contexts


### Switch Kubernetes context

kubectl config use-context <context-name>


### Show Kubernetes cluster information

kubectl cluster-info


### Check Kubernetes API connectivity

kubectl version


### Check Kubernetes resource usage
#
# Requires Metrics Server.

kubectl top nodes

kubectl top pods


### Check HPA details

kubectl describe hpa <hpa-name>


### Check HPA status

kubectl get hpa


### Check Deployment rollout status

kubectl rollout status deployment/<deployment-name>


### Show Deployment rollout history

kubectl rollout history deployment/<deployment-name>


### Restart a Deployment

kubectl rollout restart deployment/<deployment-name>


### Roll back a Deployment

kubectl rollout undo deployment/<deployment-name>


### Scale a Deployment manually

kubectl scale deployment/<deployment-name> --replicas=3


### Check Kubernetes resources in a namespace

kubectl get all -n <namespace>


### Check resources across all namespaces

kubectl get pods -A


### Search Kubernetes resources
#
# Useful for quickly finding resources matching a name.

kubectl get all


### Kubernetes troubleshooting
#
# 1. Check Pods

kubectl get pods -o wide


# 2. Check Pod details

kubectl describe pod <pod-name>


# 3. Check logs

kubectl logs <pod-name>


# 4. Check recent cluster events

kubectl get events --sort-by=.lastTimestamp


# 5. Check the Deployment

kubectl get deployment


# 6. Check the Service

kubectl get service


# 7. Check EndpointSlices

kubectl get endpointslices


### PowerShell pipeline
#
# The | operator sends the output of one command
# to another command.
#
# This allows commands to be chained together.

Get-Process | Where-Object { $_.ProcessName -like "*java*" }


### Where-Object
#
# Filters objects based on a condition.

Get-ChildItem -File |
Where-Object { $_.Extension -eq ".java" }


### Select-Object
#
# Selects specific properties or limits results.

Get-Process |
Select-Object ProcessName, Id


### Sort-Object
#
# Sorts objects by a property.

Get-ChildItem -File |
Sort-Object LastWriteTime -Descending


### Select the first results

Get-ChildItem -File |
Select-Object -First 10


### Select the last results

Get-ChildItem -File |
Select-Object -Last 10


### PowerShell variables

$name = "Climbing Management"

$port = 8080


### Display a variable

$name


### String interpolation

$name = "Climbing Management"

Write-Host "Application: $name"


### Run multiple commands

git status
git log -1 --oneline


### Run the next command only if the previous command succeeds
#
# Native programs such as Maven, Git and Docker
# expose their result through $LASTEXITCODE.

mvn test

if ($LASTEXITCODE -eq 0) {
mvn package
}


### PowerShell command history

Get-History


### Execute a previous command

Invoke-History <id>


### Clear command history from the current session

Clear-History


### Find a command in history

Get-History |
Where-Object { $_.CommandLine -like "*docker*" }


### Get help for a command

Get-Help <command>


### Get examples for a command

Get-Help <command> -Examples


### Update PowerShell help

Update-Help


### PowerShell — useful shortcuts
#
# Tab
#   Autocomplete commands, files and directories.
#
# Ctrl + C
#   Stop the current command/process.
#
# Up / Down
#   Navigate command history.
#
# Ctrl + L
#   Clear the visible terminal screen.
#
# Ctrl + R
#   Search command history in some terminal environments.


### PowerShell — useful operators
#
# Pipe:
#
#   |
#
# Sends the output of one command
# into another command.
#
#
# Comparison:
#
#   -eq
#   -ne
#   -gt
#   -lt
#   -ge
#   -le
#
#
# Matching:
#
#   -like
#   -notlike
#   -match
#   -notmatch
#
#
# Example:

Get-Process |
Where-Object { $_.ProcessName -like "*java*" }


### PowerShell — exit codes
#
# Native programs return an exit code.
#
# Convention:
#
#   0 = success
#   non-zero = failure
#
#
# The last native program's exit code
# is available through:

$LASTEXITCODE


### PowerShell — command discovery
#
# Get-Command can find:
#
#   - cmdlets
#   - functions
#   - aliases
#   - scripts
#   - executables
#
#
# Examples:

Get-Command java

Get-Command mvn

Get-Command git

Get-Command docker

Get-Command kubectl


### PowerShell — environment variables
#
# Environment variables are available through:
#
#   $env:
#
#
# Examples:

$env:PATH

$env:JAVA_HOME


### Set environment variable for current session

$env:MY_VARIABLE = "value"


### Remove environment variable

Remove-Item Env:MY_VARIABLE


### PowerShell — file existence

Test-Path <file>


### PowerShell — directory existence

Test-Path <directory>


### PowerShell — JSON
#
# JSON is commonly used when testing REST APIs.

$body = @{
name  = "Sport Climbing"
price = 90
} | ConvertTo-Json


### Convert JSON into a PowerShell object

$json = Get-Content <file>.json | ConvertFrom-Json


### Convert a PowerShell object to JSON

<object> | ConvertTo-Json


### PowerShell REST API troubleshooting
#
# Test whether Spring Boot is reachable:

Test-NetConnection localhost -Port 8080


# Test the health endpoint:

Invoke-RestMethod http://localhost:8080/actuator/health


# Test a REST endpoint:

Invoke-RestMethod http://localhost:8080/api/courses


# Send a POST request:

$body = @{
name  = "Sport Climbing"
price = 90
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/courses" `
-Method Post `
    -ContentType "application/json" `
-Body $body


### PowerShell + Spring Boot troubleshooting
#
# Application not responding?
#
#
# 1. Check whether port 8080 is listening:

Get-NetTCPConnection -LocalPort 8080


# 2. Test the port:

Test-NetConnection localhost -Port 8080


# 3. Call the health endpoint:

Invoke-RestMethod http://localhost:8080/actuator/health


# 4. Find the process using the port:

Get-NetTCPConnection -LocalPort 8080 |
Select-Object LocalAddress,LocalPort,State,OwningProcess


# 5. Inspect the process:

Get-Process -Id <PID>


### PowerShell + Docker troubleshooting
#
# Check containers:

docker compose ps


# Check application logs:

docker compose logs app --tail 30


# Enter the application container:

docker compose exec app sh


# Check Docker DNS from inside the app container:

getent hosts postgres
getent hosts mongo


# Check actual container state:

docker inspect climbing-management-sb-app-1


### PowerShell + Kubernetes troubleshooting
#
# Check Pods:

kubectl get pods -o wide


# Check Deployment:

kubectl get deployment


# Check Service:

kubectl get service


# Check EndpointSlices:

kubectl get endpointslices


# Check logs:

kubectl logs <pod-name>


# Check events:

kubectl get events --sort-by=.lastTimestamp


# Check resource usage:

kubectl top pods


# Check HPA:

kubectl get hpa


# Check Ingress:

kubectl get ingress


### PowerShell troubleshooting mental model
#
#
#                    APPLICATION PROBLEM
#                           |
#             +-------------+-------------+
#             |             |             |
#             ↓             ↓             ↓
#           LOCAL          DOCKER       KUBERNETES
#             |             |             |
#             ↓             ↓             ↓
#          Port 8080      Compose       Pods
#             |           logs/ps         |
#             ↓             |             ↓
#       Test-NetConnection ↓          Deployment
#             |           exec            |
#             ↓             |             ↓
#      Invoke-RestMethod   DNS         Service
#                         inspect          |
#                                         ↓
#                                   EndpointSlices
#                                         |
#                                         ↓
#                                      Ingress
#
#
# PowerShell is the common command-line environment
# used to inspect all three layers on Windows.


### Senior Backend interview questions
#
# Q: What is PowerShell?
#
# A:
#
# PowerShell is a command-line shell and scripting
# environment from Microsoft.
#
# Unlike traditional shells, PowerShell works primarily
# with .NET objects rather than plain text.
#
#
# Q: What is the PowerShell pipeline?
#
# A:
#
# The | operator passes objects from one command
# to another command.
#
# Example:
#
#   Get-Process | Where-Object { ... }
#
#
# Q: What is the difference between Get-Content
#    and Select-String?
#
# A:
#
# Get-Content reads file contents.
#
# Select-String searches text for matching patterns.
#
#
# Q: How do you check whether port 8080 is reachable?
#
# A:
#
# Use:
#
#   Test-NetConnection localhost -Port 8080
#
#
# Q: How do you test a REST endpoint from PowerShell?
#
# A:
#
# Use:
#
#   Invoke-RestMethod
#
# or:
#
#   Invoke-WebRequest
#
#
# Invoke-RestMethod is especially convenient
# for REST APIs because it converts JSON responses
# into PowerShell objects.
#
#
# Q: How do you find which process is using port 8080?
#
# A:
#
# Use:
#
#   Get-NetTCPConnection -LocalPort 8080
#
# Then inspect the OwningProcess PID with:
#
#   Get-Process -Id <PID>
#
#
# Q: What does $LASTEXITCODE contain?
#
# A:
#
# It contains the exit code returned by the last
# native executable.
#
# Conventionally:
#
#   0 = success
#   non-zero = failure
#
#
# Q: Why is this useful in CI/CD?
#
# A:
#
# Scripts can check command exit codes and stop or
# continue a build depending on whether a command succeeded.
#
#
# Q: How would you troubleshoot a Spring Boot application
#    running locally?
#
# A:
#
# 1. Check whether port 8080 is listening.
#
# 2. Test the port with Test-NetConnection.
#
# 3. Call /actuator/health with Invoke-RestMethod.
#
# 4. Inspect the Java process if necessary.
#
# 5. Check application logs.
#
#
# Q: How would you troubleshoot a Dockerized Spring Boot
#    application?
#
# A:
#
# 1. docker compose ps
# 2. docker compose logs app
# 3. inspect the healthcheck
# 4. docker compose exec app sh
# 5. verify Docker DNS
# 6. inspect the actual container configuration
#
#
# Q: How would you troubleshoot an application in Kubernetes?
#
# A:
#
# 1. kubectl get pods
# 2. kubectl describe pod
# 3. kubectl logs
# 4. kubectl get events
# 5. kubectl get deployment
# 6. kubectl get service
# 7. kubectl get endpointslices
# 8. kubectl get ingress
#
#
# Q: Why is PowerShell useful for backend development?
#
# A:
#
# It provides one environment for:
#
#   - Java/Maven
#   - Git
#   - Docker
#   - Kubernetes
#   - REST API testing
#   - process management
#   - networking
#   - filesystem operations
#   - automation/scripts


### Most important PowerShell commands — quick reference

# Current directory
Get-Location

# List files
Get-ChildItem

# Change directory
cd <path>

# Clear terminal
cls

# Read file
Get-Content <file>

# Follow file
Get-Content <file> -Wait

# Search project
Get-ChildItem -Recurse -File | Select-String "word"

# Search Java files
Get-ChildItem -Recurse -Filter "*.java" | Select-String "word"

# Find files
Get-ChildItem -Recurse -Filter "*name*"

# Check command
Get-Command <command>

# Environment variable
$env:PATH

# Test file/directory
Test-Path <path>

# HTTP request
Invoke-WebRequest <url>

# REST API request
Invoke-RestMethod <url>

# Test port
Test-NetConnection localhost -Port 8080

# Running processes
Get-Process

# Process using a port
Get-NetTCPConnection -LocalPort 8080

# Command history
Get-History

# Help
Get-Help <command>

# Docker
docker compose ps

# Kubernetes
kubectl get pods


### FINAL POWERSHELL MENTAL MODEL
#
#                         POWERSHELL
#                             |
#            +----------------+----------------+
#            |                |                |
#            ↓                ↓                ↓
#        FILESYSTEM         PROCESSES       NETWORK
#            |                |                |
#            ↓                ↓                ↓
#      Get-ChildItem      Get-Process    Test-NetConnection
#      Get-Content       Stop-Process   Invoke-WebRequest
#      Copy-Item                         Invoke-RestMethod
#            |
#            ↓
#        PIPELINE
#            |
#            ↓
#    Where-Object / Select-Object / Sort-Object
#            |
#            ↓
#       DEVELOPMENT TOOLS
#            |
#       +----+----+---------+
#       |         |         |
#       ↓         ↓         ↓
#      Maven      Git      Docker
#                           |
#                           ↓
#                       Kubernetes
#
#
# KEY INTERVIEW RULES:
#
# 1. PowerShell works primarily with objects.
#
# 2. The | operator passes objects through the pipeline.
#
# 3. Where-Object filters objects.
#
# 4. Select-Object selects properties or limits results.
#
# 5. Sort-Object sorts objects.
#
# 6. $LASTEXITCODE contains the exit code of the last
#    native executable.
#
# 7. Test-NetConnection is useful for TCP connectivity.
#
# 8. Invoke-RestMethod is useful for REST API testing.
#
# 9. Get-NetTCPConnection helps identify processes
#    using network ports.
#
# 10. PowerShell can control the complete local
#     backend development environment:
#
#       Java
#       Maven
#       Git
#       Docker
#       Kubernetes
#
# 11. For troubleshooting, move from:
#
#       process
#          ↓
#       port
#          ↓
#       HTTP
#          ↓
#       Docker
#          ↓
#       Kubernetes
#
#    rather than changing multiple things at once.