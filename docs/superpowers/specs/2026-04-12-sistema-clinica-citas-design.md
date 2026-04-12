# Diseño: Sistema de Clínica - Citas Médicas

**Fecha:** 2026-04-12  
**Estado:** Aprobado  
**Stack:** Spring Boot + Spring MVC + Spring Security + JSP + Bootstrap  
**Empaquetado:** WAR (desplegable en Tomcat o ejecutable con Spring Boot embebido)

---

## Resumen

Sistema web para gestión de citas médicas con dos perfiles de usuario: **Paciente** y **Doctor**. Los usuarios son creados por un administrador (no hay registro público). Los datos se almacenan en memoria con interfaces preparadas para migrar a MySQL en el futuro.

---

## Perfiles y Funcionalidades

### Paciente
- Ver doctores disponibles (nombre, especialidad).
- Agendar citas (seleccionar doctor, fecha, hora).
- Ver citas pendientes.
- Cancelar citas propias.

### Doctor
- Ver agenda de citas.
- Confirmar citas pendientes.
- Cancelar citas.
- Ver datos básicos del paciente (nombre, email, historial de citas con este doctor).

---

## Arquitectura

### Estructura del proyecto

```
src/main/java/com/clinica/
├── ClinicaApplication.java
├── ServletInitializer.java
│
├── controller/
│   ├── AuthController.java
│   ├── PacienteController.java
│   ├── DoctorController.java
│   └── AdminCuentaController.java
│
├── model/
│   ├── Usuario.java
│   ├── Doctor.java
│   ├── Paciente.java
│   └── Cita.java
│
├── service/
│   ├── UsuarioService.java
│   ├── CitaService.java
│   └── impl/
│       ├── MemoryUsuarioService.java
│       └── MemoryCitaService.java
│
├── repository/
│   ├── CitaRepository.java      ← Interfaz vacía (futuro MySQL)
│   └── UsuarioRepository.java   ← Interfaz vacía (futuro MySQL)
│
└── config/
    ├── SecurityConfig.java
    └── WebConfig.java

src/main/webapp/
├── WEB-INF/jsp/
│   ├── login.jsp
│   ├── error.jsp
│   ├── acceso-denegado.jsp
│   ├── paciente/
│   │   ├── dashboard.jsp
│   │   ├── agendar.jsp
│   │   └── mis-citas.jsp
│   └── doctor/
│       ├── dashboard.jsp
│       └── detalle-paciente.jsp
└── resources/
    └── css/custom.css
```

### Capas

| Capa | Responsabilidad |
|---|---|
| `controller` | Recibir peticiones HTTP, devolver vistas JSP |
| `service` | Lógica de negocio (interfaces + impl. en memoria) |
| `model` | Entidades/POJOs |
| `repository` | Interfaces vacías preparadas para Spring Data JPA |
| `config` | Seguridad y resolución de vistas |

---

## Modelo de Datos

### Usuario (base)
| Campo | Tipo | Detalle |
|---|---|---|
| `id` | Long | Generado con AtomicLong |
| `nombre` | String | No nulo |
| `email` | String | Único |
| `password` | String | BCrypt |
| `rol` | String | ROLE_PACIENTE / ROLE_DOCTOR |

### Doctor
| Campo | Tipo | Detalle |
|---|---|---|
| `usuario` | Usuario | Referencia |
| `especialidad` | String | Ej: "Cardiología" |

### Paciente
| Campo | Tipo | Detalle |
|---|---|---|
| `usuario` | Usuario | Referencia |
| `telefono` | String | Opcional |

### Cita
| Campo | Tipo | Detalle |
|---|---|---|
| `id` | Long | Generado con AtomicLong |
| `doctorId` | Long | FK a Doctor |
| `pacienteId` | Long | FK a Paciente |
| `fecha` | LocalDate | Fecha de la cita |
| `hora` | LocalTime | Hora de la cita |
| `estado` | Enum | PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA |
| `notas` | String | Opcional |

