FROM maven:3.9-amazoncorretto-17-alpine AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

#Финальный образ
FROM openjdk:17-jdk-alpine
WORKDIR /app

#COPY --from=build /app/target/CarsDefinition-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
