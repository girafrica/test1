pipeline {
    stages {
        stage('Build Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                sh("git tag -a some_tag -m 'Jenkins'")
                sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag --tags')              
          }
            }
        }

        stage('Git Push To Origin') {

        steps {
     
            withCredentials([usernamePassword(credentialsId: your_credentials, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
              

                sh "git tag 555"
                sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag 555"
            }
        }
    }
    }
}