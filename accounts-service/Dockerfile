# uses a base image containing the Java runtime
FROM amazoncorretto:25.0.4-alpine3.24

# copies the application JAR into the Docker image
COPY build/libs/accounts-service-0.0.1-SNAPSHOT.jar accounts-service-0.0.1-SNAPSHOT.jar

# runs the Spring Boot application
ENTRYPOINT ["java", "-jar", "accounts-service-0.0.1-SNAPSHOT.jar"]