### Estados de Cita
- **PENDIENTE:** Recién creada por el paciente.
- **CONFIRMADA:** Confirmada por el doctor.
- **CANCELADA:** Cancelada por paciente o doctor.
- **COMPLETADA:** Reservada para uso futuro.

### Horarios
- Lunes a Viernes, 9:00–17:00, turnos de 30 minutos.
- No se permite agendar en fechas pasadas ni en slots ya ocupados.

---

## Flujo de Pantallas

### 1. Login (`/login`)
- Formulario email + password.
- Error: mensaje inline.
- Éxito: redirige según rol al dashboard correspondiente.

### 2. Dashboard Paciente (`/paciente/dashboard`)
- Saludo con nombre.
- Enlace a "Agendar cita".
- Tabla de citas pendientes con botón "Cancelar".

### 3. Agendar Cita (`/paciente/agendar`)
- Lista de doctores (nombre, especialidad).
- Selección de fecha y hora.
- Confirmación de cita.

### 4. Mis Citas (`/paciente/mis-citas`)
- Tabla: Doctor | Fecha | Hora | Estado | Acción (Cancelar).

### 5. Dashboard Doctor (`/doctor/dashboard`)
- Saludo con nombre.
- Tabla de citas (paciente, fecha, hora, estado).
- Botones: Confirmar, Cancelar, Ver datos del paciente.

### 6. Detalle Paciente (`/doctor/paciente/{id}`)
- Nombre, email del paciente.
- Historial de citas con este doctor.

---

## Seguridad

### Spring Security
- Autenticación por formulario.
- Password con BCrypt.
- Sesión basada en cookies.
- Endpoints protegidos por rol:
  - `/paciente/**` → `ROLE_PACIENTE`
  - `/doctor/**` → `ROLE_DOCTOR`

### Usuarios precargados (en memoria)

| Usuario | Email | Password | Rol |
|---|---|---|---|
| Dr. García | dr.garcia@clinica.com | admin123 | DOCTOR |
| Dr. López | dr.lopez@clinica.com | admin123 | DOCTOR |
| Juan Pérez | juan@email.com | user123 | PACIENTE |
| María Gómez | maria@email.com | user123 | PACIENTE |

### Manejo de Errores
- **403:** `/acceso-denegado`
- **404:** Página no encontrada.
- **500:** Error interno con botón de volver al dashboard.
- **Validación de formularios:** Mensajes inline.

---

## Frontend

- **Bootstrap 5** vía CDN en todos los JSP.
- CSS custom mínimo para ajustes específicos (`custom.css`).
- JSP con JSTL (`c:forEach`, `c:if`, etc.).

---

## Extensión a MySQL (Futuro)

### Mecanismo
Perfiles de Spring para alternar entre memoria y JPA:

```java
@Service
@Profile("default")
public class MemoryCitaService implements CitaService { ... }

@Service
@Profile("mysql")
public class JpaCitaService implements CitaService { ... }
```

### Pasos para activar MySQL
1. Agregar `spring-boot-starter-data-jpa` + `mysql-connector-java` al `pom.xml`.
2. Configurar `application.properties` con conexión MySQL.
3. Agregar anotaciones JPA (`@Entity`, `@Table`, etc.) a las entidades.
4. Implementar repositorios JPA en `repository/`.

### Lo que NO cambia
- Controladores, vistas JSP, lógica de negocio.

---

## Dependencias principales

| Dependencia | Versión | Propósito |
|---|---|---|
| spring-boot-starter-web | 3.x | MVC, Tomcat embebido |
| spring-boot-starter-security | 3.x | Autenticación y autorización |
| spring-boot-starter-tomcat | 3.x | Provided (para WAR) |
| jasper | 10.x | Soporte JSP en Tomcat |
| jstl | 1.2 | Tag libraries en JSP |
| bootstrap (CDN) | 5.x | Frontend CSS |
