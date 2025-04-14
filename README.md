# BookingAPI

BookingAPI es una API RESTful desarrollada con Spring Boot que permite gestionar reservas de habitaciones en un sistema de gestión de reservas. El sistema incluye funcionalidades de autenticación y autorización mediante JWT, gestión de usuarios, roles, reservas y habitaciones.

## Características

- **Autenticación**: Implementación de JWT para autenticar a los usuarios.
- **Gestión de usuarios**: Registro, login, y gestión de usuarios con roles específicos.
- **Gestión de reservas**: Creación, modificación y eliminación de reservas de habitaciones.
- **Gestión de habitaciones**: Adición, actualización y eliminación de habitaciones.
- **Swagger**: Documentación interactiva de la API con Swagger.
- **Manejo de errores**: Excepciones personalizadas y respuestas claras de error.

## Tecnologías utilizadas

- **Spring Boot**: Framework principal para construir la API.
- **Spring Security**: Para la autenticación y autorización.
- **JWT (JSON Web Token)**: Para la gestión de tokens de autenticación.
- **Spring Data JPA**: Para la interacción con la base de datos.
- **H2 Database**: Base de datos en memoria para pruebas.
- **Swagger**: Para la documentación interactiva de la API.
- **BCrypt**: Para la encriptación de contraseñas.

## Dependencias en el proyecto

- **Spring Boot Starter Data JPA**: Para la integración de JPA con Spring Boot.
- **Spring Boot Starter Security**: Para la implementación de seguridad en la API.
- **Spring Boot Starter Validation**: Para validaciones de datos.
- **Spring Boot Starter Web**: Para la creación de aplicaciones web RESTful.
- **Spring Web**: Proporciona funcionalidades adicionales de web como las REST APIs.
- **Spring Boot DevTools**: Herramientas de desarrollo para mejorar la experiencia de desarrollo (reloading automático, etc.).
- **H2 Database**: Base de datos en memoria utilizada en el entorno de desarrollo.
- **Spring Boot Configuration Processor**: Para el procesamiento de configuraciones.
- **Spring Boot Starter Test**: Herramientas para realizar pruebas unitarias.
- **Spring Security Test**: Para pruebas de componentes relacionados con seguridad.
- **Hibernate Validator**: Para validaciones adicionales en el backend.
- **Jakarta Validation API**: API estándar para la validación de beans.
- **Spring Data Commons**: Biblioteca común de datos para Spring Data.
- **Springdoc OpenAPI Starter WebMVC UI**: Para generar documentación de la API utilizando Swagger.
- **JJWT (JSON Web Token)**: Para manejar tokens JWT (jjwt-api, jjwt-impl y jjwt-jackson).
- **Gson**: Para trabajar con JSON de manera sencilla.
- **Servlet API**: Para el manejo de las servlets en aplicaciones Java.
- **Javax Annotation API**: Para las anotaciones estándar de Java.
- **Spring Boot Starter Logging**: Para el logging en la aplicación.


## Endpoints

### 1. Autenticación

- **POST** `/register`: Registra un nuevo usuario.
  - **Request Body**:
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "phone": "123456789",
      "password": "password123"
    }
    ```
   - **Response**:
    
Devuelve un JWT (Token de autenticación) que se puede usar para acceder a rutas protegidas.
```
    {
      "token": "your-jwt-token"
    }
```

- **POST** `/login`: Autentica al usuario y devuelve un JWT.
  - **Request Body**:
    ```json
    {
      "username": "john.doe",
      "password": "password123"
    }
    ```
   - **Response**:
    
Devuelve un JWT (Token de autenticación) que se puede usar para acceder a rutas protegidas.
```
    {
      "token": "your-jwt-token"
    }
