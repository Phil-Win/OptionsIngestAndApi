node {
    def app
    stage("Setting up") {
        checkout scm
        sh "./gradlew build"
    }

    stage("Create docker image") {
        docker.build "philwin/options-ingest-and-api" + ":${BUILD_NUMBER}"
    }
}
