############# BUILD STAGE ##############

FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /usr/src/app
COPY pom.xml /usr/src/app
RUN mvn dependency:go-offline   
COPY src /usr/src/app/src
RUN mvn clean package -DskipTests

############# RUN STAGE ##############

FROM alpine/java:21-jdk AS run

WORKDIR /usr/src/app
COPY --from=build /usr/src/app/target/*.jar /usr/src/app/app.jar
EXPOSE 8080
