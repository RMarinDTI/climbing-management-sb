MAVEN COMMANDS — CLIMBING MANAGEMENT
====================================


### Check Maven version

mvn -version


### Clean the project
#
# Deletes the target/ directory.
#
# This removes generated build artifacts such as:
#
#   - compiled .class files
#   - previously generated JARs
#   - other Maven-generated files
#
# Useful when you want a completely fresh build.

mvn clean


### Compile the application
#
# Compiles the Java source code.
#
# Maven automatically downloads dependencies that are
# not already available in the local Maven repository.
#
# IMPORTANT:
#
# compile does NOT run the tests.

mvn compile


### Run unit tests
#
# Executes the project's tests.
#
# Maven uses the test phase of the default lifecycle.

mvn test


### Package the application
#
# Compiles, tests and packages the application.
#
# For a Spring Boot application this normally creates
# a JAR inside:
#
#   target/
#
# Example:
#
#   target/climbing-management-sb-<version>.jar

mvn package


### Clean + package
#
# First removes the previous build.
# Then:
#
#   validate
#   compile
#   test
#   package
#
# This is one of the most common Maven commands.

mvn clean package


### Package without running tests
#
# -DskipTests:
#
#   Tests are compiled but NOT executed.
#
# Useful when you need the JAR quickly after
# having already run the tests separately.
#
# WARNING:
#
# Skipping tests should not normally be used
# as a replacement for running the test suite in CI.

mvn clean package -DskipTests


### Package without compiling or running tests
#
# -Dmaven.test.skip=true:
#
# Completely skips test compilation and execution.
#
# This is different from -DskipTests.

mvn clean package -Dmaven.test.skip=true


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
# Useful when running Spring Boot directly from Windows
# while PostgreSQL and MongoDB are running in Docker.
#
# Example:
#
#   localhost:8080
#
# with:
#
#   PostgreSQL -> Docker
#   MongoDB    -> Docker
#   Spring Boot -> Windows

mvn spring-boot:run "-Dspring-boot.run.profiles=local"


### Start Spring Boot using the DOCKER profile
#
# Explicitly activates:
#
#   application-docker.properties
#
# This profile is designed for the application
# running inside Docker.
#
# Docker's internal DNS resolves:
#
#   postgres -> PostgreSQL container
#   mongo    -> MongoDB container
#
# Therefore the application can use:
#
#   postgres:5432
#   mongo:27017

mvn spring-boot:run "-Dspring-boot.run.profiles=docker"


### Start Spring Boot on a different port
#
# Overrides the default server port for this execution.
#
# Example:
#
#   http://localhost:9090

mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=9090"


### Display the dependency tree
#
# Shows all dependencies used by the application,
# including transitive dependencies.
#
# Useful for understanding:
#
#   - which libraries are included
#   - transitive dependencies
#   - dependency conflicts
#   - dependency versions

mvn dependency:tree


### Display the dependency tree for a specific dependency
#
# Useful when investigating where a particular library
# comes from.

mvn dependency:tree -Dincludes=org.springframework


### Download project dependencies
#
# Downloads dependencies required by the project
# into Maven's local repository.
#
# Useful in Docker builds because dependencies can be
# downloaded in an earlier Docker layer and reused
# by the Docker build cache.

mvn dependency:go-offline


### Validate the project
#
# Checks that the project is correct and that
# the required information is available.
#
# This happens before compilation.

mvn validate


### Show the effective POM
#
# Displays the FINAL POM after Maven has resolved:
#
#   - parent POMs
#   - properties
#   - dependency management
#   - plugins
#   - inherited configuration
#
# Useful when Maven behaves differently from what
# appears to be configured in pom.xml.
#
# Mental model:
#
# pom.xml
#    +
# parent POM
#    +
# inherited configuration
#    +
# dependency management
#    ↓
# Effective POM

mvn help:effective-pom


### Check for dependency updates
#
# Requires the Maven Versions Plugin.
#
# Shows newer versions available for project dependencies.

mvn versions:display-dependency-updates


### Check for plugin updates
#
# Shows newer versions available for Maven plugins.

mvn versions:display-plugin-updates


### Run the complete verification lifecycle
#
# Executes the lifecycle up to the verify phase.
#
# This includes:
#
#   validate
#   compile
#   test
#   package
#   verify
#
# Useful for CI/CD pipelines.

mvn clean verify


### Maven lifecycle
#
# The default Maven lifecycle contains phases such as:
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
#
# IMPORTANT:
#
# Each phase executes the previous phases automatically.
#
# Example:
#
#   mvn package
#
# automatically executes:
#
#   validate
#   compile
#   test
#   package
#
#
# Example:
#
#   mvn install
#
# executes everything through:
#
#   install
#
#
# Example:
#
#   mvn deploy
#
# executes everything through:
#
#   deploy


