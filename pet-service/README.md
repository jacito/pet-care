# 📦 pet-service

Este microservicio forma parte del sistema **PetCare**, una plataforma de gestión de salud de mascotas. El `pet-service` se encarga de la administración de las mascotas (creación, asignación de usuario/veterinario, y consulta).



## 📌 Funcionalidad principal

## 🐾 Funcionalidades principales

### 1. Registro de Mascotas
Permite a los usuarios con rol `USER` registrar nuevas mascotas a su nombre.

- Datos requeridos: nombre, especie, fecha de nacimiento.
- La mascota queda automáticamente asociada al usuario autenticado.

---

### 2. Asignación de Veterinario
Permite a los usuarios con rol `VET` asignarse como veterinarios responsables de mascotas registradas por usuarios.

- Se utiliza el `petId` y el `vetId` del veterinario autenticado.
- Solo los veterinarios pueden ejecutar esta acción.

---

### 3. Consulta de Mascotas por Usuario
Permite a un usuario autenticado (`USER`) consultar todas las mascotas registradas a su nombre.

- Incluye información básica de la mascota y del usuario propietario.

---

### 4. Consulta de Mascotas por Veterinario
Permite a un veterinario autenticado (`VET`) ver las mascotas a las que está asignado.

- Incluye información básica de la mascota y del dueño de cada una.

---

### 5. Consulta Detallada de una Mascota
Permite consultar el detalle completo de una mascota por su `ID`.

- Incluye todos los datos de la mascota.
- También muestra información detallada del usuario dueño.



## 🚀 Endpoints principales

| Método | Endpoint                              | Descripción                                                                 |
|--------|----------------------------------------|-----------------------------------------------------------------------------|
| POST   | `/api/pets/register`                  | Registrar una nueva mascota                                                |
| PUT    | `/api/pets/assign-vet`                | Asignar un veterinario a una mascota                                       |
| GET    | `/api/pets/user/{userId}`             | Obtener todas las mascotas registradas por un usuario                      |
| GET    | `/api/pets/vet/{vetId}`               | Obtener todas las mascotas asignadas a un veterinario                      |
| GET    | `/api/pets/{petId}/user/{userId}`     | Obtener el detalle completo de una mascota y su dueño                      |
| GET    | `/api/pets/{petId}/vet/{vetId}`       | Obtener el detalle completo de una mascota y su veterinario asignado       |


---
## 🔐 Seguridad

Este microservicio valida el token JWT generado por `auth-service` para proteger sus endpoints.

Requieren un token JWT válido en el encabezado `Authorization`:

```http
Authorization: Bearer <token>
```

  ![Ejemplos Token](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Auth/Token.png)

---

## 🧱 Estructura y Diagrama de Clases

  ![Estructura Completa](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/Estructura.png)


**📘 Diagrama Simplificado de Clases:**

## 📘 Diagrama Simplificado de Clases

```plaintext
+---------------------+
|     PetController   |
+---------------------+
| - petService        |
+---------------------+
| +createPet()        |
| +getPetsByUser()    |
| +getPetsByVet()     |
| +getPetDetailForUser() |
| +getPetDetailForVet() |
| +assignVet()        |
+---------------------+

           |
           v

+---------------------+
|      PetService     |
+---------------------+
| - petRepository     |
| - webClient         |
+---------------------+
| +createPet()        |
| +petExists()        |
| +getPetsByUserId()  |
| +getPetsByVetId()   |
| +getPetDetailsForUser() |
| +getPetDetailsForVet()  |
| +assignVeterinarian()   |
+---------------------+

           |
           v

+---------------------+
|       Pet           |
+---------------------+
| - id: Long          |
| - name: String      |
| - species: String   |
| - breed: String     |
| - birthDate: Date   |
| - userId: Long      |
| - vetId: Long       |
+---------------------+

+-----------------------------+       +-------------------------------+
|       CreatePetRequest      |       |       AssignVetRequest        |
+-----------------------------+       +-------------------------------+
| - name                      |       | - petId                        |
| - species                   |       | - vetId                        |
| - breed                     |       +-------------------------------+
| - birthDate                 |
| - userId                    |
+-----------------------------+

+-----------------------------+       +-------------------------------+
|       PetVetView            |       |       PetUserDetailView       |
+-----------------------------+       +-------------------------------+
| - pet: PetView              |       | - pet: PetDetailView          |
| - vet: UserView             |       | - user: UserDetailView        |
+-----------------------------+       +-------------------------------+

+-----------------------------+       +-------------------------------+
|       PetVetDetailView      |       |       PetDetailView           |
+-----------------------------+       +-------------------------------+
| - pet: PetDetailView        |       | - id                          |
| - vet: UserDetailView       |       | - name                        |
+-----------------------------+       | - species                     |
                                      | - breed                       |
                                      | - birthDate                   |
                                      +-------------------------------+

```

  ![Diagrama de Clases](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/DiagramaClases.png)

