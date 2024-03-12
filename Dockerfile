FROM openjdk:17
EXPOSE 8081
WORKDIR /app
RUN mkdir -p /app/
ADD applications/app-service/build/libs/prueba.jar /app/prueba.jar
ENTRYPOINT ["java", "-jar", "/app/prueba.jar"]
