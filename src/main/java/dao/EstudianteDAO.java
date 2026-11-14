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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import modelo.Estudiante;
import org.bson.Document;

public class EstudianteDAO {

    private final MongoDatabase db;

    public EstudianteDAO() {
        this.db = ConexionMongo.obtenerBaseDatos();
    }

    public Estudiante mapearEstudiante(Document doc) {
        if (doc == null) {
            return null;
        }

        Estudiante est = new Estudiante(
                doc.getString("carnet"),
                doc.getString("nombre"),
                doc.getString("documento"),
                doc.getString("correo"),
                doc.getBoolean("activo", true)
        );

        List<String> listaCursos = doc.getList("cursosEnCurso", String.class);

        if (listaCursos != null) {
            est.setCursosEnCurso(new HashSet<>(listaCursos));
        }

        return est;
    }

    public String registrarEstudiante(Estudiante est) {
        String clave = est.getCarnet().toUpperCase();
        MongoCollection<Document> colEst = db.getCollection("estudiantes");

        if (colEst.find(Filters.eq("carnet", clave)).first() != null) {
            return "Ya existe un estudiante registrado con el carnet: " + clave;
        }

        if (colEst.find(Filters.eq("documento", est.getDocumento())).first() != null) {
            return "El documento de identificación ya pertenece a otro estudiante.";
        }

        Document doc = new Document("carnet", clave)
                .append("nombre", est.getNombre())
                .append("documento", est.getDocumento())
                .append("correo", est.getCorreo())
                .append("activo", est.isActivo())
                .append("cursosEnCurso", new ArrayList<>(est.getCursosEnCurso()));

        colEst.insertOne(doc);

        return "OK";
    }

    public Estudiante buscarEstudiante(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return null;
        }

        String busq = criterio.trim();
        MongoCollection<Document> colEst = db.getCollection("estudiantes");

        Document doc = colEst.find(Filters.or(
                Filters.eq("carnet", busq.toUpperCase()),
                Filters.eq("documento", busq),
                Filters.regex("nombre", Pattern.compile(
                        Pattern.quote(busq),
                        Pattern.CASE_INSENSITIVE
                ))
        )).first();

        return mapearEstudiante(doc);
    }

    public String modificarEstudiante(String carnetOriginal, String nuevoNombre,
            String nuevoDoc, String nuevoCorreo, boolean nuevoEstado) {

        String clave = carnetOriginal.toUpperCase();
        MongoCollection<Document> colEst = db.getCollection("estudiantes");

        if (colEst.find(Filters.eq("carnet", clave)).first() == null) {
            return "El estudiante que intenta modificar no existe.";
        }

        Document otroConMismoDoc = colEst.find(Filters.and(
                Filters.ne("carnet", clave),
                Filters.eq("documento", nuevoDoc)
        )).first();

        if (otroConMismoDoc != null) {
            return "El documento ingresado ya está registrado en otro estudiante.";
        }

        colEst.updateOne(
                Filters.eq("carnet", clave),
                Updates.combine(
                        Updates.set("nombre", nuevoNombre),
                        Updates.set("documento", nuevoDoc),
                        Updates.set("correo", nuevoCorreo),
                        Updates.set("activo", nuevoEstado)
                )
        );

        return "OK";
    }

    public String eliminarEstudiante(String carnet) {
        String clave = carnet.toUpperCase();
        MongoCollection<Document> colEst = db.getCollection("estudiantes");

        if (colEst.find(Filters.eq("carnet", clave)).first() == null) {
            return "El estudiante seleccionado no existe.";
        }

        long matriculasAsociadas = db.getCollection("matriculas")
                .countDocuments(Filters.eq("carnetEstudiante", clave));

        if (matriculasAsociadas > 0) {
            return "No se puede eliminar el estudiante porque posee matrículas asociadas en el historial. Puede cambiar su estado a Inactivo.";
        }

        colEst.deleteOne(Filters.eq("carnet", clave));

        return "OK";
    }

    private List<Estudiante> consultarTodos() {
        List<Estudiante> lista = new ArrayList<>();

        for (Document doc : db.getCollection("estudiantes").find()) {
            lista.add(mapearEstudiante(doc));
        }

        return lista;
    }

    public List<Estudiante> obtenerOrdenadosPorCarnet() {
        List<Estudiante> lista = consultarTodos();
        Collections.sort(lista);
        return lista;
    }

    public List<Estudiante> obtenerOrdenadosPorNombre() {
        List<Estudiante> lista = consultarTodos();
        Collections.sort(lista, Estudiante.POR_NOMBRE);
        return lista;
    }
}

