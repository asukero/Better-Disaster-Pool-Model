package storage.entities;

import storage.StorageEngine;
import storage.database.entries.DevicesEntry;
import storage.database.managers.DevicesManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DevicesEntity extends Entity<DevicesManager, DevicesEntry>{
    public DevicesEntity(StorageEngine engine) throws SQLException {
        super(engine);
        setManager(new DevicesManager(engine));
        setEntry(new DevicesEntry());
    }

    public DevicesEntity(StorageEngine engine, int id) throws SQLException {
        super(engine, id);
        setManager(new DevicesManager(engine));
        setEntry(getManager().getEntry(id));
    }

    public DevicesEntity(StorageEngine engine, DevicesEntry entry) throws SQLException {
        super(engine, entry);
        setManager(new DevicesManager(engine));
        setEntry(entry);
    }

    public int getId() {
        return getEntry().getId();
    }

    public String getDeviceName() {
        return getEntry().getDeviceName();
    }

    public void setDeviceName(String deviceName) {
        getEntry().setDeviceName(deviceName);
    }

    public String getIp() {
        return getEntry().getIp();
    }

    public void setIp(String ip) {
        getEntry().setIp(ip);
    }

    public UsersEntity getUser() throws SQLException {
        return new UsersEntity(getEngine(), getEntry().getUser());
    }

    public void setUser(UsersEntity user) { getEntry().setUser(user.getEntry().getId());}

}
