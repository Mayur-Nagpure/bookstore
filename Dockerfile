FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/bookstore.jar /app/bookstore.jar
ENTRYPOINT ["java", "-jar", "/app/bookstore.jar"]

