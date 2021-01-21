FROM openjdk:12
VOLUME /tmp
EXPOSE 8090
ADD ./target/soaint-test-0.0.1-SNAPSHOT.jar soaint-test.jar
ENTRYPOINT ["java","-jar","/soaint-test.jar"]