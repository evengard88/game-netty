#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY src /home/logic-service/src
COPY pom.xml /home/logic-service
RUN mvn -f /home/logic-service/pom.xml clean compile assembly:single

#
# Package stage
#

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /home/logic-service/target/logic-service-0.1.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","--enable-preview", "-cp","/usr/local/lib/app.jar", "App", "localhost", "6379"]