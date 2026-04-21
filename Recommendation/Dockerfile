FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# FROM eclipse-temurin:21-jdk

# WORKDIR /app

# # Copy built jar from builder stage
# COPY /target/*.jar app.jar

EXPOSE 8086

CMD ["java","-jar","app.jar"]