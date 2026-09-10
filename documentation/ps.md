### Check PowerShell version

$PSVersionTable.PSVersion


### Show the current directory

Get-Location


### List files and directories

Get-ChildItem


### List files with details

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
#   - file
#   - line number
#   - matching line

Get-ChildItem -Recurse -File | Select-String "word"


### Search for a word ignoring .git and target
#
# Useful for Java/Maven projects.
#
# Avoids:
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
# Get-Content   → gc
# Select-String → sls
# Clear-Host    → cls
# Copy-Item     → cp
# Move-Item     → mv
# Remove-Item   → rm


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


### Describe a Kubernetes resource

kubectl describe pod <pod-name>

kubectl describe deployment <deployment-name>

kubectl describe service <service-name>


### Show Kubernetes Pod logs

kubectl logs <pod-name>


### Follow Kubernetes Pod logs

kubectl logs -f <pod-name>


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


### Port-forward a Kubernetes Service
#
# Creates a temporary tunnel from your local machine
# to the Kubernetes Service.

kubectl port-forward service/<service-name> 8080:80


### Check Kubernetes events
#
# Useful for troubleshooting Pods that fail to start.

kubectl get events --sort-by=.lastTimestamp


### Check Pod status and events

kubectl describe pod <pod-name>


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


### PowerShell pipeline
#
# The | operator sends the output of one command
# to another command.

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
# Useful for simple development workflows.

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

Get-History | Where-Object { $_.CommandLine -like "*docker*" }


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
# Ctrl + L
#   Clear the visible terminal screen.
#
# Up / Down
#   Navigate command history.
#
# Ctrl + R
#   Search command history in some terminal environments.


### Most important commands — quick reference

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

# Test file
Test-Path <file>

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