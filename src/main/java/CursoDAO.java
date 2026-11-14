/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Fran 
 */


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import conexion.ConexionMongo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import modelo.Curso;
import org.bson.Document;

public class CursoDAO {

    private final MongoDatabase db;

    public CursoDAO() {
        this.db = ConexionMongo.obtenerBaseDatos();
    }

    public Curso mapearCurso(Document doc) {
        if (doc == null) {
            return null;
        }

        return new Curso(
                doc.getString("codCurso"),
                doc.getString("nombre"),
                doc.getString("descripcion"),
                doc.getString("tutor"),
                doc.getInteger("cupoMaximo", 30),
                doc.getInteger("matriculadosActivos", 0)
        );
    }

    public String registrarCurso(Curso cur) {
        String clave = cur.getCodCurso().toUpperCase();
        MongoCollection<Document> colCur = db.getCollection("cursos");

        if (colCur.find(Filters.eq("codCurso", clave)).first() != null) {
            return "Ya existe un curso registrado con el código: " + clave;
        }

        Document doc = new Document("codCurso", clave)
                .append("nombre", cur.getNombre())
                .append("descripcion", cur.getDescripcion())
                .append("tutor", cur.getTutor())
                .append("cupoMaximo", cur.getCupoMaximo())
                .append("matriculadosActivos", cur.getMatriculadosActivos());

        colCur.insertOne(doc);

        return "OK";
    }

    public Curso buscarCurso(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return null;
        }

        String busq = criterio.trim();
        MongoCollection<Document> colCur = db.getCollection("cursos");

        Document doc = colCur.find(Filters.or(
                Filters.eq("codCurso", busq.toUpperCase()),
                Filters.regex("nombre", Pattern.compile(
                        Pattern.quote(busq),
                        Pattern.CASE_INSENSITIVE
                ))
        )).first();

        return mapearCurso(doc);
    }

    public String modificarCurso(String codCurso, String nuevoNombre,
            String nuevaDesc, String nuevoTutor, int nuevoCupoMax) {

        String clave = codCurso.toUpperCase();
        MongoCollection<Document> colCur = db.getCollection("cursos");

        Curso cur = mapearCurso(
                colCur.find(Filters.eq("codCurso", clave)).first()
        );

        if (cur == null) {
            return "El curso que intenta modificar no existe.";
        }

        if (nuevoCupoMax < cur.getMatriculadosActivos()) {
            return "El nuevo cupo máximo (" + nuevoCupoMax
                    + ") no puede ser menor que los estudiantes ya matriculados ("
                    + cur.getMatriculadosActivos() + ").";
        }

        colCur.updateOne(
                Filters.eq("codCurso", clave),
                Updates.combine(
                        Updates.set("nombre", nuevoNombre),
                        Updates.set("descripcion", nuevaDesc),
                        Updates.set("tutor", nuevoTutor),
                        Updates.set("cupoMaximo", nuevoCupoMax)
                )
        );

        return "OK";
    }

    public String eliminarCurso(String codCurso) {
        String clave = codCurso.toUpperCase();
        MongoCollection<Document> colCur = db.getCollection("cursos");

        if (colCur.find(Filters.eq("codCurso", clave)).first() == null) {
            return "El curso seleccionado no existe.";
        }

        long matriculasAsociadas = db.getCollection("matriculas")
                .countDocuments(Filters.eq("codCurso", clave));

        if (matriculasAsociadas > 0) {
            return "No se permite eliminar el curso porque tiene matrículas registradas que deben conservar su referencia.";
        }

        colCur.deleteOne(Filters.eq("codCurso", clave));

        return "OK";
    }

    public List<Curso> obtenerCursosOrdenados(boolean porNombre) {
        List<Curso> lista = new ArrayList<>();

        for (Document doc : db.getCollection("cursos").find()) {
            lista.add(mapearCurso(doc));
        }

        if (porNombre) {
            lista.sort((c1, c2) ->
                    c1.getNombre().compareToIgnoreCase(c2.getNombre()));
        } else {
            lista.sort((c1, c2) ->
                    c1.getCodCurso().compareToIgnoreCase(c2.getCodCurso()));
        }

        return lista;
    }
}
