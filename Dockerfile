FROM openjdk:19
COPY ./build/libs/openaibot-0.0.1-SNAPSHOT.jar /app.jar
CMD java -jar app.jar
