FROM openjdk:8-jdk-alpine
MAINTAINER koera
COPY target/tictactoe-0.0.1-SNAPSHOT.jar tictactoe-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/tictactoe-0.0.1-SNAPSHOT.jar"]