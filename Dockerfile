FROM openjdk:8-jdk-alpine
EXPOSE 8090
VOLUME /tmp
ARG JAR_FILE=build/libs/postcode-service-*.jar
COPY ${JAR_FILE} postcode-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/postcode-service.jar"]