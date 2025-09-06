# Use Eclipse Temurin OpenJDK as base
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR file
COPY target/Healthcare-0.0.1-SNAPSHOT.jar app.jar

# Expose port (important for Render)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
