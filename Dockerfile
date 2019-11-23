FROM gradle:5.6.4-jdk8 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle ./build.gradle .
RUN gradle resolveDependencies

COPY --chown=gradle:gradle . .
RUN gradle build

FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
COPY --from=build /home/gradle/src/build/libs/cinematics-*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]