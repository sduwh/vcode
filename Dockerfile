FROM java:8

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/app.jar

WORKDIR /app

EXPOSE 8000

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=prod"]