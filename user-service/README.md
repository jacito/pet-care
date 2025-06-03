# 📦 user-service

Este microservicio forma parte del sistema **PetCare** y se encarga de la gestión de usuarios y veterinarios, proporcionando endpoints para su registro y consulta.


## 📌 Funcionalidad principal

- Registro de usuarios (dueños de mascotas).
- Registro de veterinarios (con validación de datos profesionales).
- Consulta de usuarios y veterinarios por su identificador.
- Soporte para ser consultado desde otros servicios (por ejemplo, `auth-service` y `pet-service`).


## 🚀 Endpoints principales

| Método | Endpoint                                | Descripción                                     |
|--------|------------------------------------------|-------------------------------------------------|
| POST   | `/api/petcare/register/user`            | Registrar un nuevo usuario                      |
| POST   | `/api/petcare/register/vet`             | Registrar un nuevo veterinario                  |
| GET    | `/api/petcare/user/{id}`                | Obtener detalles del usuario por ID             |
| GET    | `/api/petcare/vet/{id}`                 | Obtener detalles del veterinario por ID         |
| GET    | `/api/petcare/veterinarians`            | Obtener todos los veterinarios registrados      |
| GET    | `/api/petcare/user/username/{username}` | Obtener usuario por nombre de usuario           |

---
## 🔐 Seguridad

Este microservicio valida el token JWT generado por `auth-service` para proteger sus endpoints.

- **Endpoints públicos:**  
  - `POST /api/petcare/register/user`  
  - `POST /api/petcare/register/vet`

- **Endpoints protegidos:**  
  Requieren un token JWT válido en el encabezado `Authorization`:

```http
Authorization: Bearer <token>
```

  ![Ejemplos Token](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Auth/Token.png)

---

## 🧱 Estructura y Diagrama de Clases

  ![Estructura Completa](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/Estructura.png)


**📘 Diagrama Simplificado de Clases:**

```mermaid
classDiagram
    class UserController {
        +registerUser()
        +registerVet()
        +getUserById()
        +getVetById()
    }
    class UserService {
        +saveUser()
        +saveVet()
        +getUserById()
        +getVetById()
    }
    class UserRepository
    class VetRepository

    UserController --> UserService
    UserService --> UserRepository
    UserService --> VetRepository

```

  ![Diagrama de Clases](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/DiagramaClases.png)

## ⚙️ Configuración

Este microservicio utiliza configuraciones definidas en `application.properties`, y cuenta con un `Dockerfile` para facilitar su despliegue en entornos contenerizados.

### `application.properties`

Contiene las propiedades clave para la ejecución del microservicio y la gestión de tokens JWT.

```properties
server.port=8082

jwt.secret=my-super-secret-key
jwt.expiration=86400000

```
  ![application.properties](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/applicationProperties.png)

- `server.port`: Puerto en el que se ejecuta el microservicio `auth-service`.
- `jwt.secret`: Clave secreta utilizada para firmar los tokens JWT, asegurando su integridad.
- `jwt.expiration`: Tiempo de expiración del token en milisegundos (por ejemplo, 86400000 equivale a 24 horas).

---

### 🐳 `Dockerfile`

