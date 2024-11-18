# Use an official Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# # Set the working directory in the container
WORKDIR /app
# RUN cd /app/target
# RUN ls -la

# # Copy the jar file from the build stage
COPY --from=build /app/target/isKosher-0.0.1-SNAPSHOT.jar app.jar

# # Expose the port the application runs on
EXPOSE 8080

# create env for supabase
ENV DB_SUPABASE_PASSWORD=${SUPABASE_PASSWORD}


# # Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]