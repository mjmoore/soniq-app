FROM openjdk:8

COPY src soniq-app/src
COPY gradle soniq-app/gradle

COPY gradlew build.gradle settings.gradle soniq-app/

WORKDIR soniq-app

EXPOSE 8080
RUN ./gradlew test build

ENTRYPOINT ./gradlew bootRun