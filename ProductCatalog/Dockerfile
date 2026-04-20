FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy built jar from builder stage
COPY /target/*.jar app.jar

EXPOSE 8084

CMD ["java","-jar","app.jar"]