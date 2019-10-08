node("docker_slave") {
    def customImage
    def latestImage
        
    stage("Setting up") {
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        customImage = docker.build "philwin/options-ingest-and-api" + ":${BUILD_NUMBER}"
        latestImage = docker.build "philwin/options-ingest-and-api" + ":latest"
    }
    stage ("Publish Docker Image") {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            sh "docker login -u ${USERNAME} -p ${PASSWORD}"
            customImage.push()
            latestImage.push()
        }
    }
}
