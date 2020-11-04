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

## Auth Provider - KeyCloak

### Create initial Admin User
Open URL => http://localhost:8070/auth/
We have already created while runnning docker-compose

### Create test realm
you can create a test realm (tenant) for testing. But for dev purpose we will use alreay created tenant MASTER

Open URL => http://localhost:8070/auth/admin

### Create Application client
We need to create Application client of type CLIENT_CREDENTIALS

Enable "Service Accounts" (i.e CLIENT_CREDNETIALS)

Change Access Type to CONFIDENTIAL to generate Client Secret

Disable all other flows like STANDARD, IMPLICIT, DIRECT ACCESS

Certs endpoint can be accessed => curl --location --request GET 'http://localhost:8070/auth/realms/master/protocol/openid-connect/certs'

Token can be retrieved using 

curl --location --request POST 'http://localhost:8070/auth/realms/master/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'client_id=postcode-client' \
--data-urlencode 'client_secret=d43deb37-f9e9-4cee-8df3-742e3933cde0' \
--data-urlencode 'scope=profile'



