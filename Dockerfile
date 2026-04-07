# -----------------------
# STAGE 1: Build
# -----------------------
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy Maven project files first for dependency caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn dependency:go-offline -B

# Copy full source code
COPY src ./src

# Build the JAR without tests
RUN mvn clean package -DskipTests

# -----------------------
# STAGE 2: Run
# -----------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install PostgreSQL client (optional, useful for debugging DB)
RUN apk add --no-cache postgresql-client bash

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port Render uses
EXPOSE 8080

# Set environment variables for your live Supershop DB
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d7900t6a2pns73drlgqg-a.singapore-postgres.render.com:5432/supershop
ENV SPRING_DATASOURCE_USERNAME=weldon
ENV SPRING_DATASOURCE_PASSWORD=VEY85S2ZJs6fEThbu62DDYpuFI1PBFID
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_JPA_SHOW_SQL=true

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]