FROM openjdk:21
EXPOSE 9090
ARG JAR_FILE=build/libs/labKtor.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]