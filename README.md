# Sistema de Clinica - Gestion de Citas Medicas

Aplicacion web para gestionar citas medicas con dos perfiles: **Paciente** y
**Doctor**. El proyecto esta hecho con **Java 21**, **Spring Boot 3.2.5**,
**Spring MVC**, **Spring Security**, **Spring Data JPA**, **MySQL**, **JSP** y
**Bootstrap 5**.

> Nota: el sistema ya no trabaja con memoria local. Las entidades, usuarios y
> citas se guardan en MySQL mediante JPA/Hibernate.

---

## Estado actual

- Login oficial: `http://localhost:8080/signin`
- Persistencia: MySQL
- Seguridad: Spring Security con roles `ROLE_PACIENTE` y `ROLE_DOCTOR`
- Passwords: BCrypt
- Vistas: JSP dentro de `src/main/webapp/WEB-INF/jsp`
- Despliegue objetivo: Railway u otro hosting compatible con variables de entorno
- Empaquetado: WAR ejecutable con Spring Boot

---

## Requisitos

- Java 21 o superior
- Maven 3.9+ o el wrapper incluido:
  - Windows: `mvnw.cmd`
  - Linux/Mac: `./mvnw`
- MySQL local o una base MySQL en Railway

---

## Configuracion de base de datos

La configuracion esta en:

```text
src/main/resources/application.properties
```

Por defecto intenta conectarse a MySQL local:

```properties
MYSQLHOST=localhost
MYSQLPORT=3306
MYSQLDATABASE=clinica_citas
MYSQLUSER=root
MYSQLPASSWORD=
```

La URL real se arma asi:

```properties
spring.datasource.url=jdbc:mysql://${MYSQLHOST:localhost}:${MYSQLPORT:3306}/${MYSQLDATABASE:clinica_citas}?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true
```

Para Railway, esas variables normalmente las inyecta el plugin/servicio de
MySQL. Tambien se puede controlar:

```properties
DDL_AUTO=update
SHOW_SQL=false
```

Recomendacion:

- En desarrollo: `DDL_AUTO=update`
- En produccion real: usar migraciones o controlar cambios de esquema con mas cuidado

---

## Como ejecutar localmente

Primero crea la base de datos si estas usando MySQL local:

