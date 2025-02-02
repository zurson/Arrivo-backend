FROM gradle:8.12-jdk21 AS builder
WORKDIR /arrivo/server

COPY . .

RUN gradle clean build --parallel

FROM eclipse-temurin:21
WORKDIR /arrivo/server

RUN apt-get update && apt-get install -y openssl

COPY --from=builder /arrivo/server/build/libs/*.jar server.jar
COPY --from=builder /arrivo/server/src/main/resources/application_default_credentials.json /arrivo/server/application_default_credentials.json

CMD ["java", "-jar", "server.jar"]