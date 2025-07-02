# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
# Fix permissions for mvnw and make it executable
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD sh -c "if echo \"\$SPRING_DATASOURCE_URL\" | grep -q '^postgresql://'; then export SPRING_DATASOURCE_URL=\$(echo \"\$SPRING_DATASOURCE_URL\" | sed 's|postgresql://|jdbc:postgresql://|'); fi; echo \"🚀 Starting with URL: \$SPRING_DATASOURCE_URL\"; java -jar app.jar"