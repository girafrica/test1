import java.text.SimpleDateFormat

pipeline {
    agent any
    stages {
        stage('Create version') {
            steps {
                script {
                    currentDateTime = sh script: """
                        date +"v%Y.%V"
                        """.trim(), returnStdout: true
                    version = currentDateTime.trim()  // the .trim() is necessary
                    echo "version: " + version
                }
            }
        }    

        stage('Create tag') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh("git tag -a ${version} -m '${version}'")
                    sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag --tags')              
                    }
                }
            }
        }
    }
}