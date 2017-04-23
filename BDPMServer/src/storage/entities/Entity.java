package storage.entities;

import storage.StorageEngine;
import storage.database.entries.Entry;
import storage.database.managers.Manager;

import java.sql.SQLException;

public abstract class Entity<M extends Manager, E extends Entry> {
    private StorageEngine engine;
    private M manager;
    private E entry;
    private boolean exists = false;

    public Entity(StorageEngine engine) throws SQLException {
        setEngine(engine);
    }

    public Entity(StorageEngine engine, int id) throws SQLException {
        setEngine(engine);
        setExists(true);
    }

    public Entity(StorageEngine engine, E entry) throws SQLException {
        setEngine(engine);
        setEntry(entry);
        setExists(true);
    }

    protected StorageEngine getEngine() {
        return engine;
    }

    private void setEngine(StorageEngine engine) {
        this.engine = engine;
    }

    protected M getManager() {
        return manager;
    }

    protected void setManager(M manager) {
        this.manager = manager;
    }

    protected E getEntry() {
        return entry;
    }

    protected void setEntry(E entry) {
        this.entry = entry;
    }

    public boolean isExists() {
        return exists;
    }

    private void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean save() throws SQLException {
        if (!isExists()) {
            setExists(true);

            return getManager().addEntry(getEntry());
        }
        else {
            return getManager().updateEntry(getEntry());
        }
    }

    public boolean delete() throws SQLException {
        if (!isExists()) {
            return false;
        }
        return getManager().deleteEntry(getEntry());
    }
}