# ---------- ETAPA 1: COMPILAR ----------
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Dar permisos y compilar
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# ---------- ETAPA 2: EJECUTAR ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar jar generado
COPY --from=build /app/target/*.jar app.jar

# Puerto de Spring Boot
EXPOSE 8201

# Variables opcionales
ENV SERVER_PORT=8201

# Ejecutar app
ENTRYPOINT ["java","-jar","app.jar"]
