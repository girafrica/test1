import java.text.SimpleDateFormat
library 'shared'

pipeline {
    agent any
    stages {
        // stage('Checkout') {
        //     steps {
        //         checkout scm
        //     }
        // }

        // stage('Checkout2') {
        //     steps {
        //         script {
        //             checkoutCode()              
        //             }                
        //         }            
        //     }

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
  
                    //lastTag = sh script: """git tag --sort=-version:refname | head -1 | grep -oE '[0-9]+\044'""".trim(), returnStdout: true
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                        dir ('savetag'){
                            sh (' ls -l ')
                            cloneToLocation("https://github.com/girafrica/release-tags", 'github-app')
                            //sh (' git pull https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags ')
                            lastTag = sh script: """ls -t | head -1 | grep -oE '[0-9]+\044'""".trim(), returnStdout: true
                            sh (' ls -l ')
                            deleteDir()
                        }
                    }
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
                    createTag(version, newtag)
                }
            }
        }

        stage('Save tag') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh (' ls -l ')
                        // dir ('foo'){
                        //     sh (' ls -l ')
                        //     //sh (' git config --global pull.rebase false ')
                        //     sh (' git fetch https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags ')
                        //     //sh (' git pull https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags main --allow-unrelated-histories')
                        //     def readContent = "${version}.sbt"
                        //     writeFile file: "${version}.sbt", text: readContent+"\r\nversion := 1.0.${env.BUILD_ID}"
                        //     sh (" git add -A")
                        //     sh (' git commit -am "Updated version number"')
                        //     sh (' git pull https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags main --allow-unrelated-histories')
                        //     sh (' git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags HEAD:main')
                        // }

                        dir ('savetag2'){
                            cloneToLocation("https://github.com/girafrica/release-tags", 'github-app')
                            //def readContent = "${version}.${newtag}"
                            writeFile file: "${version}.${newtag}"
                            sh (" git add -A")
                            sh (" git commit -am 'Updated version number to ${version}.${newtag}'")
                            sh (' ls -l ')
                            sh (' git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags HEAD:main')
                        }
                    }
                }
            }
        }
    }
}