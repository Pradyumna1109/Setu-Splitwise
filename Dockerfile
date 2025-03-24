FROM gradle:8.13-jdk17 AS build
WORKDIR /app
COPY . /app
COPY gradlew gradlew
COPY gradle/ ./gradle/
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app /app
EXPOSE 8080
ENTRYPOINT ["./gradlew", "bootRun", "--no-daemon"]