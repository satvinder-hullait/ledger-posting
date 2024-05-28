FROM gradle:8.7.0-jdk21-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app

RUN ./gradlew build --no-daemon -PbuildDir=/app/build -x test 
RUN ls /app/build/libs/

FROM amazoncorretto:21.0.1-alpine3.18

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/

CMD ["java", "-jar", "/app/ledger-service-0.0.1.jar"]