### Maven lifecycle mental model
#
#             Maven lifecycle
#                    |
#                    ↓
#                validate
#                    |
#                    ↓
#                 compile
#                    |
#                    ↓
#                   test
#                    |
#                    ↓
#                 package
#                    |
#                    ↓
#                 verify
#                    |
#                    ↓
#                 install
#                    |
#                    ↓
#                 deploy
#
#
# You normally invoke ONE phase.
#
# Maven automatically executes all required
# previous phases.


### Maven local repository
#
# Maven stores downloaded dependencies in the
# local repository.
#
# Windows:
#
#   C:\Users\<username>\.m2\repository\
#
# Linux/macOS:
#
#   ~/.m2/repository/
#
#
# Maven checks the local repository before
# downloading dependencies from remote repositories.
#
#
# Mental model:
#
# pom.xml
#    ↓
# Maven
#    ↓
# Local repository
#    ↓
# download missing dependencies
#    ↓
# build


### Maven install
#
# Builds the project and installs the generated artifact
# into the local Maven repository.
#
# This allows another LOCAL Maven project to use
# the artifact as a dependency.
#
# Typical location:
#
#   ~/.m2/repository/
#
# IMPORTANT:
#
# install does NOT mean installing the application
# onto a server.
#
# It means installing the Maven artifact
# into the local Maven repository.

mvn clean install


### Maven deploy
#
# Builds the project and publishes the artifact
# to a CONFIGURED REMOTE Maven repository.
#
# Common examples:
#
#   - Nexus
#   - Artifactory
#   - GitHub Packages
#
# Requires appropriate repository configuration
# and credentials.
#
# IMPORTANT:
#
# Maven deploy is about publishing Maven artifacts.
#
# It is NOT the same thing as deploying a Docker
# container to Kubernetes.

mvn deploy


### Maven artifact
#
# An artifact is a build output managed by Maven.
#
# For a Spring Boot application this is normally:
#
#   application.jar
#
#
# Maven coordinates identify the artifact:
#
#   groupId
#   artifactId
#   version
#
#
# Example:
#
#   com.rubenmarin
#   climbing-management-sb
#   1.0.0
#
#
# Together:
#
#   com.rubenmarin:climbing-management-sb:1.0.0


### Maven dependency scopes
#
# Common dependency scopes:
#
#   compile
#   test
#   provided
#   runtime
#
#
# compile:
#
# Available during compilation, testing and runtime.
#
#
# test:
#
# Available only for tests.
#
# Example:
#
#   JUnit
#   Mockito
#
#
# runtime:
#
# Not required for compilation but required at runtime.
#
#
# provided:
#
# Expected to be provided by the runtime environment.


### Maven dependency management
#
# Maven can manage dependency versions through:
#
#   <dependencyManagement>
#
# This is commonly used by Spring Boot's parent POM.
#
# It allows dependencies to use managed versions
# without explicitly specifying every version.
#
#
# Mental model:
#
# dependency
#      ↓
# dependencyManagement
#      ↓
# version selected


### Maven plugins
#
# Maven itself provides the lifecycle,
# while plugins perform the actual work.
#
# Examples:
#
#   maven-compiler-plugin
#   maven-surefire-plugin
#   maven-failsafe-plugin
#   maven-jar-plugin
#   spring-boot-maven-plugin
#
#
# Example:
#
#   mvn test
#
# uses Maven's test-related plugins to execute tests.


### Maven Surefire vs Failsafe
#
# Surefire:
#
# Usually runs UNIT TESTS during:
#
#   test
#
#
# Failsafe:
#
# Usually runs INTEGRATION TESTS during:
#
#   integration-test
#   verify
#
#
# Mental model:
#
#   unit tests
#       ↓
#   Surefire
#       ↓
#   test
#
#
#   integration tests
#       ↓
#   Failsafe
#       ↓
#   integration-test
#       ↓
#   verify


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
#
# Example:
#
#   mvn clean package
#
# followed by:
#
#   docker build -t climbing-management-sb:latest .
#
#
# Or Docker can execute Maven inside
# a multi-stage Dockerfile.


### Maven + multi-stage Docker build
#
# A common Docker approach is:
#
#   Stage 1:
#       Maven image
#       ↓
#       compile + test + package
#       ↓
#       JAR
#
#   Stage 2:
#       lightweight Java runtime image
#       ↓
#       copy JAR
#       ↓
#       run application
#
#
# The final image does not need Maven
# or the source code.
#
#
# Mental model:
#
#   source code
#       ↓
#   Maven build stage
#       ↓
#   application.jar
#       ↓
#   runtime image
#       ↓
#   Spring Boot container


