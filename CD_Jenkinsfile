def version = "${env.BUILD_NUMBER}"

pipeline {
    agent any

    stage('Checkout') {
        scm checkout
    }

    stage 'Build'
    sh "./gradlew -Pversion=${version} build"

}

