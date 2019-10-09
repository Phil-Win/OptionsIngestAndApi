import java.text.SimpleDateFormat

node("docker_slave") {
    def customImage
    def latestImage
    def date    =   new Date()
        
    stage("Setting up") {
        delete()
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        def dateFormat =   new SimpleDateFormat("yyyy_MM_dd")
        customImage = docker.build "philwin/options-ingest-and-api" + ":jbn_${BUILD_NUMBER}_date_${dateFormat.format(date)}"
        latestImage = docker.build "philwin/options-ingest-and-api" + ":latest"
    }
    stage ("Publish Docker Image") {
        customImage.push()
        latestImage.push()
    }
}
