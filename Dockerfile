FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash
MAINTAINER himynameisfil@gmail.com
RUN mkdir -p /data/daily_stock_data_landing_zone
COPY build/libs/*.jar options.jar
ENTRYPOINT ["java","-jar","options.jar", "bootRun"]
