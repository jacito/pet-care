# 🐾 PetCare

Sistema de gestión de mascotas desarrollado como proyecto final del Bootcamp Código Facilito (Microservicios con Java).

---

## 📋 Descripción general

**PetCare** es una plataforma basada en microservicios para la gestión integral de mascotas, usuarios y veterinarios.  
Permite que los dueños puedan registrar sus mascotas, asignar veterinarios y consultar detalles, mientras que los veterinarios pueden gestionar la información de las mascotas asignadas.

El sistema está dividido en los siguientes microservicios:

- **auth-service**: Maneja el inicio de sesión de usuarios y veterinarios, emitiendo tokens JWT para el acceso seguro a la plataforma.
- **user-service**: Gestiona el registro y consulta de usuarios, diferenciando entre usuarios regulares (dueños) y veterinarios.
- **pet-service**: Administra la creación de mascotas, su asociación con usuarios y veterinarios, y permite consultar los detalles respectivos.

---

## 🚀 Características principales

- Registro y autenticación de usuarios (dueños y veterinarios) con JWT.
- Gestión de mascotas: creación, asociación con usuarios y veterinarios.
- Arquitectura desacoplada con microservicios (`auth-service`, `user-service`, `pet-service`).
- Comunicación entre servicios usando OpenFeign y WebClient.
- Seguridad mediante Spring Security.
- Documentación de APIs con Swagger/OpenAPI.
- Base de datos H2 (modo desarrollo).

---

## 🧰 Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- OpenFeign
- WebClient
- Swagger (Springdoc OpenAPI)
- Maven
- Docker y Docker Compose
- IntelliJ IDEA

---

## 📁 Estructura del proyecto

![Estructura del proyecto](https://github.com/jacito/pet-care-imagenes/raw/main/Estructura/Basica.png)


---

## Cómo levantar el proyecto

Para ejecutar todos los servicios de forma local con Docker Compose, primero asegúrate de tener instalado y corriendo Docker y Docker Desktop.

En la raíz del proyecto (`petcare/`), ejecuta:

```bash
docker-compose up --build
``` 

Esto construirá las imágenes y levantará los tres servicios en los puertos:

- auth-service: `localhost:8081`
- user-service: `localhost:8082`
- pet-service: `localhost:8083`

## 📌 Pendientes y mejoras futuras

- Integración con GitHub Actions para CI/CD.
- Implementación de pruebas de integración.
- Mejoras en validaciones de negocio y manejo de errores global.
- Despliegue a un entorno cloud (Heroku, AWS, etc.).

---

## 👩‍💻 Autora

Proyecto desarrollado por **Jazmín Velázquez** como parte del Bootcamp de Microservicios con Java y Spring Boot de [Código Facilito](https://codigofacilito.com/).

---

## 📇 Contacto

| ![Jacito](https://github.com/jacito/pet-care-imagenes/raw/main/Perfil/Jacito.jpg) | [GitHub](https://github.com/jacito)<br>[LinkedIn](https://www.linkedin.com/in/jacito/) |
|:--:|:--:|
| ******* | **Redes de contacto** |



---

¡Gracias por visitar el proyecto PetCare! 🐾

