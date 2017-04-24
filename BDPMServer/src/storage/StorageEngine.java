package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Moteur permettant de se connecter/deconnecter pour faire des requêtes à la BDD
 */
public class StorageEngine {
    private String path;
    private Connection connection;

    public StorageEngine(String path) {
        this.path = path;
    }

    public StorageEngine() {
        this("db/bdpm.db");
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
    }

    public void disconnect() {
        try{
            this.connection.close();
        } catch (SQLException ex){
            System.out.println("[!] Error while closing engine.");
        }

    }

    public Connection getConnection() {
        return this.connection;
    }

}
