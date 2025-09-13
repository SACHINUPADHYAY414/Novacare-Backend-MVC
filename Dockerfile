# ---------- Step 1: Build the application ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Leverage Docker layer caching: copy only pom and .mvn first
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Now copy the source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ---------- Step 2: Lightweight runtime image ----------
FROM gcr.io/distroless/java21-debian12:nonroot

WORKDIR /app

# Copy only the final JAR from the builder stage
COPY --from=builder /app/target/healthcare-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (used by Spring Boot)
EXPOSE 8080

# Run the app with optimized JVM flags
ENTRYPOINT ["java", 
  "-XX:+UseContainerSupport", 
  "-XX:MaxRAMPercentage=75.0", 
  "-XX:InitialRAMPercentage=50.0", 
  "-XX:+UseG1GC", 
  "-XX:+HeapDumpOnOutOfMemoryError", 
  "-XX:HeapDumpPath=/tmp", 
  "-jar", "app.jar"]