El `Dockerfile` permite empaquetar el microservicio en una imagen Docker lista para ejecutarse en cualquier entorno que soporte contenedores.

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/user-service.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
```

 ![Dockerfile](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/Dockerfile.png)

#### Comandos para construir y correr el contenedor:

Para construir la imagen del microservicio `user-service` con Docker:

```bash
docker build -t user-service .
```

Luego, para ejecutar el contenedor:

```bash
docker run -p 8082:8082 user-service
```

Esto levantará el servicio en: http://localhost:8082

### 🧪 Pruebas

El servicio **user-service** incluye pruebas automatizadas para verificar su funcionamiento. Las pruebas están basadas en JUnit y se encuentran en la carpeta `src/test/java`, incluye pruebas unitarias para validar la creación de usuarios, veterinarios y consultas.

  ![LoginControllerTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/LoginControllerTest.png)
  ![PasswordEncoderTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/PasswordEncoderTest.png)
  ![UserControllerTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/UserControllerTest.png)
  ![UserServiceImplTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/UserServiceImplTest.png)
  ![VetControllerTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/VetControllerTest.png)
  ![VetServiceImplTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/VetServiceImplTest.png)
  
  ![TESTs](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/Tests.png)

### 🌐 Swagger

El servicio **user-service** integra [Swagger](https://swagger.io/) para la documentación interactiva de la API. Con Swagger, puedes explorar todos los endpoints disponibles, visualizar sus descripciones y probarlos directamente desde el navegador.

Puedes acceder a la documentación de la API en la siguiente URL después de levantar el servicio:

```bash
http://localhost:8082/swagger-ui/index.html
```

---

## 🔐 Complemento/soporte auth
APIs relacionadas con el registro general de usuarios y su consulta para soporte a login de auth-service

### 📘 Endpoints del LoginController

#### 📘 `/api/petcare/auth-info/{username}`

- **Método:** `GET`
- **Descripción:** Este endpoint es utilizado por `auth-service` para obtener los datos básicos de autenticación de un usuario, como el `id`, `username`, contraseña cifrada y rol del usuario.

**📥 Parámetros**
- **Path Variable:** `username` — Nombre de usuario a buscar.

**✅ Respuesta Exitosa (200)**
```json
{
  "id": 1,
  "username": "usuario123",
  "password": "$2a$10$h7X...",
  "role": "USER"
}
```
🚫 **Respuesta 204 - Usuario no encontrado**

```json
{
  "code": 204,
  "message": "No Content",
  "details": "User not found"
}
```
**💥 Respuesta 500 - Error interno del servidor**

```json
{
  "code": 500,
  "message": "Internal Server Error",
  "details": "Unexpected error occurred"
}

```
---

### 📘 `/api/petcare/auth-info/details/{id}`

- **Método:** `GET`
- **Descripción:** Este endpoint se utiliza después de un inicio de sesión exitoso para obtener información detallada del perfil del usuario a partir de su ID. Sirve para enriquecer el JWT con los datos del usuario, como nombre completo, contacto, etc.

**📥 Parámetros**
- **Path Variable:** `id` — Identificador único del usuario.

---

**✅ Respuesta Exitosa (200)**
```json
{
  "id": 1,
  "fullName": "Usuario Ejemplo",
  "email": "usuario@example.com",
  "phone": "1234567890",
  "address": "Calle 123, Ciudad"
}
```
🚫 **Respuesta 204 - Usuario no encontrado**

```json
{
  "code": 204,
  "message": "No Content",
  "details": "User not found"
}
```
**💥 Respuesta 500 - Error interno del servidor**

```json
{
  "code": 500,
  "message": "Internal Server Error",
  "details": "Unexpected error occurred"
}

```
---

## 👤 Gestión de usuarios
APIs relacionadas con el registro y consulta de usuarios en el sistema **PetCare**.

#### 🧾 Respuestas Compartidas

🚫 **Respuesta 204 - Usuario no encontrado**

```json
{
  "code": 204,
  "message": "No Content",
  "details": "User not found"
}
```
**💥 Respuesta 500 - Error interno del servidor**

```json
{
  "code": 500,
  "message": "Internal Server Error",
  "details": "Unexpected error occurred"
}

