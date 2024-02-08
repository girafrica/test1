import java.text.SimpleDateFormat

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Checkout2') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh('git fetch https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag  --prune --prune-tags')              
                    }                
                }            
            }
        }

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

        stage('List tags') {
            steps {
                script {
                  int x = 1;
  
                  lastTag = sh script: """git tag --list ${version}* --sort=-version:refname | sort -r | head -1 | grep -oE '[0-9]+\044'""".trim(), returnStdout: true
                  lt = lastTag.trim()  // the .trim() is necessary
                  echo "lastTag: " + lt
                  int lt = lt.toInteger()

                  newtag = lt + x

                  echo newtag.toString()
                }
            }
        }  

        stage('Create tag') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh("git tag -f -a ${version}.${newtag} -m '${version}.${newtag}'")
                    sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag --tags -f')
                    }
                }
            }
        }
    }
}