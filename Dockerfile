# ===== Step 1: Use a lightweight JDK 17 image =====
FROM eclipse-temurin:17-jdk-jammy

# ===== Step 2: Set working directory inside container =====
WORKDIR /app

# ===== Step 3: Copy built JAR to container =====
COPY target/greenfield-school-0.0.1-SNAPSHOT.jar app.jar

# ===== Step 4: Expose port (must match your Spring Boot server.port) =====
EXPOSE 8080

# ===== Step 5: Run the Spring Boot app =====
ENTRYPOINT ["java","-jar","app.jar"]