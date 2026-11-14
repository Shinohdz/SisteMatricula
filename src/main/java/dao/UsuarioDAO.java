package dao;

/**
 *
 * @author Fran
 */



import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import conexion.ConexionMongo;
import modelo.Usuario;
import org.bson.Document;

public class UsuarioDAO {

    private final MongoDatabase db;

    public UsuarioDAO() {
        this.db = ConexionMongo.obtenerBaseDatos();
    }

    public Usuario autenticarUsuario(String username, String passwordPlano) {
        String hashBuscado = Usuario.generarHash(passwordPlano);

        MongoCollection<Document> colUsuarios = db.getCollection("usuarios");

        Document doc = colUsuarios.find(Filters.and(
                Filters.eq("username", username),
                Filters.eq("passwordHash", hashBuscado)
        )).first();

        if (doc != null) {
            return new Usuario(
                    doc.getString("username"),
                    doc.getString("passwordHash"),
                    doc.getString("nombreCompleto"),
                    doc.getString("rol")
            );
        }

        return null;
    }
}

