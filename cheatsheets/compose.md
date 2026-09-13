DOCKER TROUBLESHOOTING
======================


### Check running Compose services

docker compose ps


### Show application logs

docker compose logs app


### Show only the last 30 application log lines

docker compose logs app --tail 30


### Rebuilding the application image

docker compose down
docker compose up -d --build


### docker compose exec
#
# Executes a command INSIDE an already-running container
# belonging to a Compose service.
#
# Important:
#
#   docker compose exec app ...
#
# uses the Compose SERVICE name:
#
#   app
#
# It does NOT create a new container.

docker compose exec app sh


    # Show the user running inside the container.

    whoami


    # List the application directory.

    ls -la /app


    # Test the Spring Boot application from INSIDE
    # the application container.
    #
    # IMPORTANT:
    #
    # localhost refers to the APP CONTAINER itself.
    #
    # Therefore:
    #
    #   localhost:8080
    #
    # means:
    #
    #   Spring Boot running inside this same container.

    wget -S -O - http://localhost:8080/


    # Exit the container shell.

    exit


### docker compose exec vs docker exec
#
# docker compose exec:
#
#   Uses the Compose SERVICE name.
#
# Example:

docker compose exec app sh


# docker exec:
#
#   Uses the actual CONTAINER name or container ID.
#
# Example:

docker exec -it climbing-management-sb-app-1 sh


# Mental model:
#
#   docker compose exec
#           ↓
#   Compose service
#           ↓
#   corresponding container
#
#
#   docker exec
#           ↓
#   specific container


### Configuration vs actual container state


# docker compose config
#
# Shows the FINAL Compose configuration after resolving:
#
#   - compose.yaml
#   - .env variables
#   - environment interpolation
#   - service configuration
#   - merged Compose configuration
#
# In other words:
#
#   "What configuration is Compose going to use?"

docker compose config


# docker inspect
#
# Shows the ACTUAL configuration and runtime state
# of an existing Docker object.
#
# For example:
#
#   - environment variables
#   - networks
#   - mounts
#   - ports
#   - health status
#   - container state
#   - image
#
# In other words:
#
#   "What does the created container actually have?"

docker inspect climbing-management-sb-app-1


### Configuration vs runtime troubleshooting
#
# docker compose config
#       ↓
# Intended configuration
#
# docker inspect
#       ↓
# Actual created container
#
#
# This distinction is very useful when:
#
#   "My compose.yaml looks correct,
#    but the running container behaves differently."


### Docker troubleshooting — deliberate failure #1
#
# Scenario:
#
# The Spring Boot container cannot connect to PostgreSQL
# because it is using the wrong hostname.
#
#
# Docker Compose