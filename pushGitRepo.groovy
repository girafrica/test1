import java.text.SimpleDateFormat

pipeline {
    agent any
    stages {
        stage('Create tag') {
            steps {
                script {
                    def date = new Date()
                    sdf = new SimpleDateFormat("dd-MM-yyyy")
                    println("Date is: "+sdf.format(date))
                    def TAG="tag-${sdf.format(date)}"
                    echo "TAG is : ${TAG}"
                    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh("git tag -a ${TAG} -m '${TAG}'")
                    sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag --tags')              
                    }
                }
            }
        }
    }
}