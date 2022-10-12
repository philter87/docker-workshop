# We use a gradle image.
# Dockerfile is inspired by: https://codefresh.io/docs/docs/learn-by-example/java/gradle/
FROM gradle:7.5-jdk17-alpine as build

COPY --chown=grade:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17.0.2-slim-bullseye
EXPOSE 8080

RUN mkdir /app

RUN useradd -ms /bin/bash app-user

COPY --from=build --chown=app-user:app-user /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

# "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom",
ENTRYPOINT ["java","-XX:+UseContainerSupport" ,"-jar","/app/spring-boot-application.jar"]