FROM gradle:8.14.3-jdk23 AS builder

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle . .

RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:23-jre-jammy

WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]