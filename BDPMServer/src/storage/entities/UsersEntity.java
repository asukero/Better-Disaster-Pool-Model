package storage.entities;

import storage.StorageEngine;
import storage.database.entries.DevicesEntry;
import storage.database.entries.UsersEntry;
import storage.database.managers.UsersManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersEntity extends Entity<UsersManager, UsersEntry> {
    public UsersEntity(StorageEngine engine) throws SQLException {
        super(engine);
        setManager(new UsersManager(engine));
        setEntry(new UsersEntry());
    }

    public UsersEntity(StorageEngine engine, int id) throws SQLException {
        super(engine, id);
        setManager(new UsersManager(engine));
        setEntry(getManager().getEntry(id));
    }

    public UsersEntity(StorageEngine engine, UsersEntry entry) throws SQLException {
        super(engine, entry);
        setManager(new UsersManager(engine));
        setEntry(entry);
    }

    public int getId() {
        return getEntry().getId();
    }

    public String getNickName() {
        return getEntry().getNickName();
    }

    public void setNickName(String nickName) {
        getEntry().setNickName(nickName);
    }

    public String getHash() {
        return getEntry().getHash();
    }

    public void setHash(String hash) {
        getEntry().setHash(hash);
    }

    public List<DevicesEntity> getDevices() throws SQLException {
        List<DevicesEntity> devices = new ArrayList<>();
        for(DevicesEntry entry : getManager().getDevices(getId())){
            devices.add(new DevicesEntity(getEngine(), entry));
        }

        return devices;

    }

}
