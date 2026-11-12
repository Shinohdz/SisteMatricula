package conexion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ConexionMongo {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "sistemagestionacademica_db";
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static MongoDatabase obtenerBaseDatos() {
        if (database == null) {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(DB_NAME);
        }
        return database;
    }

    public static boolean probarConexion() {
        try {
            MongoDatabase db = obtenerBaseDatos();
            db.runCommand(new Document("ping", 1));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}