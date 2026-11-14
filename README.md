# Sistema de Gestión Académica (Clave 3) - POO115

Proyecto de la asignatura **Programación Orientada a Objetos (POO115)** - Universidad de El Salvador (Facultad de Ingeniería y Arquitectura - Escuela de Ingeniería de Sistemas Informáticos).

## Integrantes del Equipo (Grupo 3 - GT01)
* **Fuentes Argueta, Francisco Javier** (FA26017) - `@frandlff`
* **Hernández Flores, José Luis** (HF23001) - `@Shinohdz`
* **Lemus Granados, Joseline Alexandra** (LG25024) - `@lg25024-cell`
* **Medrano Villatoro, Bryan Vladimir** (MV23002) - `@bryanvilla2018-u`

## Requisitos Técnicos
* **Java Development Kit (JDK):** 21
* **IDE:** Apache NetBeans 22 (Proyecto Maven)
* **Base de Datos:** MongoDB Community Server ejecutándose en `mongodb://localhost:27017`
* **Base de datos del sistema:** `sistemagestionacademica_db`
* **Dependencias Maven:** `mongodb-driver-sync` (4.11.1) y `slf4j-nop` (2.0.9)

## Arquitectura del Proyecto (5 Paquetes)
* `app`: Clase principal `GestionAcademica.java` que inicia el sistema dentro del Event Dispatch Thread (EDT).
* `conexion`: Clase `ConexionMongo.java` para gestionar la conexión hacia MongoDB.
* `modelo`: Entidades de dominio (`Usuario`, `Estudiante`, `Curso`, `Matricula`) con encriptación SHA-256, `HashSet`, `Comparable` y `Comparator`.
* `dao`: Capa de acceso a datos (`UsuarioDAO`, `EstudianteDAO`, `CursoDAO`, `MatriculaDAO`).
* `vista`: Formularios construidos 100% con `javax.swing` puro (`frmInicio`, `frmMenu`, `frmEstudiantes`, `frmCursos`, `frmMatriculas`).

## Instrucciones de Ejecución
1. Asegúrese de que el servicio local de MongoDB (`localhost:27017`) esté activo.
2. Abra **MongoDB Compass** o la terminal **`mongosh`** y ejecute el contenido del script `init_mongodb.js` para crear la base de datos `sistemagestionacademica_db`, las 4 colecciones (`usuarios`, `estudiantes`, `cursos`, `matriculas`) y los registros iniciales.
3. Abra el proyecto `SisteMatricula` en **Apache NetBeans 22**.
4. Ejecute la clase principal `app.GestionAcademica`.

## Credenciales de Acceso (Login)
* **Usuario:** `admin` | **Contraseña:** `admin123`
* **Usuario:** `bryan` | **Contraseña:** `poo2026`
