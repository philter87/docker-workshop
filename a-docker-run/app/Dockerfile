# We use a gradle image.
# Dockerfile is inspired by: https://codefresh.io/docs/docs/learn-by-example/java/gradle/
FROM gradle:7.5-jdk17-alpine as build

COPY --chown=grade:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre-centos7
EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

# "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom",
ENTRYPOINT ["java","-XX:+UseContainerSupport" ,"-jar","/app/spring-boot-application.jar"]