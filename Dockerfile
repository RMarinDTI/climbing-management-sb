
# Why use multi-stage builds?
# Multi-stage builds separate the build environment from the runtime environment.
# We can use Maven and the JDK to compile the application, but copy only the resulting artifact into a smaller runtime image.
# This reduces image size and attack surface and keeps build tools out of production.

# When a Docker layer changes, subsequent layers are invalidated; previous cached layers can still be reused.

# ---------- BUILD ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

# Dependency layer can be cached separately (smaller → safer → reproducible)
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# ---------- RUNTIME ----------
# Alpine-based image can significantly reduce the base image footprint
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# New User: Spring Boot application, it doesn't need root.
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the jar from the previous build
COPY --from=build /app/target/*.jar app.jar

# From this point onwards, run the container as spring, not root.
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

