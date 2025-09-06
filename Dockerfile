# Step 1: Use Maven to build the application
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy all source code
COPY . .

# Build the project (skipping tests is optional)
RUN mvn clean package -DskipTests

# Step 2: Run the built JAR in a smaller JDK image
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=builder /app/target/Healthcare-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (important for Render)
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
