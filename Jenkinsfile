node("docker_slave") {
    def customImage
        
    stage("Setting up") {
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        customImage = docker.build "himynameisfil/options-ingest-and-api" + ":${BUILD_NUMBER}"
    }
    stage ("Publish Docker Image") {
        customImage.push()
    }
}
