FROM amazoncorretto:21
WORKDIR /app
COPY target/*.jar backend-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]