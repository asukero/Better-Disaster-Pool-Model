package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public void disconnect() throws SQLException {
        this.connection.close();
    }

    public Connection getConnection() {
        return this.connection;
    }

}
