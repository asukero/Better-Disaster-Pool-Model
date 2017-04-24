package storage.database.managers;

import storage.StorageEngine;
import storage.database.entries.Entry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Manager realise les requêtes SQL pour recupérer des informations de la BBD ou ajouter/supprimer des lignes.
 * 1 Manager par table
 * @param <E>
 */
public abstract class Manager<E extends Entry> {
    private StorageEngine engine;

    public Manager(StorageEngine engine) throws SQLException {
        this.engine = engine;
        createTable();
    }

    protected Connection getConnection() {
        return this.engine.getConnection();
    }

    protected abstract void createTable() throws SQLException;
    public abstract boolean addEntry(E entry) throws SQLException;
    public abstract List<E> getEntries() throws SQLException;
    public abstract E getEntry(int id) throws SQLException;
    public abstract boolean updateEntry(E entry) throws SQLException;
    public abstract boolean deleteEntry(E entry) throws SQLException;

    public void updateEntryId(E entry, Statement statement) throws SQLException {
        ResultSet lastInsert = statement.getGeneratedKeys();
        entry.setId(Integer.valueOf(lastInsert.getString("last_insert_rowid()")));
        lastInsert.close();
    }
}