### Maven in CI/CD
#
# A typical CI pipeline can execute:
#
#   mvn clean verify
#
#
# This:
#
#   - validates the project
#   - compiles the code
#   - runs tests
#   - packages the application
#   - executes verification
#
#
# The resulting JAR can then be used
# to build the Docker image.
#
#
# Typical pipeline:
#
#   Git push
#      ↓
#   GitHub Actions
#      ↓
#   mvn clean verify
#      ↓
#   Docker build
#      ↓
#   Docker image
#      ↓
#   GHCR
#      ↓
#   Kubernetes deployment


### Maven offline mode
#
# Runs Maven without attempting to download
# dependencies from remote repositories.
#
# Useful when dependencies are already available
# in the local Maven repository.

mvn -o clean verify


### Maven debug output
#
# -X enables detailed Maven debug output.
#
# Useful for troubleshooting complicated
# Maven build problems.

mvn -X clean verify


### Maven quiet mode
#
# -q reduces Maven output.
#
# Useful when you only want minimal output.

mvn -q test


### Maven help
#
# Shows Maven command-line help.

mvn help:help


### Show information about a Maven plugin
#
# Example:

mvn help:describe -Dplugin=spring-boot


### Maven wrapper
#
# A Maven project can include:
#
#   mvnw
#   mvnw.cmd
#
#
# The Maven Wrapper allows the project to use
# a specific Maven version without requiring
# Maven to be installed globally.
#
#
# Windows:

mvnw.cmd clean verify


# Linux/macOS:

./mvnw clean verify
#
#
# This is particularly useful in CI/CD because
# the project controls the Maven version.


### Maven Wrapper vs globally installed Maven
#
# Global Maven:
#
#   mvn
#
# Uses the Maven installation configured
# on the machine.
#
#
# Maven Wrapper:
#
#   mvnw.cmd
#
# Uses the Maven version configured by
# the project.
#
#
# Interview mental model:
#
# Global Maven:
#
#   machine controls Maven version
#
#
# Maven Wrapper:
#
#   project controls Maven version


### Maven troubleshooting workflow
#
# When a Maven build fails:
#
#
# 1. Check Maven version

mvn -version


#
# 2. Clean previous build artifacts

mvn clean


#
# 3. Run compilation

mvn compile


#
# 4. Run tests

mvn test


#
# 5. Run full verification

mvn clean verify


#
# 6. Inspect dependency tree if the problem
#    appears dependency-related

mvn dependency:tree


#
# 7. Use debug output for difficult problems

mvn -X clean verify


### Maven troubleshooting mental model
#
# Build failure
#      |
#      +------------------+
#      |                  |
#      ↓                  ↓
#   Compile            Test failure
#      |                  |
#      ↓                  ↓
#   mvn compile        mvn test
#      |
#      ↓
# Dependency problem?
#      |
#      ↓
# mvn dependency:tree
#
#
# If configuration is suspicious:
#
#   mvn help:effective-pom
#
#
# If Maven itself behaves unexpectedly:
#
#   mvn -version
#
#
# If the problem is difficult to diagnose:
#
#   mvn -X clean verify


### Maven + profiles
#
# Spring Boot profiles and Maven profiles
# are NOT the same concept.
#
#
# Spring Boot profile:
#
#   application-local.properties
#   application-docker.properties
#
# Controls Spring application configuration.
#
#
# Example:

mvn spring-boot:run "-Dspring-boot.run.profiles=local"


#
# Maven profile:
#
# Controls Maven build configuration.
#
# Defined in pom.xml using:
#
#   <profiles>
#
#
# Important:
#
# A Maven profile and a Spring profile
# can have the same name, but they are
# conceptually different mechanisms.


### Important distinction:
# Maven build vs Spring Boot runtime
#
# Maven:
#
#   compile
#   test
#   package
#
# prepares the application.
#
#
# Spring Boot:
#
#   spring-boot:run
#
# starts the application.
#
#
# Docker:
#
#   docker build
#
# creates the container image.
#
#
# Kubernetes:
#
#   kubectl apply
#
# deploys and manages the application
# in the cluster.
#
#
# Mental model:
#
#   Maven
#      ↓
#   JAR
#      ↓
#   Docker
#      ↓
#   Image
#      ↓
#   Kubernetes
#      ↓
#   Pod


### Maven lifecycle vs Docker lifecycle
#
# Maven:
#
#   source code
#       ↓
#   compile
#       ↓
#   test
#       ↓
#   package
#       ↓
#   JAR
#
#
# Docker:
#
#   Dockerfile
#       ↓
#   docker build
#       ↓
#   image
#       ↓
#   docker run
#       ↓
#   container
#
#
# Kubernetes:
#
#   Deployment
#       ↓
#   Pod
#       ↓
#   container


