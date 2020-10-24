#Postcode Service

This service is  responsible for maintaining postcodes and lookup

##CI

Jenkinsfile can be used in Multibranch pipeline setup on Jenkins

##CD
deploy.Jenkinsfile is used to run docker images created by CI along with its dependencies.

## Running Test locally

``./gradlew clean test``

Code coverage can be checked in build/reports/jacoco/test/html/index.html

## Running App locally
Start Dependencies like postgres DB

``sudo docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=secret postgres:12``

Start Application with dev profile

``./gradlew bootRun --args='--spring.profiles.active=dev'``

Check Actuator Health endpoint

``curl http://localhost:8090/postcode/actuator/health``

## Build Project

Build Artifacts

``./gradlew clean build``

Build Docker Images

``sudo docker build . -t postcode-service:test``

Login to docker hub / artifactory

``sudo docker login``

Tag created docker images for docker hub / artifactory

``sudo docker tag  postcode-service:test docker.io/mgupta82/postcode-service:latest``

Publish docker image with latest tag

``sudo docker push docker.io/mgupta82/postcode-service:latest``

## Run Project

Start Depedencies and application with ST(System Test) profile

``sudo /usr/local/bin/docker-compose up -d``

``sudo /usr/local/bin/docker-compose down``

