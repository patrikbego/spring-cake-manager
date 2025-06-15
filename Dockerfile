# Stage 1: Build the application using Gradle
FROM gradle:8.7.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build -x test

# Stage 2: Create minimal runtime image using a JDK base
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
EXPOSE 8080

# Copy the built jar file from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Run the app
ENTRYPOINT ["java","-jar","/app.jar"]
