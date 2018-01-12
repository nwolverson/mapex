FROM node as buildui

WORKDIR /usr/src/app

COPY frontend/ /usr/src/app/

RUN npm i && npm run build

FROM maven:3.5.2-jdk-8 as build
WORKDIR /usr/src/app
ENV MAVEN_OPTS=-Dmaven.repo.local=../m2repo/

COPY pom.xml .
COPY libs/ libs/
RUN mvn install:install-file -Dfile=libs/javastrava-api-2.0.0-SNAPSHOT.jar -DgroupId=com.github.danshannon -DartifactId=javastrava-api -Dversion=2.0.0-SNAPSHOT

COPY src/ src/
COPY --from=buildui /usr/src/app/build/ /usr/src/app/src/main/resources/static/
RUN mvn install



FROM openjdk:8u151-jre

COPY --from=build /usr/src/app/target/mapex-1.0-SNAPSHOT.jar App.jar

COPY wait-for-it.sh .

EXPOSE 8080

CMD [ "java", "-jar","App.jar"]
