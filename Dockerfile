##Stage 1
## initialize build and set base image for first stage
FROM docker.io/maven:3.8.4-openjdk-17 as stage1

## speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Dhttps.protocols=TLSv1.2 -Dmaven.repo.local=.m2/repository -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=WARN -Dorg.slf4j.simpleLogger.showDateTime=true -Djava.awt.headless=true"
ARG MAVEN_CLI_OPTS="--batch-mode --errors --fail-at-end --show-version -DinstallAtEnd=true -DdeployAtEnd=true"

## set working directory
RUN mkdir -p /opt/demo/
WORKDIR /opt/demo

## copy just pom.xml
ADD ./pom.xml /opt/demo/pom.xml

## go-offline using the pom.xml
#RUN mvn $MAVEN_CLI_OPTS dependency:go-offline

## copy your other files
ADD ./src /opt/demo/src

## compile the source code and package it in a jar file
#RUN mvn compile
RUN mvn $MAVEN_CLI_OPTS clean install -Dmaven.test.skip=true

#Stage 2 - Java
# set base image for second stage
FROM docker.io/openjdk:17-alpine3.14

# set deployment directory
RUN mkdir -p /temp
WORKDIR /temp

# copy over the built artifact from the maven image
COPY --from=stage1 /opt/demo/target/*.jar /temp/aggregator-service.jar

#EXPOSE 8080
ENTRYPOINT ["java","-jar","/temp/aggregator-service.jar"]
