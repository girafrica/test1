def call() {
    withCredentials([usernamePassword(credentialsId: 'github-app', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
        sh("git tag -a some_tag -m 'Jenkins'")
        sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/girafrica/tag --tags')
    }
}