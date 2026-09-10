### Check running Compose services
docker compose ps

### Show application logs
docker compose logs app

### Show only the last 30 application log lines
docker compose logs app --tail 30


### Rebuilding the application image
docker compose down
docker compose up -d --build


### docker compose exec = execute a command inside an already-running container.
docker compose exec app sh

    whoami
    ls -la /app
    exit

    # Test the application from INSIDE the app container.
    # localhost refers to the app container itself.
    wget -S -O - http://localhost:8080/


### Configuration vs actual container state

# docker compose config
# Shows the final Compose configuration after resolving:
#   - .env variables
#   - Compose interpolation
#   - service configuration
#
# In other words:
# "What configuration is Compose going to use?"

docker compose config


# docker inspect
# Shows the actual configuration/state of an existing Docker object.
#
# In other words:
# "What does the created container actually have?"

docker inspect climbing-management-sb-app-1


### Docker troubleshooting — deliberate failure #1
#
# Scenario:
# The Spring Boot container cannot connect to PostgreSQL
# because it is using the wrong hostname.
#
# Verify Docker's internal DNS resolution:

docker compose exec app sh

    getent hosts postgres
    getent hosts mongo

exit


### Docker troubleshooting — deliberate failure #2
#
# Container is running ≠ application is healthy.
#
# Docker can report:
#
#   STATUS: Up
#
# while the application's healthcheck is failing.
#
# Inspect the complete healthcheck state:

docker inspect climbing-management-sb-app-1 --format '{{json .State.Health}}'