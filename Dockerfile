FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/vocab_view-0.0.1-SNAPSHOT.jar vocab_view.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "vocab_view.jar"]