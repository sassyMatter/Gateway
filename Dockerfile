FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app
#VOLUME /hostpipe
# Copy the JAR file into the container
COPY target/Gateway-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080:8080


# Command to run the application
CMD ["java", "-jar", "demo.jar"]