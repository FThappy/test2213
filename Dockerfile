# Stage 1: Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src
RUN wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip -d /opt && \
    rm -f gradle-8.5-bin.zip
ENV GRADLE_HOME /opt/gradle-8.5
ENV PATH $PATH:$GRADLE_HOME/bin
RUN gradle build

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/your-application.jar
EXPOSE 8080
CMD ["java", "-jar", "your-application.jar"]

#FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]