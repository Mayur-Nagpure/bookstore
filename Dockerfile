FROM eclipse-temurin:17-jre
EXPOSE 8080
COPY target/bookstore.jar /bookstore.jar
ENTRYPOINT ["java", "-jar", "/bookstore.jar"]
