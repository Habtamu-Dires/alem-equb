#Build Stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime Stage
FROM eclipse-temurin:21-jre-alpine
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app

# Timezone setup
#RUN apk add --no-cache tzdata \
#    && ln -snf /usr/share/zoneinfo/Africa/Addis_Ababa /etc/localtime \
#    && echo "Africa/Addis_Ababa" > /etc/timezone
ENV TZ="Africa/Addis_Ababa"

COPY --from=build /build/target/alem-ekub-api-*.jar /app/

EXPOSE 8088

# Enviroment variable
ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run as non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} alem-ekub-api-${JAR_VERSION}.jar


