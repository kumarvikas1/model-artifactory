FROM adoptopenjdk/openjdk11
COPY target/demo-spring-boot-service-1.0-SNAPSHOT.jar /app/app.jar
CMD ["java","-jar", "/app/app.jar"]