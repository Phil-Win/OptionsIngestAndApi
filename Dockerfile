FROM gradle:4.7.0-jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:8-jdk-alpine
RUN apk add --no-cache bash
MAINTAINER himynameisfil@gmail.com
RUN mkdir -p /data/daily_stock_data_landing_zone
COPY --from=build /home/gradle/src/build/libs/*.jar /app/options.jar
ENTRYPOINT ["java","-jar","/app/options.jar", "bootRun"]
