FROM maven:3.9.9-eclipse-temurin-22 AS build
COPY . .
RUN mvn clean package -DskipTest

FROM eclipse-temurin:22.0.2_9-jre
copy --from=build /target/ProdutoApplication-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]