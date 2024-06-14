# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory
WORKDIR /app

COPY src ./src
# Copy the pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests
#RUN mvn spring-boot:build-image -DskipTests

# Stage 2: Use the built image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built image from the previous stage
COPY --from=builder /app/target/authentication-0.0.1-SNAPSHOT.jar authentication-1.0.0.jar

# Run the application
ENTRYPOINT ["java", "-jar", "authentication-1.0.0.jar"]