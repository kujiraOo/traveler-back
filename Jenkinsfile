pipeline {
    agent { docker 'maven:3.3.3' }
    stages {
        stage('build') {
            steps {
                sh 'mvn package'
            }
        }

        stage('test') {
            steps {
                echo 'Tests should be run here'
            }
        }

        stage('deploy') {
            steps {
                echo 'Deployment script if any should be run here'
            }
        }
    }
}