FROM gradle:6.2.0-jdk11 AS build
WORKDIR /home/gradle
COPY . /home/gradle
RUN gradle clean build

FROM adoptopenjdk:11-jre-hotspot
COPY --from=build /home/gradle/build/libs/*.jar ctclientapp.jar
ENTRYPOINT ["java","-jar","/ctclientapp.jar"]