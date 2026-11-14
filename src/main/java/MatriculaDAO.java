package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Fran
 */


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionMongo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Matricula;
import org.bson.Document;

public class MatriculaDAO {

    private final MongoDatabase db;
    private final EstudianteDAO estudianteDAO;
    private final CursoDAO cursoDAO;

    public MatriculaDAO() {
        this.db = ConexionMongo.obtenerBaseDatos();
        this.estudianteDAO = new EstudianteDAO();
        this.cursoDAO = new CursoDAO();
    }

    public String matricularEstudiante(String carnetEst, String codCur) {
        String claveEst = carnetEst.toUpperCase();
        String claveCur = codCur.toUpperCase();

        Estudiante est = estudianteDAO.mapearEstudiante(
                db.getCollection("estudiantes")
                        .find(Filters.eq("carnet", claveEst)).first()
        );

        Curso cur = cursoDAO.mapearCurso(
                db.getCollection("cursos")
                        .find(Filters.eq("codCurso", claveCur)).first()
        );

        if (est == null) {
            return "El estudiante seleccionado no existe.";
        }

        if (!est.isActivo()) {
            return "El estudiante se encuentra inactivo y no puede inscribir cursos.";
        }

        if (cur == null) {
            return "El curso seleccionado no existe.";
        }

        if (cur.getCuposDisponibles() <= 0) {
            return "El curso " + cur.getCodCurso()
                    + " no tiene cupos disponibles (Cupo máximo alcanzado).";
        }

        if (!est.agregarCurso(cur.getCodCurso())) {
            return "El estudiante " + est.getCarnet()
                    + " ya tiene una matrícula activa en el curso "
                    + cur.getCodCurso() + ".";
        }

        String idMat = generarSiguienteCodigoMatricula();
        String fechaHoy = LocalDate.now().toString();
        int nuevosActivos = cur.getMatriculadosActivos() + 1;

        Document docMat = new Document("idMatricula", idMat)
                .append("carnetEstudiante", est.getCarnet())
                .append("codCurso", cur.getCodCurso())
                .append("fechaMatricula", fechaHoy)
                .append("estado", "ACTIVA");

        db.getCollection("matriculas").insertOne(docMat);

        db.getCollection("estudiantes").updateOne(
                Filters.eq("carnet", est.getCarnet()),
                Updates.set("cursosEnCurso",
                        new ArrayList<>(est.getCursosEnCurso()))
        );

        db.getCollection("cursos").updateOne(
                Filters.eq("codCurso", cur.getCodCurso()),
                Updates.set("matriculadosActivos", nuevosActivos)
        );

        return "OK:" + idMat;
    }

    public String retirarMatricula(String idMatricula) {
        MongoCollection<Document> colMat = db.getCollection("matriculas");

        Document docMat = colMat.find(
                Filters.eq("idMatricula", idMatricula.toUpperCase())
        ).first();

        if (docMat == null) {
            return "La matrícula indicada no existe.";
        }

        String estadoActual = docMat.getString("estado");

        if ("RETIRADA".equalsIgnoreCase(estadoActual)) {
            return "No se puede retirar una matrícula que ya se encuentra en estado RETIRADA.";
        }

        String carnetEst = docMat.getString("carnetEstudiante");
        String codCur = docMat.getString("codCurso");

        colMat.updateOne(
                Filters.eq("idMatricula", docMat.getString("idMatricula")),
                Updates.set("estado", "RETIRADA")
        );

        Estudiante est = estudianteDAO.mapearEstudiante(
                db.getCollection("estudiantes")
                        .find(Filters.eq("carnet", carnetEst)).first()
        );

        if (est != null) {
            est.retirarUnCurso(codCur);

            db.getCollection("estudiantes").updateOne(
                    Filters.eq("carnet", carnetEst),
                    Updates.set("cursosEnCurso",
                            new ArrayList<>(est.getCursosEnCurso()))
            );
        }

        Curso cur = cursoDAO.mapearCurso(
                db.getCollection("cursos")
                        .find(Filters.eq("codCurso", codCur)).first()
        );

        if (cur != null) {
            int nuevosActivos = Math.max(
                    0,
                    cur.getMatriculadosActivos() - 1
            );

            db.getCollection("cursos").updateOne(
                    Filters.eq("codCurso", codCur),
                    Updates.set("matriculadosActivos", nuevosActivos)
            );
        }

        return "OK";
    }

    private String generarSiguienteCodigoMatricula() {
        int max = 0;

        for (Document doc : db.getCollection("matriculas").find()) {
            String idMat = doc.getString("idMatricula");

            if (idMat != null && idMat.startsWith("MAT-")) {
                try {
                    int num = Integer.parseInt(idMat.replace("MAT-", ""));

                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return String.format("MAT-%03d", max + 1);
    }

    public List<Matricula> obtenerMatriculas() {
        List<Matricula> resultado = new ArrayList<>();

        MongoCollection<Document> colEst = db.getCollection("estudiantes");
        MongoCollection<Document> colCur = db.getCollection("cursos");

        for (Document doc : db.getCollection("matriculas").find()) {
            String idMat = doc.getString("idMatricula");
            String carnetEst = doc.getString("carnetEstudiante");
            String codCur = doc.getString("codCurso");
            String fecha = doc.getString("fechaMatricula");
            String estado = doc.getString("estado");

            Estudiante est = estudianteDAO.mapearEstudiante(
                    colEst.find(Filters.eq("carnet", carnetEst)).first()
            );

            Curso cur = cursoDAO.mapearCurso(
                    colCur.find(Filters.eq("codCurso", codCur)).first()
            );

            if (est != null && cur != null) {
                resultado.add(new Matricula(
                        idMat, est, cur, fecha, estado
                ));
            }
        }

        return resultado;
    }

    public List<Estudiante> buscarEstudiantesPorCurso(String codCurso) {
        List<Estudiante> resultado = new ArrayList<>();

        MongoCollection<Document> colEst = db.getCollection("estudiantes");

        for (Document docMat : db.getCollection("matriculas").find(Filters.and(
                Filters.eq("codCurso", codCurso.toUpperCase()),
                Filters.eq("estado", "ACTIVA")
        ))) {
            String carnet = docMat.getString("carnetEstudiante");

            Estudiante est = estudianteDAO.mapearEstudiante(
                    colEst.find(Filters.eq("carnet", carnet)).first()
            );

            if (est != null) {
                resultado.add(est);
            }
        }

        return resultado;
    }

    public List<Curso> buscarCursosPorEstudiante(String carnet) {
        List<Curso> resultado = new ArrayList<>();

        Estudiante est = estudianteDAO.mapearEstudiante(
                db.getCollection("estudiantes")
                        .find(Filters.eq("carnet", carnet.toUpperCase())).first()
        );

        if (est != null) {
            MongoCollection<Document> colCur = db.getCollection("cursos");

            for (String cod : est.getCursosEnCurso()) {
                Curso c = cursoDAO.mapearCurso(
                        colCur.find(Filters.eq("codCurso", cod.toUpperCase())).first()
                );

                if (c != null) {
                    resultado.add(c);
                }
            }
        }

        return resultado;
    }
}
