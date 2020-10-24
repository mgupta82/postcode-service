#Postcode Service

## Running Test

``./gradlew clean test``

Code coverage can be checked in build/reports/jacoco/test/html/index.html

## Build Project

``./gradlew clean build``

``sudo docker build . -t postcode-service:test``

``sudo docker login``

``sudo docker tag  postcode-service:test docker.io/mgupta82/postcode-service:latest``

``sudo docker push docker.io/mgupta82/postcode-service:latest``

## Run Project

``sudo /usr/local/bin/docker-compose up -d``

``sudo /usr/local/bin/docker-compose down``