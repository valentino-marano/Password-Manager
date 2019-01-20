FROM openjdk:8
ARG fatjar
COPY /maven/$fatjar /app/password_manager.jar
WORKDIR /app
CMD ["java", "-jar", "password_manager.jar"]