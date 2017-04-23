package storage.entities;

import storage.StorageEngine;
import storage.database.entries.ServersEntry;
import storage.database.managers.ServersManager;

import java.sql.SQLException;

public class ServersEntity extends Entity<ServersManager, ServersEntry> {
    public ServersEntity(StorageEngine engine) throws SQLException {
        super(engine);
        setManager(new ServersManager(engine));
        setEntry(new ServersEntry());
    }

    public ServersEntity(StorageEngine engine, int id) throws SQLException {
        super(engine, id);
        setManager(new ServersManager(engine));
        setEntry(getManager().getEntry(id));
    }

    public ServersEntity(StorageEngine engine, ServersEntry entry) throws SQLException {
        super(engine, entry);
        setManager(new ServersManager(engine));
        setEntry(entry);
    }

    public int getId() {
        return getEntry().getId();
    }

    public String getName() {
        return getEntry().getName();
    }

    public void setName(String name) {
        getEntry().setName(name);
    }

    public String getIp() {
        return getEntry().getIp();
    }

    public void setIp(String ip) {
        getEntry().setIp(ip);
    }

}