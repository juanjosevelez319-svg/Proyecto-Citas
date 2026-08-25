# Sistema de Gestión de Citas Hospitalarias

## Descripción

El Sistema de Gestión de Citas Hospitalarias es una aplicación web desarrollada con Spring Boot que permite administrar usuarios, médicos, horarios y citas médicas mediante una interfaz web desarrollada con Thymeleaf y Bootstrap.

El sistema incorpora autenticación y autorización mediante Spring Security, permitiendo diferenciar las funciones disponibles para administradores y usuarios.

---

## Objetivo

Desarrollar una aplicación que permita administrar de manera segura las citas médicas de un hospital, facilitando la gestión de usuarios, médicos, horarios disponibles y reservas de citas.

---

## Tecnologías utilizadas

- Java 26
- Spring Boot 4.1.0
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- Bootstrap 5
- Base de datos H2
- Maven

---

## Funcionalidades principales

### Gestión de usuarios

- Registro de usuarios.
- Inicio de sesión.
- Recuperación de contraseña mediante correo electrónico registrado.
- Edición de información.
- Eliminación de usuarios (solo administrador).
- Contraseñas almacenadas utilizando BCrypt.

### Gestión de médicos

- Registrar médicos.
- Editar médicos.
- Eliminar médicos.
- Consultar médicos registrados.
- Consultar los horarios asociados a un médico específico.

### Gestión de horarios

- Registrar horarios disponibles para cada médico.
- Editar horarios.
- Eliminar horarios.
- Visualizar horarios disponibles.
- Validación para impedir horarios duplicados o que se traslapen para un mismo médico.

### Gestión de citas

- Registrar citas médicas.
- Confirmar citas.
- Cancelar citas.
- Visualizar citas registradas.

### Reportes de citas

- Panel de reportes con filtros combinables por rango de fechas, médico, estado de la cita y especialidad.
- Resumen del reporte con el total de citas encontradas y su distribución por estado (pendientes, confirmadas y canceladas).
- Exportación del reporte filtrado a un archivo CSV (reporte_citas.csv), compatible con programas de hojas de cálculo como Excel o Google Sheets.

---

## Seguridad

El sistema utiliza Spring Security para controlar el acceso.

### Administrador

Puede:

- Administrar usuarios.
- Administrar médicos.
- Administrar horarios.
- Confirmar citas.
- Cancelar cualquier cita.
- Generar y exportar reportes de citas.
- Visualizar toda la información.

### Usuario

Puede:

- Registrarse.
- Iniciar sesión.
- Recuperar su contraseña.
- Consultar y editar su información.
- Crear citas.
- Cancelar sus propias citas.

---

## Reglas de negocio implementadas

- No se permite registrar usuarios con el mismo correo electrónico.
- No se permite registrar usuarios con la misma cédula.
- Las contraseñas se almacenan cifradas utilizando BCrypt.
- Un médico no puede tener horarios que se traslapen en el mismo día.
- Un usuario únicamente puede editar su propia información.
- Solo un administrador puede eliminar usuarios.
- El acceso a cada módulo depende del rol del usuario autenticado.
- El reporte de citas solo devuelve resultados que cumplan simultáneamente con todos los filtros seleccionados.

---

## Estructura del proyecto


src
│
├── Controller
│   ├── CitaController
│   ├── HorarioController
│   ├── InicioController
│   ├── LoginController
│   ├── MedicoController
│   ├── ReporteController
│   └── UsuariosController
│
├── Model
│   ├── Usuario
│   ├── Medico
│   ├── Horarios
│   └── Citas
│
├── Repository
│   ├── UsuariosRepository
│   ├── MedicoRepository
│   ├── HorarioRepository
│   └── CitaRepository
│
├── Service
│   ├── UsuariosService
│   ├── MedicoService
│   ├── HorarioService
│   ├── CitasService
│   └── ReporteService
│
├── Security
│   ├── LoginSuccessHandler
│   └── UsuarioDetailsService
│
└── Config
    ├── SecurityConfig
    └── DataInitializer


---

## Base de datos

El proyecto utiliza H2 Database.

La consola puede accederse desde:


http://localhost:8080/h2-console


Configuración:


JDBC URL:
jdbc:h2:file:./citasdb

Usuario:
sa

Contraseña:
(vacía)


---

## Usuarios creados automáticamente

Administrador


Correo:
admin@hospital.com

Contraseña:
admin123


Usuario


Correo:
usuario@hospital.com

Contraseña:
usuario123


---

## Ejecución del proyecto

1. Clonar el repositorio.


git clone <url-del-repositorio>


2. Abrir el proyecto en Visual Studio Code o IntelliJ IDEA.

3. Ejecutar la clase:


CitasApplication.java


4. Abrir el navegador:


http://localhost:8080/login


---

## Autor

*Dylan Rodríguez*
*Juan Jose Velez Cayola*

Proyecto desarrollado para el curso de Programación IV utilizando Spring Boot, Spring Security, Spring Data JPA, Thymeleaf y H2 Database.