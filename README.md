# 🐾 PetCare - Proyecto de Microservicios 🐾

**PetCare** es un sistema de microservicios diseñado para gestionar la salud y el cuidado de mascotas. Permite el registro de usuarios, veterinarios y mascotas, además de ofrecer funcionalidades para la gestión de datos como alimentación, paseos y visitas al veterinario. El sistema está compuesto por tres servicios principales: **auth-service**, **user-service**, y **pet-service**.

## 🛠️ Tecnologías

- **Spring Boot**: Framework principal para la construcción de los microservicios.
- **Spring Security & JWT**: Para autenticación y autorización mediante tokens JWT.
- **H2 Database**: Base de datos en memoria utilizada durante el desarrollo.
- **Swagger**: Para la documentación interactiva de las APIs.
- **Lombok**: Para reducir la cantidad de código boilerplate.
- **Maven**: Para la gestión de dependencias y construcción del proyecto.

## 🐾 Descripción de los Microservicios

### 1. 📦 `auth-service`

Gestiona la autenticación y autorización de los usuarios y veterinarios a través de JWT.

#### 🏷️ Endpoints principales:
- **POST** `/api/petcare/auth/login`: Realiza el login y devuelve un token JWT.

### 2. 📦 `user-service`

Este servicio maneja el registro, consulta y detalles de los usuarios, incluidos los veterinarios.

#### Endpoints adicionales para auth

- **GET** `/api/petcare/auth-info/{username}`:  
  Obtiene la información de autenticación de un usuario por nombre de usuario.
  
- **GET** `/api/petcare/auth-info/details/{id}`:  
  Obtiene los detalles completos del perfil de un usuario por ID.

#### Endpoints de gestion de usuarios

- **POST** `/api/petcare/register/user`:  
  Registra un nuevo usuario.

- **GET** `/api/petcare/users`:  
  Obtiene una lista de todos los usuarios registrados (solo accesible por el rol `VET`).

- **GET** `/api/petcare/user/{id}`:  
  Obtiene un resumen del perfil de un usuario por ID (solo accesible por el rol `VET`).

- **GET** `/api/petcare/user/detail/{id}`:  
  Obtiene el perfil completo de un usuario por ID (solo accesible por el rol `VET`).

#### Endpoints de gestion de Veterinarios

- **POST** `/api/petcare/register/vet`:  
  Registra un nuevo veterinario.

- **GET** `/api/petcare/veterinarians`:  
  Obtiene una lista de todos los veterinarios registrados (solo accesible por el rol `USER`).

- **GET** `/api/petcare/veterinarian/{id}`:  
  Obtiene un resumen del perfil de un veterinario por ID (solo accesible por el rol `USER`).

- **GET** `/api/petcare/veterinarian/detail/{id}`:  
  Obtiene el perfil completo de un veterinario por ID (solo accesible por el rol `USER`).

### 3. 📦 `pet-service`

Este servicio maneja la gestión de las mascotas, incluyendo la creación de mascotas, su asociación con usuarios y veterinarios, y la consulta de sus datos.

#### Endpoints de gestion de mascotas

- **POST** `/api/petcare/pets`:  
  Crea una nueva mascota.

- **GET** `/api/petcare/pets/{userId}`:  
  Obtiene todas las mascotas asociadas a un usuario.

- **GET** `/api/petcare/pets/vet/{vetId}`:  
  Obtiene todas las mascotas asociadas a un veterinario.


## 🏁 Cómo Ejecutar el Proyecto

### 1. 🖥️ Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/petcare.git
cd petcare
```

## 🏁 Cómo Ejecutar el Proyecto

### 1. 🚀 Ejecutar los Servicios

Cada microservicio está en su propio directorio. Para ejecutarlos, navega a cada uno de los directorios y usa el siguiente comando:

```bash
mvn spring-boot:run
```

### 2. 🌐 Enlaces de Swagger

Los microservicios cuentan con documentación interactiva de Swagger. Una vez que los servicios estén corriendo, puedes acceder a la documentación en las siguientes URL:

- **Auth Service**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **User Service**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **Pet Service**: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

## 🔧 Configuración

Puedes cambiar los puertos de cada servicio editando el archivo `application.properties` dentro de cada microservicio.

- **auth-service**: 8081
- **user-service**: 8082
- **pet-service**: 8083

![Configuración de Puertos](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Configuracion/puertosAplicationProperties.png)

### 🔐 Configuración de JWT
Recuerda configurar el `secret` para los tokens JWT en el archivo `application.properties`.

![jws Secret](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Configuracion/secret.png)

---
