FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=build/libs/onlinestore-api-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

# cp spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]