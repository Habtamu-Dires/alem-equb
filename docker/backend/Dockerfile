#Build Stage
FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime Stage
FROM eclipse-temurin:23-jre-alpine
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app

# Timezone setup
RUN apk add --no-cache tzdata \
    && ln -snf /usr/share/zoneinfo/Africa/Addis_Ababa /etc/localtime \
    && echo "Africa/Addis_Ababa" > /etc/timezone
ENV TZ="Africa/Addis_Ababa"

COPY --from=build /build/target/alem-ekub-api-*.jar /app/

EXPOSE 8088

ENV DB_URL=jdbc:postgresql://postgres-ekub:5432/alem_ekub_db
ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} alem-ekub-api-${JAR_VERSION}.jar


