# Sistema de Clínica - Gestión de Citas Médicas

Sistema web para gestión de citas médicas con dos perfiles de usuario: **Paciente** y **Doctor**. Desarrollado con **Spring Boot 3.2.5**, **Spring Security**, **JSP** y **Bootstrap 5**.

---

## Requisitos

- **Java 21** o superior
- **Apache Maven 3.9+** (o usa el wrapper incluido `mvnw`)
- **NetBeans** (recomendado) o cualquier IDE

---

## Cómo ejecutar

### Opción 1: Desde NetBeans (recomendado)

1. Abre NetBeans → **File → Open Project**
2. Selecciona la carpeta `mavenproject1`
3. Espera a que Maven descargue las dependencias
4. Click derecho en el proyecto → **Run**
5. Abre el navegador en: `http://localhost:8080/signin`

> **Si obtienes un error de "puerto en uso"**: otro proceso está usando el puerto 8080. Ejecuta en terminal:
> ```bash
> fuser -k 8080/tcp
> ```

### Opción 2: Desde terminal

```bash
# Con Maven instalado
mvn spring-boot:run

# O con el wrapper incluido
./mvnw spring-boot:run
```

Luego abre `http://localhost:8080/signin`

---

## Usuarios de prueba

Los usuarios se crean automáticamente al iniciar la aplicación (datos en memoria).

| Rol | Email | Contraseña |
|---|---|---|
| Doctor | dr.garcia@clinica.com | admin123 |
| Doctor | dr.lopez@clinica.com | admin123 |
| Paciente | juan@email.com | user123 |
| Paciente | maria@email.com | user123 |

---

## Flujo del programa

### Pantalla de Login

```
http://localhost:8080/signin
```

1. Ingresa email y contraseña
2. Si las credenciales son correctas → redirige según el rol
3. Si fallan → muestra mensaje de error

---

### Flujo del Paciente

```
Login → /paciente/dashboard
```

**Dashboard del Paciente:**
- Saludo con su nombre
- Tabla con citas pendientes (Doctor, Fecha, Hora, Estado)
- Botón **"Agendar Nueva Cita"** → formulario con lista de doctores
- Botón **"Ver Todas Mis Citas"** → historial completo
- Botón **"Cancelar"** en citas con estado PENDIENTE
- Botón **"Cerrar Sesión"** en la barra superior

**Agendar Cita:**
1. Selecciona un doctor de la lista
2. Elige la fecha (no puede ser anterior a hoy)
3. Elige la hora (turnos de 30 min, 9:00 a 17:00)
4. Click en **"Confirmar Cita"** → se crea con estado PENDIENTE
5. Redirige al dashboard con la cita en la tabla

**Mis Citas:**
- Tabla con todas las citas (pendientes, confirmadas, canceladas)
- Solo puede cancelar citas con estado PENDIENTE

---

### Flujo del Doctor

```
Login → /doctor/dashboard
```

**Dashboard del Doctor:**
- Saludo con su nombre
- Tabla con su agenda (Paciente, Fecha, Hora, Estado)
- **Confirmar** → cambia estado de PENDIENTE a CONFIRMADA
- **Cancelar** → cambia estado a CANCELADA
- **Ver Paciente** → abre detalle con datos del paciente e historial

**Detalle del Paciente:**
- Nombre, email, teléfono
- Historial de citas que ha tenido con este doctor

---

## Funcionalidades por perfil

### Paciente puede:
| Acción | Disponible |
|---|---|
| Ver doctores disponibles | ✅ |
| Agendar cita | ✅ |
| Ver citas pendientes | ✅ |
| Ver historial de citas | ✅ |
| Cancelar cita propia (solo si está PENDIENTE) | ✅ |
| Acceder a página de doctor | ❌ (403) |

### Doctor puede:
| Acción | Disponible |
|---|---|
| Ver agenda de citas | ✅ |
| Confirmar citas pendientes | ✅ |
| Cancelar citas (pendientes o confirmadas) | ✅ |
| Ver datos del paciente | ✅ |
| Ver historial de citas del paciente | ✅ |
| Acceder a página de paciente | ❌ (403) |

---

## Estados de las citas

| Estado | Descripción |
|---|---|
| **PENDIENTE** | Recién creada por el paciente |
| **CONFIRMADA** | Confirmada por el doctor |
| **CANCELADA** | Cancelada por paciente o doctor |
| **COMPLETADA** | Reservada para uso futuro |

---

