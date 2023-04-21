FROM gradle:jdk17-focal AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test -i

FROM openjdk:17.0.2-jdk-slim-buster
EXPOSE 8888
COPY --from=builder /app/build/libs/SimpleCloudService-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
