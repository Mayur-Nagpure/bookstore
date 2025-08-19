# Stage 1: Build with Maven
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with lightweight JRE image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/bookstore.jar /app/bookstore.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/bookstore.jar"]
