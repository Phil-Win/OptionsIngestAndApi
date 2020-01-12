import java.text.SimpleDateFormat

node() {
    def customImage
    def latestImage
    def longTermSupportImage
    def date    =   new Date()
        
    stage("Setting up") {
        deleteDir()
        checkout scm
        sh "./gradlew build"
    }
    stage("Create Docker image") {
        def dateFormat =   new SimpleDateFormat("yyyy_MM_dd")
        customImage = docker.build "philwin/options-ingest-and-api" + ":${env.BRANCH_NAME}_${BUILD_NUMBER}_date_${dateFormat.format(date)}"
        latestImage = docker.build "philwin/options-ingest-and-api" + ":latest"
        if ("${env.GIT_BRANCH}" == "master") {
            longTermSupportImage = docker.build "philwin/options-ingest-and-api" + ":lts"
        }
    }
    stage ("Publish Docker Image") {
        customImage.push()
        latestImage.push()
        if ("${env.GIT_BRANCH}" == "master") {
            longTermSupportImage.push()
        }
    }
}