```
---

### 📘 Endpoint de Registro de Usuario

- **URL:** `/api/petcare/register/user`
- **Método:** `POST`
- **Descripción:** Este endpoint permite registrar a un nuevo usuario en el sistema. Recibe un objeto `RegisterUserRequest` con los datos de credenciales y perfil personal. Si el nombre de usuario o el correo electrónico ya están registrados, se devuelve un error de conflicto. Si la creación es exitosa, se devuelve un mensaje de confirmación.

**📥 Parámetros**

- **Request Body:**

```json
{
  "username": "usuario123",
  "email": "usuario@example.com",
  "password": "contraseñaSegura",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```
#### 🧾 Respuestas

 ✅ **Código 201 - Usuario registrado con éxito:**

Si el registro es exitoso, se devuelve un mensaje de confirmación indicando que el usuario fue creado correctamente.

```json
"User registered successfully."
```

🚫 **Código 409 - Conflicto (Usuario o correo ya registrado):**
Si el nombre de usuario o el correo electrónico ya están en uso, el servicio responde con un error de conflicto.
``` json
{
  "code": 409,
  "message": "Conflict",
  "details": "Username or email already exists."
}
```

---

### 📘 Endpoint
`GET /api/petcare/users`  
**Descripción**: Recupera una lista de todos los usuarios registrados (excluyendo veterinarios) con información básica.  
🔐 Requiere rol: `VET`

**📥 Parámetros**
Este endpoint no requiere parámetros en la URL ni en el cuerpo de la solicitud.  

**Cabecera requerida**:  
Debe incluir el token JWT válido obtenido tras el login.

```http
Authorization: Bearer <jwt-token>

eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVkVUIiwiaWQiOjIsInVzZXJuYW1lIjoiZHJqYW5ldmV0Iiwic3ViIjoiZHJqYW5ldmV0IiwiaWF0IjoxNzQ4ODY5ODY2LCJleHAiOjE3NDg4NzM0NjZ9.paF0PMztEiGVbplInrN1qNvD5OdUhS2Fhe_9gaNz2ns
```


 ✅ **Respuesta Exitosa - 200 OK**
Retorna una lista de usuarios con su información básica (id y nombre completo).

```json
[
  {
    "id": 1,
    "fullName": "Ana López"
  },
  {
    "id": 2,
    "fullName": "Carlos Pérez"
  }
]
```
---

### 📘 Endpoint
`GET /api/petcare/user/{id}`  
**Descripción**: Recupera el perfil resumido de un usuario específico por su ID.  
🔐 Requiere rol: `VET`

📥 **Parámetros**

| Tipo     | Nombre | Descripción             | Requerido |
|----------|--------|-------------------------|-----------|
| Path     | `id`   | ID del usuario a buscar | ✅ Sí     |

**Cabecera requerida**:  
Debe incluir el token JWT válido obtenido tras el login.

```http
Authorization: Bearer <jwt-token>

eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVkVUIiwiaWQiOjIsInVzZXJuYW1lIjoiZHJqYW5ldmV0Iiwic3ViIjoiZHJqYW5ldmV0IiwiaWF0IjoxNzQ4ODY5ODY2LCJleHAiOjE3NDg4NzM0NjZ9.paF0PMztEiGVbplInrN1qNvD5OdUhS2Fhe_9gaNz2ns
```


**✅ Respuesta Exitosa - 200**
Devuelve la información básica del usuario solicitado.

```json
{
  "id": 7,
  "fullName": "Laura García"
}
```
---

### 📘 Endpoint
`GET /api/petcare/user/detail/{id}`  
**Descripción**: Recupera el perfil completo de un usuario específico identificado por su ID.  
🔐 Requiere rol: `VET`

📥 **Parámetros**

| Tipo     | Nombre | Descripción             | Requerido |
|----------|--------|-------------------------|-----------|
| Path     | `id`   | ID del usuario a buscar | ✅ Sí     |

**Cabecera requerida**:  
Debe incluir el token JWT válido obtenido tras el login.

```http
Authorization: Bearer <jwt-token>

eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVkVUIiwiaWQiOjIsInVzZXJuYW1lIjoiZHJqYW5ldmV0Iiwic3ViIjoiZHJqYW5ldmV0IiwiaWF0IjoxNzQ4ODY5ODY2LCJleHAiOjE3NDg4NzM0NjZ9.paF0PMztEiGVbplInrN1qNvD5OdUhS2Fhe_9gaNz2ns
```


✅ **Respuesta Exitosa - 200 OK**  
Devuelve la información detallada del usuario solicitado.

```json
{
  "id": 7,
  "firstName": "Laura",
  "lastName": "García",
  "phone": "5522334455",
  "address": "Av. Siempre Viva 123",
  "birthDate": "1990-06-15"
}
```

## 🩺 Gestion de veterinarios

APIs relacionadas con la consulta de veterinarios en el sistema **PetCare**.

#### 🧾 Respuestas Compartidas

🚫 **Respuesta 204 - Veterinario no encontrado**

```json
{
  "code": 204,
  "message": "No Content",
  "details": "Veterinarian not found not found"
}
```
**💥 Respuesta 500 - Error interno del servidor**

```json
{
  "code": 500,
  "message": "Internal Server Error",
  "details": "Unexpected error occurred"
}

```
---

### 📘 `POST /api/petcare/register/vet`

**Descripción**: Registra un nuevo veterinario en el sistema.

 **📥 Parámetros**

```json
{
  "username": "vet_user",
  "email": "vet@email.com",
  "password": "securePassword",
  "firstName": "Ana",
  "lastName": "Ramirez",
  "licenseNumber": "12345678",
  "specialty": "Feline Medicine"
}
```

**✅ Respuesta exitosa – `201 Created`**

```text
Veterinarian registered successfully.
```

🚫 Otra respuesta – 409 Conflict
```json
{
  "code": 409,
  "message": "Conflict",
  "details": "Username or email already exists."
}
```

### 📘 `GET /api/petcare/veterinarians`

**Descripción**: Recupera una lista de todos los veterinarios registrados.

🔐 Requiere rol: `USER`

**✅ Respuesta exitosa – `200 OK`**

```json
[
  {
    "id": 1,
    "fullName": "Ana Ramirez",
    "specialty": "Feline Medicine"
  },
  {
    "id": 2,
    "fullName": "Carlos Mejia",
    "specialty": "Surgery"
  }
]
```

### 📘 `GET /api/petcare/veterinarian/{id}`

**Descripción**: Obtiene un **resumen** del perfil de un veterinario por su ID.

🔐 Requiere rol: `USER`

**✅ Respuesta exitosa – `200 OK`**

```json
{
  "id": 1,
  "fullName": "Ana Ramirez",
  "specialty": "Feline Medicine"
}
```

### 📘 `GET /api/petcare/veterinarian/detail/{id}`

**Descripción**: Obtiene el **perfil completo** de un veterinario por su ID.

🔐 Requiere rol: `USER`

**✅ Respuesta exitosa – `200 OK`**

```json
{
  "id": 1,
  "username": "vet_user",
  "email": "vet@email.com",
  "fullName": "Ana Ramirez",
  "specialty": "Feline Medicine",
  "licenseNumber": "12345678"
}
```


  ![Swagger aut-service](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/swagger.png)

### 🧪 Test

#### Herramientas de Test

Puedes usar herramientas como **Postman** o **Curl** para probar los endpoint de user-service, el uso de los JWT tokens, los errores en y problemas internos del servidor.

### 📬 Colección de Postman

Puedes utilizar esta colección para probar los endpoints del microservicio `auth-service`.

🔗 [Ver colección en Postman](https://github.com/jacito/pet-care/blob/425b6883b3da87d7577012c00df7a13f1253b361/PetCare.postman_collection.json)

  ![CollectionPostmant](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/postmant.png)

### 🚀 Arranque del Servicio

Sigue los siguientes pasos para compilar y ejecutar el microservicio `user-service`.

#### 1. Construcción del Proyecto

Utiliza Maven para compilar el proyecto:

```bash
mvn clean install
```
  ![Compilacion aut-service](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/Compilacion.png)

Este comando compilará el proyecto y generará el archivo .jar en la carpeta target/.

#### 1. Ejecución del Servicio

Puedes iniciar el servicio usando el siguiente comando:

```bash
mvn spring-boot:run
```

  ![Arrancar aut-service](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/User/run.png)

