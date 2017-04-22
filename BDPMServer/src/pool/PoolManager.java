package pool;

import storage.StorageEngine;
import storage.database.entries.ServersEntry;
import storage.stores.DevicesStore;
import storage.stores.ServersStore;
import storage.stores.UsersStore;

import java.sql.SQLException;
import java.util.ArrayList;

public class PoolManager {
    private StorageEngine engine = new StorageEngine();
    private UsersStore usersStore;
    private DevicesStore devicesStore;
    private ServersStore serversStore;

    private int maxConnections;

    private ArrayList<User> users = new ArrayList<>();

    public PoolManager(int maxConnections) throws SQLException {
        this.maxConnections = maxConnections;
        engine.connect();
        usersStore = new UsersStore(engine);
        devicesStore = new DevicesStore(engine);
        serversStore = new ServersStore(engine);
        engine.disconnect();
    }

    public void registerUsers(){

    }

    public void loginUser(){

    }

    public void logoffUser(){

    }
}