```

### 2. Gestión de Usuarios

- **POST** `/api/users?roleName=ROLE_NAME`: Crea un nuevo usuario con el rol especificado. Solo accesible por administradores.
- **GET** `/api/users`: Obtiene la lista de todos los usuarios. Solo accesible por administradores.
- **GET** `/api/users/{id}`: Obtiene la información de un usuario por su ID. Solo accesible por administradores.
- **PUT** `/api/users/{id}`: Actualiza la información de un usuario por su ID. Solo accesible por administradores.
- **DELETE** `/api/users/{id}`: Elimina un usuario por su ID. Solo accesible por administradores.


### 3. Gestión de Reservas

- **POST** `/api/bookings`: Crea una nueva reserva.
  - **Request Body** (ejemplo):
    ```json
    {
      "customerName": "John Doe",
      "service": "Spa",
      "bookingDate": "2025-05-01",
      "user": {
        "id": 1
      },
      "room": {
        "id": 1
      }
    }
    ```

- **GET** `/api/bookings`: Obtiene todas las reservas.

- **GET** `/api/bookings/{id}`: Obtiene una reserva por su ID.

- **PUT** `/api/bookings/{id}`: Actualiza una reserva existente por su ID.

- **DELETE** `/api/bookings/{id}`: Elimina una reserva por su ID.


### 4. Gestión de Habitaciones

- **POST** `/api/rooms`: Crea una nueva habitación.
  - **Request Body** (ejemplo):
    ```json
    {
      "name": "Room 101",
      "capacity": 2
    }
    ```

- **GET** `/api/rooms`: Obtiene todas las habitaciones.

- **GET** `/api/rooms/{id}`: Obtiene una habitación por su ID.

- **PUT** `/api/rooms/{id}`: Actualiza una habitación existente.
  - **Request Body** (ejemplo):
    ```json
    {
      "name": "Room 101 Renovated",
      "capacity": 3
    }
    ```

- **DELETE** `/api/rooms/{id}`: Elimina una habitación por su ID.

### 5. Gestión de Roles

> ⚠️ **Requiere rol ADMIN**

- **POST** `/api/roles`: Crea un nuevo rol.
  - **Request Body** (ejemplo):
    ```json
    {
      "name": "DEVELOPER"
    }
    ```

- **GET** `/api/roles/{roleName}`: Obtiene un rol por su nombre.


## Instalación

###1. Clona el repositorio:

	git clone https://github.com/Lucash00/bookingapi.git
   
###2. Navega al directorio del proyecto:

Abre el proyecto en tu IDE preferido (por ejemplo, IntelliJ IDEA o Eclipse).

Compila y ejecuta el proyecto. Si estás utilizando Maven, puedes hacer lo siguiente:

	mvn spring-boot:run

O si usas Gradle:

	gradle bootRun

Una vez realizado podrás entrar en:

**http://localhost:8080/**

## Swagger

La documentación de la API está disponible a través de Swagger. Una vez que la aplicación esté en funcionamiento, puedes acceder a la documentación interactiva en:

**http://localhost:8080/swagger-ui.html**

o desde la pagina inicial:

**http://localhost:8080/**


## Seguridad (JWT)

Esta API utiliza autenticación basada en **JWT (JSON Web Tokens)** para proteger los endpoints. A continuación se describe cómo funciona y cómo está implementado:

### 🔐 Flujo de Autenticación

1. **Generación del Token**  
   El backend genera un token JWT usando `JwtTokenUtils` al autenticar al usuario.  
   Este token incluye:
   - El nombre de usuario (`subject`)
   - Una lista de roles (`claim` llamado `roles`)
   - Fecha de creación y expiración (1 hora por defecto)

2. **Envío del Token**  
   El cliente debe enviar el token en la cabecera `Authorization` en cada petición protegida, con el formato:

**Authorization: Bearer <jwt_token>**


3. **Validación del Token**  
El filtro `JwtAuthenticationFilter` intercepta las peticiones:
- Extrae y valida el token
- Extrae los roles del token
- Crea un `UsernamePasswordAuthenticationToken` y lo asigna al `SecurityContext`

4. **Roles y Autorización**  
Los roles extraídos del token se convierten a `SimpleGrantedAuthority` con el prefijo `ROLE_`, necesarios para proteger rutas mediante anotaciones como:

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> metodoProtegido() {
    // ...
}
```

## Pruebas

El proyecto incluye pruebas unitarias y de integración para verificar las funcionalidades principales de la API. A continuación se presentan los archivos de prueba:

###1. Tests de la Aplicación Principal

**BookingapiApplicationTests.java:** Pruebas generales de la aplicación para verificar que el contexto se carga correctamente.


###2. Tests de Repositorios

**BookingRepositoryTest.java:** Pruebas unitarias para la capa de repositorio de reservas.

**RoleRepositoryTest.java:** Pruebas unitarias para la capa de repositorio de roles.

**RoomRepositoryTest.java:** Pruebas unitarias para la capa de repositorio de habitaciones.

**UserRepositoryTest.java:** Pruebas unitarias para la capa de repositorio de usuarios.

###3. Tests de Seguridad

**JwtAuthenticationFilterTest.java:** Pruebas para verificar que el filtro de autenticación JWT funciona correctamente.

**JwtTokenUtilsTest.java:** Pruebas para la utilidad que maneja la creación y validación de JWT.

###4. Tests de Servicios

**BookingServiceTest.java:** Pruebas unitarias para la lógica de negocio relacionada con las reservas.

**RoleServiceTest.java:** Pruebas unitarias para la lógica de negocio relacionada con los roles de los usuarios.

**RoomServiceTest.java:** Pruebas unitarias para la lógica de negocio relacionada con las habitaciones.

**UserServiceTest.java:** Pruebas unitarias para la lógica de negocio relacionada con los usuarios.

##Ejecución de las pruebas

Para ejecutar las pruebas, puedes usar el siguiente comando dependiendo de tu sistema de construcción:

Maven:

	mvn test

Gradle:

	gradle test

##Estructura del Proyecto
El proyecto está estructurado de la siguiente manera:

```
/src
  /main
    /java
      /com/bookingapi
        /config           # Configuraciones de seguridad, JWT, JPA
        /controllers      # Controladores REST
        /exceptions       # Manejo de excepciones personalizadas
        /models           # Modelos de la base de datos (User, Booking, etc.)
        /repositories     # Repositorios JPA
        /security         # Autenticación y autorización (JWT)
        /services         # Lógica de negocio
    /resources
      /application.properties  # Configuraciones de la aplicación (base de datos, JWT, etc.)
  /test
    /java
      /com/bookingapi
        /BookingapiApplicationTests.java
        /repositories
          /BookingRepositoryTest.java
          /RoleRepositoryTest.java
          /RoomRepositoryTest.java
          /UserRepositoryTest.java
        /security
          /JwtAuthenticationFilterTest.java
          /JwtTokenUtilsTest.java
        /services
          /BookingServiceTest.java
          /RoleServiceTest.java
          /RoomServiceTest.java
          /UserServiceTest.java
```
          
##Contribuciones

Si deseas contribuir a este proyecto, por favor sigue estos pasos:

Haz un fork del repositorio.

Crea una rama para tu feature

	git checkout -b feature/mi-feature

Realiza tus cambios.

Haz commit de tus cambios 

	git commit -am 'Añadir mi feature'

Haz push a tu rama 

	git push origin feature/mi-feature

Abre un pull request.

##Licencia

Este proyecto está bajo la Licencia **MIT**. Consulta el archivo **LICENSE** para más detalles.