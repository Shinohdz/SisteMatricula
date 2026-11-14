use("sistemagestionacademica_db");

db.usuarios.drop();
db.estudiantes.drop();
db.cursos.drop();
db.matriculas.drop();

// Contraseña del usuario 'admin': admin123
// Contraseña del usuario 'bryan': poo2026
// Las contraseñas se almacenan en hash SHA-256.

db.usuarios.insertMany([
    {
        username: "admin",
        passwordHash: "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9",
        nombreCompleto: "Administrador Académico",
        rol: "ADMIN"
    },
    {
        username: "bryan",
        passwordHash: "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918",
        nombreCompleto: "Bryan Vladimir Medrano Villatoro",
        rol: "ADMIN"
    }
]);

db.estudiantes.insertMany([
    {
        carnet: "MV23002",
        nombre: "Bryan Vladimir Medrano Villatoro",
        documento: "06123456-7",
        correo: "mv23002@ues.edu.sv",
        activo: true,
        cursosEnCurso: ["POO115", "ESD115"]
    },
    {
        carnet: "HF23001",
        nombre: "José Luis Hernández Flores",
        documento: "05234567-8",
        correo: "hf23001@ues.edu.sv",
        activo: true,
        cursosEnCurso: ["POO115"]
    },
    {
        carnet: "LG25024",
        nombre: "Joseline Alexandra Lemus Granados",
        documento: "04345678-9",
        correo: "lg25024@ues.edu.sv",
        activo: true,
        cursosEnCurso: ["POO115", "TSI115"]
    },
    {
        carnet: "FA26017",
        nombre: "Francisco Javier Fuentes Argueta",
        documento: "03456789-0",
        correo: "fa26017@ues.edu.sv",
        activo: true,
        cursosEnCurso: ["ESD115"]
    },
    {
        carnet: "RM22008",
        nombre: "Carlos Eduardo Rivas Mendoza",
        documento: "02567890-1",
        correo: "rm22008@ues.edu.sv",
        activo: true,
        cursosEnCurso: ["MAT215"]
    },
    {
        carnet: "GC24015",
        nombre: "Ana Gabriela Gómez Castro",
        documento: "01678901-2",
        correo: "gc24015@ues.edu.sv",
        activo: true,
        cursosEnCurso: []
    }
]);

db.cursos.insertMany([
    {
        codCurso: "POO115",
        nombre: "Programación Orientada a Objetos",
        descripcion: "Diseño de clases, herencia, colecciones y Swing",
        tutor: "Ing. Milton Wilfredo Alfaro Pérez",
        cupoMaximo: 30,
        matriculadosActivos: 3
    },
    {
        codCurso: "ESD115",
        nombre: "Estructura de Datos",
        descripcion: "Listas enlazadas, pilas, colas y árboles",
        tutor: "Ing. Roberto Carlos Mejía",
        cupoMaximo: 25,
        matriculadosActivos: 2
    },
    {
        codCurso: "TSI115",
        nombre: "Teoría de Sistemas",
        descripcion: "Enfoque sistémico y modelado organizacional",
        tutor: "Inga. Claudia Patricia Morales",
        cupoMaximo: 30,
        matriculadosActivos: 1
    },
    {
        codCurso: "MAT215",
        nombre: "Matemática II",
        descripcion: "Cálculo integral y series",
        tutor: "Lic. Ernesto Alonso Renderos",
        cupoMaximo: 35,
        matriculadosActivos: 1
    },
    {
        codCurso: "BDD115",
        nombre: "Bases de Datos I",
        descripcion: "Modelado relacional y NoSQL con MongoDB",
        tutor: "Ing. Mario Ernesto Valladares",
        cupoMaximo: 20,
        matriculadosActivos: 0
    },
    {
        codCurso: "PRN215",
        nombre: "Programación II",
        descripcion: "Desarrollo de aplicaciones de escritorio",
        tutor: "Ing. Milton Wilfredo Alfaro Pérez",
        cupoMaximo: 2,
        matriculadosActivos: 0
    }
]);

db.matriculas.insertMany([
    {
        idMatricula: "MAT-001",
        carnetEstudiante: "MV23002",
        codCurso: "POO115",
        fechaMatricula: "2026-11-01",
        estado: "ACTIVA"
    },
    {
        idMatricula: "MAT-002",
        carnetEstudiante: "HF23001",
        codCurso: "POO115",
        fechaMatricula: "2026-11-01",
        estado: "ACTIVA"
    },
    {
        idMatricula: "MAT-003",
        carnetEstudiante: "LG25024",
        codCurso: "POO115",
        fechaMatricula: "2026-11-02",
        estado: "ACTIVA"
    },
    {
        idMatricula: "MAT-004",
        carnetEstudiante: "MV23002",
        codCurso: "ESD115",
        fechaMatricula: "2026-11-02",
        estado: "ACTIVA"
    },
    {
        idMatricula: "MAT-005",
        carnetEstudiante: "FA26017",
        codCurso: "ESD115",
        fechaMatricula: "2026-11-03",
        estado: "ACTIVA"
    },
    {
        idMatricula: "MAT-006",
        carnetEstudiante: "LG25024",
        codCurso: "TSI115",
        fechaMatricula: "2026-11-03",
        estado: "ACTIVA"
    },
    {
        idMatricula: "MAT-007",
        carnetEstudiante: "RM22008",
        codCurso: "MAT215",
        fechaMatricula: "2026-11-04",
        estado: "ACTIVA"
    }
]);

print("Base de datos inicializada correctamente.");
