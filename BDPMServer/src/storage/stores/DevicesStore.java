package storage.stores;

import storage.StorageEngine;
import storage.database.entries.DevicesEntry;
import storage.database.managers.DevicesManager;
import storage.entities.DevicesEntity;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DevicesStore {
    private StorageEngine engine;
    private DevicesManager manager;

    public DevicesStore(StorageEngine engine) throws SQLException {
        this.engine = engine;
        this.manager = new DevicesManager(engine);
    }

    public DevicesEntity getById(int id) throws SQLException {
        return new DevicesEntity(this.engine, this.manager.getEntry(id));
    }

    public List<DevicesEntity> getAll() throws SQLException {
        List<DevicesEntity> result = new LinkedList<>();
        for(DevicesEntry entry: this.manager.getEntries()) {
            result.add(new DevicesEntity(this.engine, entry));
        }
        return result;
    }
}