## ⚙️ Configuración

Este microservicio utiliza configuraciones definidas en `application.properties`, y cuenta con un `Dockerfile` para facilitar su despliegue en entornos contenerizados.

### `application.properties`

Contiene las propiedades clave para la ejecución del microservicio y la gestión de tokens JWT.

```properties
server.port=8083

jwt.secret=my-super-secret-key
jwt.expiration=86400000

```
  ![application.properties](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/applicationProperties.png)

- `server.port`: Puerto en el que se ejecuta el microservicio `pet-service`.
- `jwt.secret`: Clave secreta utilizada para firmar los tokens JWT, asegurando su integridad.
- `jwt.expiration`: Tiempo de expiración del token en milisegundos (por ejemplo, 86400000 equivale a 24 horas).

---

### 🐳 `Dockerfile`

El `Dockerfile` permite empaquetar el microservicio en una imagen Docker lista para ejecutarse en cualquier entorno que soporte contenedores.

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/pet-service.jar pet-service.jar
ENTRYPOINT ["java", "-jar", "pet-service.jar"]
```

 ![Dockerfile](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/Dockerfile.png)

#### Comandos para construir y correr el contenedor:

Para construir la imagen del microservicio `pet-service` con Docker:

```bash
docker build -t pet-service .
```

Luego, para ejecutar el contenedor:

```bash
docker run -p 8083:8083 pet-service
```

Esto levantará el servicio en: http://localhost:8083

### 🧪 Pruebas

El servicio **pet-service** incluye pruebas automatizadas para verificar su funcionamiento. Las pruebas están basadas en JUnit y se encuentran en la carpeta `src/test/java`, incluye pruebas unitarias para validar la creación de mascotas, asignación de veterinarios, consultas de mascotas relacionadas con dueños y veterinarios, así como sus detalles.

  ![PasswordEncoderTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/PasswordEncoderTest.png)
  ![PetControllerTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/PetControllerTest.png)
  ![PetServiceImplTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/PetServiceImplTest.png)
  ![UserServiceImplTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/UserServiceImplTest.png)
  ![VetServiceImplTest](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/VetServiceImplTest.png)

  
  ![TESTs](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/Test.png)

### 🌐 Swagger

El servicio **pet-service** integra [Swagger](https://swagger.io/) para la documentación interactiva de la API. Con Swagger, puedes explorar todos los endpoints disponibles, visualizar sus descripciones y probarlos directamente desde el navegador.

Puedes acceder a la documentación de la API en la siguiente URL después de levantar el servicio:

```bash
http://localhost:8083/swagger-ui/index.html
```

---

## 🐾 Gestión de Mascotas

APIs relacionadas con el registro, asignación y consulta de mascotas en la plataforma **PetCare**.

###Cabecera requerida:  
Debe incluir el token JWT válido obtenido tras el login.

```http
Authorization: Bearer <jwt-token>

eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVkVUIiwiaWQiOjIsInVzZXJuYW1lIjoiZHJqYW5ldmV0Iiwic3ViIjoiZHJqYW5ldmV0IiwiaWF0IjoxNzQ4ODY5ODY2LCJleHAiOjE3NDg4NzM0NjZ9.paF0PMztEiGVbplInrN1qNvD5OdUhS2Fhe_9gaNz2ns
```

### 📘 Respuestas Comunes

**🚫 Respuesta 204 - Mascota no encontrada**

```json
{
  "code": 204,
  "message": "No Content",
  "details": "Pet not found"
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
### 🐶 Registrar una nueva mascota

- **URL:** `POST /api/pets/register`
- **Descripción:** Crea un nuevo registro de mascota. Este endpoint está disponible para los usuarios autenticados con los permisos adecuados. Si el nombre de la mascota ya existe, se devuelve un error de conflicto.
- **Parámetros:**

| Tipo     | Nombre   | Descripción                             | Requerido |
|----------|----------|-----------------------------------------|-----------|
| Body     | `CreatePetRequest` | Contiene los detalles de la mascota, como el nombre, especie, raza, etc. | ✅ Sí     |




- **Request Body:**

```json
{
  "name": "Mishi",
  "species": "Cat",
  "breed": "Siamese",
  "age": 3,
  "weight": 4.5
}

```

**✅ Código 201 - Mascota registrada con éxito:**

Si la creación de la mascota es exitosa, se devuelve el siguiente mensaje:

```json
"Pet registered successfully."
```

**🚫 Código 409 - Conflicto (Mascota ya registrada):**

Si la mascota ya existe en el sistema, se devuelve un error de conflicto con el siguiente mensaje:

```json
{
  "code": 409,
  "message": "Conflict",
  "details": "Pet already exists."
}
```
### 🐶🐱 Obtener mascotas por ID de usuario

- **URL:** `GET /api/pets/user/{userId}`
- **Descripción:** Obtiene una lista de las mascotas registradas por un usuario específico. Este endpoint está diseñado para que los dueños de mascotas puedan consultar todas sus mascotas.
- **Parámetros:**

| Tipo     | Nombre   | Descripción                               | Requerido |
|----------|----------|-------------------------------------------|-----------|
| Path     | `userId` | El ID del usuario cuya lista de mascotas se desea obtener. | ✅ Sí     |



**✅ Código 200 - Mascotas encontradas:**

Devuelve una lista de mascotas asociadas al usuario.

```json
[
  {
    "id": 1,
    "name": "Mishi",
    "species": "Cat",
    "breed": "Siamese"
  },
  {
    "id": 2,
    "name": "Fido",
    "species": "Dog",
    "breed": "Golden Retriever"
  }
]
```

### 🐶🐱 Obtener mascotas por ID de veterinario

- **URL:** `GET /api/pets/vet/{vetId}`
- **Descripción:** Obtiene una lista de las mascotas asignadas a un veterinario específico. Este endpoint está diseñado para que los veterinarios puedan ver todos sus pacientes.
- **Parámetros:**

| Tipo     | Nombre   | Descripción                             | Requerido |
|----------|----------|-----------------------------------------|-----------|
| Path     | `vetId`  | El ID del veterinario cuya lista de mascotas se desea obtener. | ✅ Sí     |

- **Respuestas:**

  ✅ **Código 200 - Listado de mascotas:**
  
  Si el veterinario tiene mascotas asignadas, se devuelve un array con la lista de las mascotas y su dueño correspondiente.

  Ejemplo de respuesta:

  ```json
  [
    {
      "petId": 1,
      "name": "Mishi",
      "species": "Cat",
      "owner": {
        "userId": 201,
        "name": "Alice"
      }
    },
    {
      "petId": 2,
      "name": "Bobby",
      "species": "Dog",
      "owner": {
        "userId": 202,
        "name": "Bob"
      }
    }
  ]
```

### 🐶🐱 Obtener mascotas por ID de usuario

- **URL:** `GET /api/pets/user/{userId}`
- **Descripción:** Obtiene una lista de todas las mascotas registradas por un usuario específico. Este endpoint está diseñado para que los dueños de mascotas puedan ver todas las mascotas que han registrado.
- **Parámetros:**

| Tipo     | Nombre   | Descripción                              | Requerido |
|----------|----------|------------------------------------------|-----------|
| Path     | `userId` | El ID del usuario cuyas mascotas se desean obtener. | ✅ Sí     |

- **Respuestas:**

  ✅ **Código 200 - Listado de mascotas:**
  
  Si el usuario tiene mascotas registradas, se devuelve un array con la lista de las mascotas y su veterinario asignado (si hay alguno).

  Ejemplo de respuesta:

  ```json
  [
    {
      "petId": 1,
      "name": "Mishi",
      "species": "Cat",
      "vet": {
        "vetId": 101,
        "name": "Dr. Smith"
      }
    },
    {
      "petId": 2,
      "name": "Bobby",
      "species": "Dog",
      "vet": {
        "vetId": 102,
        "name": "Dr. Johnson"
      }
    }
  ]
```

### 🐶🐱 Obtener mascotas por ID de veterinario

- **URL:** `GET /api/pets/vet/{vetId}`
- **Descripción:** Recupera todas las mascotas que han sido asignadas a un veterinario específico. Este endpoint permite a los veterinarios consultar las mascotas que están bajo su cuidado.
- **Parámetros:**

| Tipo  | Nombre   | Descripción                                   | Requerido |
|-------|----------|-----------------------------------------------|-----------|
| Path  | `vetId`  | El ID del veterinario cuyas mascotas se desean obtener. | ✅ Sí     |

- **Respuestas:**

  ✅ **Código 200 - Listado de mascotas:**
  
  Si el veterinario tiene mascotas asignadas, se devuelve un array con los detalles de cada una y su respectivo dueño.

  Ejemplo de respuesta:

  ```json
  [
    {
      "petId": 3,
      "name": "Max",
      "species": "Dog",
      "owner": {
        "userId": 201,
        "fullName": "Alice Gómez"
      }
    },
    {
      "petId": 4,
      "name": "Luna",
      "species": "Cat",
      "owner": {
        "userId": 202,
        "fullName": "Carlos Ruiz"
      }
    }
  ]
```

### 🐾 Obtener detalle de una mascota y su dueño

- **URL:** `GET /api/pets/{petId}/user/{userId}`
- **Descripción:** Devuelve información detallada de una mascota registrada y su dueño. Este endpoint es utilizado por usuarios para visualizar el perfil completo de su mascota y sus propios datos.

- **Parámetros:**

| Tipo  | Nombre     | Descripción                                       | Requerido |
|-------|------------|---------------------------------------------------|-----------|
| Path  | `petId`    | ID de la mascota.                                 | ✅ Sí     |
| Path  | `userId`   | ID del usuario dueño de la mascota.               | ✅ Sí     |

- **Respuestas:**

  ✅ **Código 200 - Detalle de mascota y usuario:**

  Si la mascota existe y pertenece al usuario, se devuelve el detalle completo de ambos.

  Ejemplo de respuesta:

  ```json
  {
    "pet": {
      "id": 7,
      "name": "Coco",
      "species": "Cat",
      "breed": "Siamese",
      "age": 4
    },
    "user": {
      "id": 105,
      "fullName": "María Torres",
      "email": "maria.torres@example.com"
    }
  }
```

### 🩺 Obtener detalle de una mascota y su veterinario

- **URL:** `GET /api/pets/{petId}/vet/{vetId}`
- **Descripción:** Devuelve información detallada de una mascota y del veterinario asignado. Este endpoint es utilizado por veterinarios para acceder al perfil completo de una mascota que atienden.

- **Parámetros:**

| Tipo  | Nombre     | Descripción                                  | Requerido |
|-------|------------|----------------------------------------------|-----------|
| Path  | `petId`    | ID de la mascota.                            | ✅ Sí     |
| Path  | `vetId`    | ID del veterinario asignado a la mascota.   | ✅ Sí     |

- **Respuestas:**

  ✅ **Código 200 - Detalle de mascota y veterinario:**

  Ejemplo de respuesta:

  ```json
  {
    "pet": {
      "id": 9,
      "name": "Toby",
      "species": "Dog",
      "breed": "Beagle",
      "age": 5
    },
    "vet": {
      "id": 301,
      "fullName": "Dr. Luis Fernández",
      "licenseNumber": "VET-998877",
      "specialty": "Small Animals"
    }
  }
```

### 🩺 Asignar un veterinario a una mascota

- **URL:** `PUT /api/pets/assign-vet`
- **Descripción:** Asocia una mascota existente con un veterinario registrado. Es útil para que una mascota tenga un veterinario asignado responsable de su salud.

- **Body (JSON):**

```json
{
  "petId": 9,
  "vetId": 301
}
```

| Campo    | Tipo   | Descripción                     | Requerido |
|----------|--------|---------------------------------|-----------|
| petId    | Long   | ID de la mascota a asignar.     | ✅ Sí     |
| vetId    | Long   | ID del veterinario asignado.    | ✅ Sí     |

- **Respuestas:**

  ✅ **Código 200 - Veterinario asignado correctamente:**

  ```text
  Veterinarian assigned successfully
```



  ![Swagger aut-service](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/swagger.png)

### 🧪 Test

#### Herramientas de Test

Puedes usar herramientas como **Postman** o **Curl** para probar los endpoint de pet-service, el uso de los JWT tokens, los errores en y problemas internos del servidor.

### 📬 Colección de Postman

Puedes utilizar esta colección para probar los endpoints del microservicio `pet-service`.

🔗 [Ver colección en Postman](https://github.com/jacito/pet-care/blob/425b6883b3da87d7577012c00df7a13f1253b361/PetCare.postman_collection.json)

  ![CollectionPostmant](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/Postmant.png)

### 🚀 Arranque del Servicio

Sigue los siguientes pasos para compilar y ejecutar el microservicio `pet-service`.

#### 1. Construcción del Proyecto

Utiliza Maven para compilar el proyecto:

```bash
mvn clean install
```
  ![Compilacion pet-service](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/Compilacion.png)

Este comando compilará el proyecto y generará el archivo .jar en la carpeta target/.

#### 1. Ejecución del Servicio

Puedes iniciar el servicio usando el siguiente comando:

```bash
mvn spring-boot:run
```

  ![Arrancar pet-service](https://raw.githubusercontent.com/jacito/pet-care-imagenes/refs/heads/main/Servicios/Pet/run.png)


