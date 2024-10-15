FROM alpine/java:21-jdk
WORKDIR /usr/src/app
COPY target/*.jar /usr/src/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
