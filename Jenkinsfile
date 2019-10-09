node("docker_slave") {
    def customImage
    def latestImage
        
    stage("Setting up") {
        delete()
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        customImage = docker.build "philwin/options-ingest-and-api" + ":${BUILD_NUMBER}"
        latestImage = docker.build "philwin/options-ingest-and-api" + ":latest"
    }
    stage ("Publish Docker Image") {
        customImage.push()
        latestImage.push()
    }
}
