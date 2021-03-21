FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
COPY ./dater/target/dater-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar","dater-0.0.1-SNAPSHOT.jar"]