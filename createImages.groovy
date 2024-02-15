library 'shared'
def map = [
        Bob  : 42,
        Alice: 54,
        Max  : 33
]

pipeline {
    agent any
    options {
        skipDefaultCheckout()
    }

    stages {
    
        map.each { entry ->


            stage('Checkout 1') {
                steps {
                    echo "Checkout 1"
                }
            }
        
            stage('Checkout 2') {
                steps {
                    echo "Checkout 2"
                }
            }    
        
            stage('Checkout 3') {
                steps {
                    echo "Checkout 3"
                }
            }    
        }
    
    
    
    }




}