FROM maven:3.8.3-openjdk-22 AS build
COPY . .
RUN mvn clean package -DskipTest

FROM openjdk:22-jdk-slim
copy --from=build /target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]