# Use a lightweight base image for Java applications
FROM openjdk:17-slim

# Set working directory
WORKDIR /app

# Install dependencies using Maven (optional, adjust based on your environment)
RUN apt-get update && apt-get install -y maven

# Copy the application code from the context (adjust based on your project structure)
COPY ./target/pet-viewer-1.0.0-SNAPSHOT.jar app.jar
COPY . /app	

# Build the application using Maven
RUN mvn clean install -DskipTests

# Define the final JAR file location (adjust if the JAR name is different)
ARG JAR_FILE=target/*.jar

# Copy the final JAR file to a specific location (optional)
COPY $JAR_FILE app.jar

# Expose the port used by your application (assuming port 8080)
EXPOSE 8080

# Start the application using the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

# Alternative: Use CMD instead of ENTRYPOINT (optional)
# CMD ["java", "-jar", "app.jar"]
