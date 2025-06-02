# Usamos una imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Crear un directorio en el contenedor
WORKDIR /app

# Copiar el JAR construido al contenedor
COPY target/pet-service-*.jar app.jar

# Expone el puerto de la app
EXPOSE 8083

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
