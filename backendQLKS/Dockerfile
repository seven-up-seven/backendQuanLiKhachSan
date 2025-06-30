# 1. Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy maven wrapper & config trước để tận dụng cache
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy code còn lại
COPY src src

# Build project
RUN chmod +x mvnw \
 && ./mvnw -B -DskipTests clean package

# 2. Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy jar từ build stage
COPY --from=build /app/target/backendQLKS-0.0.1-SNAPSHOT.jar app.jar

# Khởi chạy
ENTRYPOINT ["java","-jar","app.jar"]
