FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar website_backend.jar

ENV SERVER_PORT=8080

EXPOSE 8080

CMD ["java", "-jar", "website_backend.jar"]