## Reglas de negocio

- Horarios: Lunes a Viernes, 9:00–17:00, turnos de **30 minutos**
- No se permite agendar en **fechas pasadas**
- No se permite agendar en **slots ya ocupados**
- Un paciente solo puede cancelar citas en estado **PENDIENTE**
- Un doctor puede cancelar citas **PENDIENTE** y **CONFIRMADA**

---

## Estructura del proyecto

```
src/main/java/com/clinica/
├── ClinicaApplication.java          ← Punto de entrada
├── ServletInitializer.java          ← Soporte WAR
├── config/
│   ├── SecurityConfig.java          ← Spring Security (roles, login)
│   ├── WebConfig.java               ← ViewControllers + recursos
│   └── PasswordConfig.java          ← BCryptPasswordEncoder
├── model/
│   ├── Usuario.java                 ← Entidad base
│   ├── Doctor.java                  ← Datos del doctor
│   ├── Paciente.java                ← Datos del paciente
│   ├── Cita.java                    ← Citas médicas
│   └── EstadoCita.java              ← Enum de estados
├── service/
│   ├── UsuarioService.java          ← Interfaz
│   ├── CitaService.java             ← Interfaz
│   └── impl/
│       ├── MemoryUsuarioService.java    ← Impl. en memoria
│       └── MemoryCitaService.java       ← Impl. en memoria
├── repository/                      ← Interfaces vacías (futuro MySQL)
│   ├── UsuarioRepository.java
│   └── CitaRepository.java
└── controller/
    ├── PacienteController.java      ← Endpoints del paciente
    └── DoctorController.java        ← Endpoints del doctor

src/main/webapp/WEB-INF/jsp/
├── login.jsp                        ← Página de inicio de sesión
├── error.jsp                        ← Error genérico
├── acceso-denegado.jsp              ← Error 403
├── paciente/
│   ├── dashboard.jsp                ← Panel principal
│   ├── agendar.jsp                  ← Formulario agendar
│   └── mis-citas.jsp                ← Historial citas
└── doctor/
    ├── dashboard.jsp                ← Panel del doctor
    └── detalle-paciente.jsp         ← Info del paciente

src/main/resources/
└── application.properties           ← Configuración
```

---

## Migrar a MySQL (futuro)

El sistema está preparado para cambiar a base de datos con pasos mínimos:

1. Agregar al `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
   <dependency>
       <groupId>com.mysql</groupId>
       <artifactId>mysql-connector-j</artifactId>
   </dependency>
   ```

2. Configurar `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/clinica
   spring.datasource.username=root
   spring.datasource.password=tu_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Agregar anotaciones JPA a las entidades (`@Entity`, `@Table`, `@Id`, `@GeneratedValue`)

4. Implementar los repositorios en `com.clinica.repository/`

**Lo que NO cambia:** Controllers, vistas JSP, lógica de negocio.

---

## Endpoints

| URL | Método | Acceso | Descripción |
|---|---|---|---|
| `/signin` | GET | Público | Página de login |
| `/do-login` | POST | Público | Procesar login |
| `/logout` | POST | Autenticado | Cerrar sesión |
| `/paciente/dashboard` | GET | ROLE_PACIENTE | Dashboard paciente |
| `/paciente/agendar` | GET | ROLE_PACIENTE | Formulario agendar |
| `/paciente/agendar` | POST | ROLE_PACIENTE | Crear cita |
| `/paciente/mis-citas` | GET | ROLE_PACIENTE | Historial citas |
| `/paciente/cancelar-cita` | POST | ROLE_PACIENTE | Cancelar cita |
| `/doctor/dashboard` | GET | ROLE_DOCTOR | Dashboard doctor |
| `/doctor/confirmar-cita` | POST | ROLE_DOCTOR | Confirmar cita |
| `/doctor/cancelar-cita` | POST | ROLE_DOCTOR | Cancelar cita |
| `/doctor/paciente/{id}` | GET | ROLE_DOCTOR | Detalle paciente |
| `/error` | GET | Público | Error genérico |
| `/acceso-denegado` | GET | Público | Error 403 |

---

## Tecnologías

- **Spring Boot 3.2.5**
- **Spring Security 6.2.4**
- **Spring MVC**
- **Jakarta Servlet API**
- **JSP + JSTL**
- **Bootstrap 5.3.3** (CDN)
- **Tomcat 10.1.20** (embebido)
- **BCrypt** (encriptación de contraseñas)
