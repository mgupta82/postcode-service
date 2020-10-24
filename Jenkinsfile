pipeline {
    agent any

    triggers {
        pollSCM('*/5 * * * *')
    }

    environment {
        SHORT_GIT_COMMIT = sh (script: 'echo $(git rev-parse --short HEAD)',returnStdout: true).trim()
    }

    stages {
        stage('Compile') {
            steps {
                sh 'echo ${SHORT_GIT_COMMIT}'
                gradlew('clean', 'classes')
            }
        }
        stage('Unit Tests') {
            steps {
                gradlew('test')
            }
            post {
                always {
                    junit '**/build/test-results/test/TEST-*.xml'
                }
            }
        }
        stage('Assemble') {
            steps {
                gradlew('assemble')
            }
        }
        stage('Containerise') {
            steps {
                sh "docker build . -t postcode-service:${SHORT_GIT_COMMIT}"
            }
        }
    }

    post {
        failure {
            mail to: 'mukeshsgupta@gmail.com', subject: 'Build failed', body: 'Please fix!'
        }
    }

}
def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}

