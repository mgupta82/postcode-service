pipeline {
    agent any
    parameters {
        string(
                name: 'SHORT_GIT_COMMIT',
                defaultValue: '',
                description: 'Git commit hash used to promote'
        )
    }
    stages {
        stage('Deploy to Production') {
            steps {
                sh 'sudo /usr/local/bin/docker-compose up -d'
            }
        }
    }
 }
