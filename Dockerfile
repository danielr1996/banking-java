FROM gradle:jdk8 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build
#----
FROM openjdk:8-jre-slim
MAINTAINER Daniel Richter
EXPOSE 9090

COPY --from=build /home/gradle/src/build/libs/banking-java-1.0-SNAPSHOT.jar /opt/banking/banking.jar
WORKDIR /opt/banking
CMD ["java","-jar","banking.jar"]
