#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean compile assembly:single

#
# Package stage
#
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /home/app/target/RPS-server-0.5-jar-with-dependencies.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar", "8080", "redis", "6379"]
