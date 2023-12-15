#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY src /home/logic-service/src
COPY pom.xml /home/logic-service
RUN mvn -f /home/logic-service/pom.xml clean compile package

#
# Package stage
#

FROM registry.access.redhat.com/ubi8/openjdk-21

ENV LANGUAGE='en_US:en'


# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --from=build --chown=185 /home/logic-service/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /home/logic-service/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /home/logic-service/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /home/logic-service/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV AB_JOLOKIA_OFF=""
ENV JAVA_OPTS_APPEND="--enable-preview -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]

