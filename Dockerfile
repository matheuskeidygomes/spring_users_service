FROM openjdk:21-jdk-alpine
WORKDIR /usr/src/app
COPY target/*.jar /usr/src/app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
