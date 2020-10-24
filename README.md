# Postcode Service

This service is responsible for maintaining postcodes and lookup

## Local Development

### Run Unit test

``./gradlew clean test``

Code coverage can be checked in build/reports/jacoco/test/html/index.html

### Running App
Start Dependencies like postgres DB

``sudo /usr/local/bin/docker-compose up -d``

``sudo /usr/local/bin/docker-compose down``

Start Application with dev profile

``./gradlew bootRun --args='--spring.profiles.active=dev'``

Check Actuator Health endpoint

``curl http://localhost:8090/postcode/actuator/health``

### Test using swagger-ui
``http://localhost:8090/postcode/swagger-ui.html``

### Swagger for building UI
``http://localhost:8090/postcode/v2/api-docs``

## Create Infra

Jenkinsfile.infra can be used to run below command for creating ec2 instance

``aws cloudformation deploy --stack-name postcode-ec2 --template-file ./ec2-infra.yaml --parameter-overrides SSHKey=ec2web``

## CI

Jenkinsfile can be used in Multibranch pipeline setup on Jenkins to build project and create artifacts

Build Artifacts

``./gradlew clean build``

Build Docker Images

``sudo docker build . -t postcode-service:test``

Login to docker hub / artifactory

``sudo docker login``

Tag  docker images for docker hub / artifactory

``sudo docker tag  postcode-service:test docker.io/mgupta82/postcode-service:latest``

Publish docker image with latest tag

``sudo docker push docker.io/mgupta82/postcode-service:latest``

## CD
Jenkinsfile.deploy is used to run docker images created by CI along with its dependencies.

Login to ec2 instance

````ssh -i ~/.ssh/ec2web.pem ec2-user@postcode.mukeshgupta.info````

Start Dependencies and applications (ST Env)

``sudo docker run -d --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=secret postgres:12``

``sudo docker run -d --name postcodeapp -p 80:8090 -e SPRING_PROFILES_ACTIVE=st mgupta82/postcode-service``

### SIT and PROD env

We should be using docker orchestration platform like Kubernetes or AWS Elastic Container Service




