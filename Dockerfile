FROM openjdk:8-alpine

ENV TZ=Asia/Shanghai

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/app.jar

WORKDIR /app

EXPOSE 8000

RUN set -eux; \
        ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
        echo $TZ > /etc/timezone

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=prod"]