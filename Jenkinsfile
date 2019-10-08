node("docker_slave") {
    def customImage
    def latestImage
        
    stage("Setting up") {
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        customImage = docker.build "himynameisfil/options-ingest-and-api" + ":${BUILD_NUMBER}"
        latestImage = docker.build "himynameisfil/options-ingest-and-api" + ":latest"
    }
    stage ("Publish Docker Image") {
        customImage.push()
        latestImage.push()
    }
}
