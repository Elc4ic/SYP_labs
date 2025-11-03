FROM openjdk:21
EXPOSE 8080
ARG JAR_FILE=build/libs/lab3-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]