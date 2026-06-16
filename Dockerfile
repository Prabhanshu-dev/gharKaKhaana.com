FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/
COPY auth-service/pom.xml auth-service/
COPY vendor-service/pom.xml vendor-service/
COPY menu-service/pom.xml menu-service/
COPY order-service/pom.xml order-service/
COPY payment-service/pom.xml payment-service/
COPY ai-service/pom.xml ai-service/

# Download dependencies first
RUN mvn dependency:go-offline -B || true

COPY . .
# We use a build argument to specify which module to package and copy
ARG MODULE_NAME
RUN mvn clean package -pl ${MODULE_NAME} -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG MODULE_NAME
COPY --from=build /app/${MODULE_NAME}/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
