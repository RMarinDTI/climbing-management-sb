### Check Maven version
mvn -version


### Clean the project
#
# Deletes the target/ directory.
# This removes compiled classes, previous JARs and other generated files.
#
# Useful when you want a completely fresh build.

mvn clean


### Compile the application
#
# Compiles the Java source code.
# Dependencies are downloaded automatically if they are not already
# available in the local Maven repository.

mvn compile


### Run unit tests
#
# Executes the project's tests.
# By default, Maven uses the test phase of the lifecycle.

mvn test


### Package the application
#
# Compiles, tests and packages the application.
#
# For a Spring Boot application, this normally creates a JAR inside:
#
#   target/

mvn package


### Clean + package
#
# First removes the previous build.
# Then compiles, tests and packages the application.

mvn clean package


### Package without running tests
#
# Useful when you already tested the application separately
# and only need to generate the JAR.
#
# WARNING:
# -DskipTests skips test execution.
# The tests are still compiled.

mvn clean package -DskipTests


### Start the Spring Boot application
#
# Runs the application using the Spring Boot Maven plugin.

mvn spring-boot:run


### Start Spring Boot using the LOCAL profile
#
# Explicitly activates:
#
#   application-local.properties
#
# This is useful when running Spring Boot directly from Windows
# while PostgreSQL and MongoDB are running in Docker.

mvn spring-boot:run "-Dspring-boot.run.profiles=local"


### Start Spring Boot using the DOCKER profile
#
# Explicitly activates:
#
#   application-docker.properties
#
# This profile is designed for the application running inside Docker.
#
# Docker's internal DNS resolves:
#
#   postgres -> PostgreSQL container
#   mongo    -> MongoDB container

mvn spring-boot:run "-Dspring-boot.run.profiles=docker"


### Start Spring Boot on a different port
#
# Overrides the default server port for this execution.
#
# Example:
#   http://localhost:9090

mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=9090"


### Display the dependency tree
#
# Shows all dependencies used by the application,
# including transitive dependencies.
#
# Useful for understanding:
#   - which libraries are being included
#   - dependency conflicts
#   - transitive dependencies

mvn dependency:tree


### Download project dependencies
#
# Downloads dependencies required by the project
# into Maven's local repository.
#
# Useful in Docker builds because dependencies can be
# downloaded in a separate Docker layer and reused by Docker cache.

mvn dependency:go-offline


### Validate the project
#
# Checks that the project is correct and all required information
# is available before the build continues.

mvn validate


### Show the effective POM
#
# Displays the final POM after Maven has resolved:
#   - parent POMs
#   - properties
#   - dependency management
#   - plugins
#   - inherited configuration
#
# Useful when Maven behaves differently from what appears
# in your pom.xml.

mvn help:effective-pom


### Check for dependency updates
#
# Requires the Maven Versions Plugin.
#
# Shows newer versions available for project dependencies.

mvn versions:display-dependency-updates


### Run the complete verification lifecycle
#
# Executes the lifecycle up to the verify phase.
#
# This includes:
#   validate
#   compile
#   test
#   package
#   integration-test
#   verify
#
# Useful for CI/CD pipelines.

mvn clean verify


### Maven lifecycle
#
# The main Maven lifecycle phases are:
#
#   validate
#      ↓
#   compile
#      ↓
#   test
#      ↓
#   package
#      ↓
#   verify
#      ↓
#   install
#      ↓
#   deploy
#
# Each phase executes the previous phases automatically.
#
# Example:
#
# mvn package
#
# automatically executes:
#
#   validate
#   compile
#   test
#   package


### Maven local repository
#
# Maven stores downloaded dependencies in the local repository.
#
# Windows:
#
#   C:\Users\<username>\.m2\repository\
#
# Linux/macOS:
#
#   ~/.m2/repository/
#
# Maven checks this repository before downloading dependencies
# from remote repositories.


### Maven install
#
# Builds the project and installs the generated artifact
# into the local Maven repository.
#
# This allows another local Maven project to use the artifact
# as a dependency.

mvn clean install


### Maven deploy
#
# Builds the project and publishes the artifact to a configured
# remote Maven repository.
#
# Common examples:
#   Nexus
#   Artifactory
#   GitHub Packages
#
# Requires the appropriate repository configuration and credentials.

mvn deploy


### Maven + Spring Boot + Docker
#
# Typical production build flow:
#
#   1. Maven compiles the application
#   2. Maven runs tests
#   3. Maven packages the application as a JAR
#   4. Docker copies the JAR into the runtime image
#   5. Docker runs the Spring Boot application
#
# Example:
#
#   mvn clean package
#
# followed by:
#
#   docker build -t climbing-management-sb:latest .
#
# Or Docker can execute Maven inside a multi-stage Dockerfile.


### Maven in CI/CD
#
# A typical CI pipeline can execute:
#
#   mvn clean verify
#
# This validates the project, compiles the code,
# runs tests and verifies the packaged application.
#
# The resulting JAR can then be used to build the Docker image.


### Useful Maven commands — quick reference
#
# Check Maven:
mvn -version

# Clean:
mvn clean

# Compile:
mvn compile

# Test:
mvn test

# Package:
mvn package

# Clean + package:
mvn clean package

# Package without executing tests:
mvn clean package -DskipTests

# Run Spring Boot:
mvn spring-boot:run

# Run Spring Boot with local profile:
mvn spring-boot:run "-Dspring-boot.run.profiles=local"

# Run Spring Boot with Docker profile:
mvn spring-boot:run "-Dspring-boot.run.profiles=docker"

# Dependency tree:
mvn dependency:tree

# Download dependencies:
mvn dependency:go-offline

# Full verification:
mvn clean verify

# Install artifact locally:
mvn clean install

# Deploy artifact remotely:
mvn deploy