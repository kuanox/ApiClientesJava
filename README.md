# API de Gestión de Usuarios

### API de Gestión de Usuarios con Spring Boot, JWT y PostgreSQL

🔐 Autenticación JWT | 🛠️ Spring Boot 3.2 | 🐘 PostgreSQL 17 | 📄 Swagger UI

Esta API permite gestionar usuarios con autenticación JWT, validación de datos, manejo de errores y documentación interactiva.


### Tecnologías Utilizadas

| **Tecnología**                     | **Uso**                                |
|------------------------------------|----------------------------------------|
| Spring Boot 3.2                    | Framework backend                      |
| Java 21                            | Lenguaje principal                     |
| PostgreSQL 17                      | Base de datos relacional               |
| JWT (jjwt)                         | Autenticación segura                   |
| Lombok                             | Reducción de código boilerplate        |
| Spring Security                    | Control de acceso y roles              |
| Swagger UI                         | Documentación interactiva              |

### 🚀 Cómo Ejecutar el Proyecto

📥 Requisitos Previos

    Java 21 instalado.

    PostgreSQL 17 instalado y corriendo.

    Maven (para gestión de dependencias).

### Configuración Inicial

1 - Crea una base de datos en PostgreSQL:

    
    CREATE DATABASE user_db;
    
2 - Configura las credenciales en src/main/resources/application.properties::

    
    spring.datasource.url=jdbc:postgresql://localhost:5432/userdb  
    spring.datasource.username=tu_usuario  
    spring.datasource.password=tu_contraseña  
    
3 - Ejecución Local:
    
    mvn spring-boot:run
    
4 - Acceder a API:

   - URL base: `http://localhost:8080/api/v1/users`
   - Documentación Swagger: `http://localhost:8080/swagger-ui/index.html`
   - Autenticación: Utiliza el endpoint `/api/v1/auth/login` para obtener un token JWT.
   - Ejemplo de solicitud de autenticación:

   ```json
   {
       "username": "tu_usuario",
       "password": "tu_contraseña"
   }
   ```

### Endpoints Principales   
| Método | Endpoint                         | Descripción                            |   
|--------|----------------------------------|----------------------------------------|
| POST   | /api/v1/auth/register            | Autenticación y obtención de token JWT |
| POST   | /api/v1/auth/login               | Login Usuario                          |
| GET    | /api/v1/users                    | Obtener todos los usuarios             |
| GET    | /api/v1/users/{id}               | Obtener usuario por ID                 |
| PUT    | /api/v1/users/{id}               | Actualiza un usuario.                  |
| DELETE | /api/v1/users/{id}               | Elimina un usuario.                    |

### Pruebas Unitarias

    

