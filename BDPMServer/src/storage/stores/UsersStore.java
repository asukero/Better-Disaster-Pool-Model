package storage.stores;

import storage.StorageEngine;
import storage.database.entries.UsersEntry;
import storage.database.managers.UsersManager;
import storage.entities.UsersEntity;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UsersStore {
    private StorageEngine engine;
    private UsersManager manager;

    public UsersStore(StorageEngine engine) throws SQLException {
        this.engine = engine;
        this.manager = new UsersManager(engine);
    }

    public UsersEntity getById(int id) throws SQLException {
        return new UsersEntity(this.engine, this.manager.getEntry(id));
    }

    public UsersEntity getByName(String name) throws SQLException {
        return new UsersEntity(this.engine, this.manager.getEntry(name));
    }

    public List<UsersEntity> getAll() throws SQLException {
        List<UsersEntity> result = new LinkedList<>();
        for(UsersEntry entry: this.manager.getEntries()) {
            result.add(new UsersEntity(this.engine, entry));
        }
        return result;
    }
}
