package storage.stores;

import storage.StorageEngine;
import storage.database.entries.ServersEntry;
import storage.database.managers.ServersManager;
import storage.entities.ServersEntity;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ServersStore {
    private StorageEngine engine;
    private ServersManager manager;

    public ServersStore(StorageEngine engine) throws SQLException {
        this.engine = engine;
        this.manager = new ServersManager(engine);
    }

    public ServersEntity getById(int id) throws SQLException {
        return new ServersEntity(this.engine, this.manager.getEntry(id));
    }

    public List<ServersEntity> getAll() throws SQLException {
        List<ServersEntity> result = new LinkedList<>();
        for(ServersEntry entry: this.manager.getEntries()) {
            result.add(new ServersEntity(this.engine, entry));
        }
        return result;
    }
}
