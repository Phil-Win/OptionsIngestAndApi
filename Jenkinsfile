node("docker_slave") {
    def app
    stage("Setting up") {
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        def customImage = docker.build "philwin/options-ingest-and-api" + ":${BUILD_NUMBER}"
        def latestImage = docker.build "philwin/options-ingest-and-api" + ":latest"
    }
    stage ("Publish Docker Image") {
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            sh "docker login -u ${USERNAME} -p ${PASSWORD}"
            customImage.push()
            latestImage.push()
        }
    }
}
