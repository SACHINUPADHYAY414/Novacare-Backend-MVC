# Base image
FROM eclipse-temurin:21-jdk

# Create app directory
WORKDIR /app

# Copy built jar file
COPY target/Healthcare-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