### Senior Backend interview questions
#
# Q: What is Maven?
#
# A:
#
# Maven is a build and dependency-management tool
# for Java projects.
#
# It can:
#
#   - manage dependencies
#   - compile source code
#   - run tests
#   - package applications
#   - execute plugins
#   - publish artifacts
#
#
# Q: What is the Maven lifecycle?
#
# A:
#
# It is a sequence of build phases such as:
#
#   validate
#   compile
#   test
#   package
#   verify
#   install
#   deploy
#
# When a later phase is executed, Maven automatically
# executes the previous phases.
#
#
# Q: What is the difference between mvn package
#    and mvn install?
#
# A:
#
# package creates the artifact, normally inside target/.
#
# install also creates the artifact but additionally
# installs it into the local Maven repository.
#
#
# Q: What is the difference between install and deploy?
#
# A:
#
# install publishes the artifact to the LOCAL
# Maven repository.
#
# deploy publishes the artifact to a CONFIGURED
# REMOTE Maven repository.
#
#
# Q: What is the difference between mvn test
#    and mvn verify?
#
# A:
#
# test runs the test phase.
#
# verify executes all phases up to verify,
# including testing, packaging and verification.
#
#
# Q: What is the difference between -DskipTests
#    and -Dmaven.test.skip=true?
#
# A:
#
# -DskipTests:
#
#   Tests are compiled but not executed.
#
#
# -Dmaven.test.skip=true:
#
#   Test compilation and test execution are skipped.
#
#
# Q: Why is dependency:tree useful?
#
# A:
#
# It shows direct and transitive dependencies,
# making it useful for identifying dependency conflicts
# and understanding where a library comes from.
#
#
# Q: What is the Maven local repository?
#
# A:
#
# It is the local cache where Maven stores downloaded
# dependencies and locally installed artifacts.
#
# On Windows it is normally:
#
#   C:\Users\<username>\.m2\repository\
#
#
# Q: What is the effective POM?
#
# A:
#
# It is the final Maven configuration after Maven has
# combined the project's POM with parent POMs,
# inherited configuration, properties and dependency management.
#
#
# Q: Why is mvn clean verify commonly used in CI?
#
# A:
#
# It starts from a clean build and executes the Maven
# lifecycle through the verify phase, including compilation,
# tests, packaging and verification.
#
#
# Q: Why use the Maven Wrapper?
#
# A:
#
# It allows the project to control the Maven version
# used to build the application, improving reproducibility
# across developers and CI environments.


### Most important Maven commands — quick reference

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

# Package without compiling tests:
mvn clean package -Dmaven.test.skip=true

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

# Validate:
mvn validate

# Effective POM:
mvn help:effective-pom

# Full verification:
mvn clean verify

# Install artifact locally:
mvn clean install

# Deploy artifact remotely:
mvn deploy

# Offline build:
mvn -o clean verify

# Debug build:
mvn -X clean verify

# Maven Wrapper on Windows:
mvnw.cmd clean verify


### FINAL MAVEN MENTAL MODEL
#
#                         pom.xml
#                            |
#                            ↓
#                         Maven
#                            |
#          +-----------------+-----------------+
#          |                 |                 |
#          ↓                 ↓                 ↓
#     Dependencies        Plugins          Lifecycle
#          |                 |                 |
#          ↓                 ↓                 ↓
#       .m2 repo       compiler/test       validate
#                                          compile
#                                          test
#                                          package
#                                          verify
#                                          install
#                                          deploy
#                                             |
#                                             ↓
#                                      application JAR
#                                             |
#                                             ↓
#                                           Docker
#                                             |
#                                             ↓
#                                      Docker image
#                                             |
#                                             ↓
#                                        Kubernetes
#                                             |
#                                             ↓
#                                            Pod
#
#
# KEY INTERVIEW RULES:
#
# 1. Maven manages Java builds and dependencies.
#
# 2. mvn package creates the application artifact.
#
# 3. mvn install puts the artifact in the local
#    Maven repository.
#
# 4. mvn deploy publishes the artifact to a configured
#    remote Maven repository.
#
# 5. Later lifecycle phases automatically execute
#    previous phases.
#
# 6. mvn clean removes generated build output.
#
# 7. dependency:tree is useful for dependency troubleshooting.
#
# 8. help:effective-pom shows Maven's final resolved configuration.
#
# 9. -DskipTests skips test execution but still compiles tests.
#
# 10. Maven Wrapper makes Maven builds more reproducible.
#
# 11. Maven creates the JAR.
#
# 12. Docker packages the JAR into an image.
#
# 13. Kubernetes runs the containerized application.