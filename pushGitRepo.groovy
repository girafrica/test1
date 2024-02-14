import java.text.SimpleDateFormat
library 'shared'

pipeline {
    agent any
    options {
        skipDefaultCheckout()
    }
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
                    currentDateTime = sh script: """date +"v%Y.%V" """.trim(), returnStdout: true
                    version = currentDateTime.trim()  // the .trim() is necessary
                }
            }
        }    

        // stage('List tags') {
        //     steps {
        //         script {
        //             int x = 1;
  
        //             //lastTag = sh script: """git tag --sort=-version:refname | head -1 | grep -oE '[0-9]+\044'""".trim(), returnStdout: true
        //             //withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        //                 dir ('savetag'){    
        //                     cloneToLocation("https://github.com/girafrica/release-tags", 'github-app')
        //                     //sh (' git pull https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags ')
        //                     lastTag = sh script: """ls -t | head -1 | grep -oE '[0-9]+\044'""".trim(), returnStdout: true
        //                     sh (' ls -l ')
        //                 }
        //             //}
        //             lt = lastTag.trim()  // the .trim() is necessary
        //             echo "lastTag: " + lt
        //             int lt = lt.toInteger()

        //             newtag = "${env.BUILD_ID}"

        //             echo newtag.toString()
        //         }
        //     }
        // }

        stage('Create tag') {
            steps {
                script {
                    createTag(version)
                }
            }
        }

        stage('Save tag') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
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

                        cloneToLocation("https://github.com/girafrica/release-tags", 'github-app')
                        //def readContent = "${version}.${newtag}"
                        // def newtag = "${env.BUILD_ID}"
                        // tag = newtag.toString()

                        // echo tag 

                        if (!fileExists('releases')) {
                            writeFile file: 'releases', text: "Releases:"
                        }

                        def readContent = readFile 'releases'

                        writeFile file: 'releases', text: readContent+"\r\n${version}.${env.BUILD_ID}"
                        sh (" git add -A")
                        sh (" git commit -am 'Updated version number to ${version}.${env.BUILD_ID}'")
                        sh (' git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/release-tags HEAD:main')
                    }
                }
            }
        }
    }
}