FROM openjdk:19
COPY ./build/libs/openaibot-0.0.5-SNAPSHOT.jar /app.jar
CMD java -jar app.jar
