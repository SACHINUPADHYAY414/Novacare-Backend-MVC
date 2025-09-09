# ----------- Step 1: Build the application using Maven ----------- #
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only dependency-related files first to cache maven dependencies
COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the project and skip tests to speed up the process
RUN mvn clean package -DskipTests

# ----------- Step 2: Create a lightweight production image ----------- #
FROM eclipse-temurin:21-jdk

WORKDIR /app

# ✅ Copy the shaded jar file
# COPY --from=builder /app/target/healthcare-0.0.1-SNAPSHOT-shaded.jar app.jar
COPY --from=builder /app/target/healthcare-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:InitialRAMPercentage=50.0 \
               -XX:+HeapDumpOnOutOfMemoryError \
               -XX:HeapDumpPath=/tmp \
               -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
