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
        def dateFormate =   new SimpleDateFormat("yyyy_MM_dd")
        customImage = docker.build "philwin/options-ingest-and-api" + ":${dateFormat.format(date)}_jenkins_bn_${BUILD_NUMBER}"
        latestImage = docker.build "philwin/options-ingest-and-api" + ":latest"
    }
    stage ("Publish Docker Image") {
        customImage.push()
        latestImage.push()
    }
}
