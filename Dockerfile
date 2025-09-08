# ----------- Step 1: Build the application using Maven ----------- #
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy all project files into the container
COPY . .

# Build the project and skip tests to speed up the process
RUN mvn clean package -DskipTests


# ----------- Step 2: Create a lightweight production image ----------- #
FROM eclipse-temurin:21-jdk

# Set working directory in the production image
WORKDIR /app

# Copy the built jar file from the builder image
COPY --from=builder /app/target/healthcare-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (important for Docker/Render)
EXPOSE 8080

# Set JVM options for memory management inside Docker container
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:InitialRAMPercentage=50.0 \
               -XX:+HeapDumpOnOutOfMemoryError \
               -XX:HeapDumpPath=/tmp \
               -XX:+UseG1GC"

# Start the application with the defined JVM options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