```sql
CREATE DATABASE clinica_citas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Luego ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

Abre:

```text
http://localhost:8080/signin
```

Para compilar el proyecto:

```powershell
.\mvnw.cmd -DskipTests package
```

El WAR se genera en:

```text
target/clinica-citas-1.0-SNAPSHOT.war
```

---

## Usuarios

La aplicacion no tiene registro de usuarios desde la web. Los usuarios deben
crearse directamente en la base de datos o mediante el `DataSeeder`.

Al iniciar, `DataSeeder` crea usuarios de prueba solo si la tabla `usuarios`
esta vacia:

| Rol | Email | Password |
|---|---|---|
| Doctor | dr.garcia@clinica.com | admin123 |
| Doctor | dr.lopez@clinica.com | admin123 |
| Paciente | juan@email.com | user123 |
| Paciente | maria@email.com | user123 |

Importante: las contrasenas en la tabla `usuarios.password` deben estar en
formato BCrypt. No se deben guardar en texto plano.

---

## Flujo del paciente

Despues del login, un paciente entra a:

```text
/paciente/dashboard
```

Puede:

- Ver sus citas activas: `PENDIENTE` y `CONFIRMADA`
- Agendar una nueva cita
- Ver su historial completo en `/paciente/mis-citas`
- Cancelar citas propias solo si estan `PENDIENTE`

Reglas al agendar:

- Solo lunes a viernes
- Horario de 9:00 a 17:00
- Turnos cada 30 minutos
- No se permiten fechas pasadas
- No se permite ocupar un horario ya reservado por una cita activa
- Una cita cancelada libera el horario

---

## Flujo del doctor

Despues del login, un doctor entra a:

```text
/doctor/dashboard
```

Puede:

- Ver sus citas activas: `PENDIENTE` y `CONFIRMADA`
- Confirmar citas pendientes
- Cancelar citas pendientes o confirmadas
- Marcar una cita confirmada como completada
- Ver el detalle de un paciente y su historial con ese doctor

---

## Estados de cita

| Estado | Uso |
|---|---|
| `PENDIENTE` | Cita creada por el paciente, esperando confirmacion |
| `CONFIRMADA` | Cita aceptada por el doctor |
| `CANCELADA` | Cita cancelada por paciente o doctor |
| `COMPLETADA` | Cita atendida y cerrada por el doctor |

Los dashboards muestran solo citas activas: `PENDIENTE` y `CONFIRMADA`.
El historial muestra tambien `CANCELADA` y `COMPLETADA`.

---

## Endpoints principales

| URL | Metodo | Acceso | Descripcion |
|---|---|---|---|
| `/` | GET | Publico | Redirige a `/signin` |
| `/signin` | GET | Publico | Pagina de login |
| `/do-login` | POST | Publico | Procesa el login |
| `/logout` | POST | Autenticado | Cierra sesion |
| `/paciente/dashboard` | GET | `ROLE_PACIENTE` | Dashboard del paciente |
| `/paciente/agendar` | GET | `ROLE_PACIENTE` | Formulario para agendar |
| `/paciente/agendar` | POST | `ROLE_PACIENTE` | Crea una cita |
| `/paciente/mis-citas` | GET | `ROLE_PACIENTE` | Historial del paciente |
| `/paciente/cancelar-cita` | POST | `ROLE_PACIENTE` | Cancela una cita pendiente |
| `/doctor/dashboard` | GET | `ROLE_DOCTOR` | Dashboard del doctor |
| `/doctor/confirmar-cita` | POST | `ROLE_DOCTOR` | Confirma una cita pendiente |
| `/doctor/completar-cita` | POST | `ROLE_DOCTOR` | Marca una cita confirmada como completada |
| `/doctor/cancelar-cita` | POST | `ROLE_DOCTOR` | Cancela una cita pendiente o confirmada |
| `/doctor/paciente/{id}` | GET | `ROLE_DOCTOR` | Detalle e historial del paciente |
| `/acceso-denegado` | GET | Publico | Pagina 403 |
| `/error` | GET | Publico | Error generico |

Todos los formularios POST usan token CSRF.

---

## Estructura del proyecto

```text
src/main/java/com/clinica/
|-- ClinicaApplication.java
|-- ServletInitializer.java
|-- config/
|   |-- DataSeeder.java
|   |-- PasswordConfig.java
|   |-- SecurityConfig.java
|   `-- WebConfig.java
|-- controller/
|   |-- DoctorController.java
|   `-- PacienteController.java
|-- model/
|   |-- Cita.java
|   |-- Doctor.java
|   |-- EstadoCita.java
|   |-- Paciente.java
|   `-- Usuario.java
|-- repository/
|   |-- CitaRepository.java
|   |-- DoctorRepository.java
|   |-- PacienteRepository.java
|   `-- UsuarioRepository.java
`-- service/
    |-- CitaService.java
    |-- UsuarioService.java
    `-- impl/
        |-- JpaCitaService.java
        `-- JpaUsuarioService.java

src/main/webapp/WEB-INF/jsp/
|-- login.jsp
|-- error.jsp
|-- acceso-denegado.jsp
|-- doctor/
|   |-- dashboard.jsp
|   `-- detalle-paciente.jsp
`-- paciente/
    |-- agendar.jsp
    |-- dashboard.jsp
    `-- mis-citas.jsp
```

---

## Despliegue en Railway

El proyecto esta preparado para usar variables de entorno.

Variables esperadas:

```text
PORT
MYSQLHOST
MYSQLPORT
MYSQLDATABASE
MYSQLUSER
MYSQLPASSWORD
DDL_AUTO
SHOW_SQL
```

El `Dockerfile` compila el WAR y lo ejecuta con Java:

```dockerfile
CMD ["java", "-jar", "app.war"]
```

Pasos generales:

1. Crear proyecto en Railway.
2. Agregar servicio MySQL.
3. Conectar el repositorio.
4. Verificar que Railway exponga las variables `MYSQL...`.
5. Desplegar.
6. Entrar a la URL publica y abrir `/signin`.

---

## Problema Maven con Avast / PKIX

Si Maven falla con un error parecido a:

```text
PKIX path building failed
unable to find valid certification path to requested target
```

en esta maquina se detecto que Avast intercepta HTTPS con el certificado:

```text
Avast Web/Mail Shield Root
```

La solucion aplicada fue crear un truststore local para Maven:

```text
C:\Users\User\.m2\avast-cacerts
```

y guardar esta variable de usuario:

```text
MAVEN_OPTS=-Djavax.net.ssl.trustStore=C:\Users\User\.m2\avast-cacerts -Djavax.net.ssl.trustStorePassword=changeit
```

Despues de eso, el proyecto compila correctamente con:

```powershell
.\mvnw.cmd -DskipTests package
```

---

## Tecnologias

- Java 21
- Spring Boot 3.2.5
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JSP + JSTL
- Bootstrap 5.3.3
- Tomcat embebido
- BCrypt
