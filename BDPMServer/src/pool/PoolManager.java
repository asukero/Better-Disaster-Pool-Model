package pool;

import com.google.common.hash.Hashing;
import serializable.Credentials;
import serializable.Message;
import storage.StorageEngine;
import storage.entities.UsersEntity;
import storage.stores.DevicesStore;
import storage.stores.ServersStore;
import storage.stores.UsersStore;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

public class PoolManager {
    private StorageEngine engine = new StorageEngine();
    private UsersStore usersStore;
    private DevicesStore devicesStore;
    private ServersStore serversStore;

    private static final String salt = "398^fefe2~54kui#{2dzshjt#";

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

    public Object processMessage(Message message) {
        try {
            switch (message.getMessageType()) {
                case CONNECT:
                    System.out.println("[*] A client is connected");
                    /*if (message.getContent() instanceof Credentials) {
                        Credentials credentials = (Credentials) message.getContent();
                        return loginUser(credentials);
                    } else {
                        throw new ClassNotFoundException("Credentials doesn't exist");
                    }*/
                    return "[>] Connection was a success!";

                case DISCONNECT:
                    System.out.println("[*] A client wants to be disconnected");
                    return "[>] Disconnection was a success!";

                case HELP:
                    System.out.println("[*] A client need help!");
                    return "[>] Requested help was a success!";

                case REGISTER:
                    System.out.println("[*] A client demand a registration");
                    if (message.getContent() instanceof Credentials) {
                        Credentials credentials = (Credentials) message.getContent();
                        return registerNewUser(credentials);
                    } else {
                        throw new ClassNotFoundException("Credentials doesn't exist");
                    }

                default:
                    return new Exception("Unknown Message Type");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            String error = "[!] Error during " + message.getMessageType().toString() + ": " + ex.getMessage();
            System.out.println(error);
            return message;
        }
    }

    public Object registerNewUser(Credentials credentials) throws SQLException {
        String fHash = Hashing.sha256().hashString(credentials.getPassword(), StandardCharsets.UTF_8).toString();
        String hash = Hashing.sha256().hashString(fHash + salt, StandardCharsets.UTF_8).toString();
        engine.connect();
        UsersEntity newUser = new UsersEntity(engine);
        newUser.setNickName(credentials.getNickName());
        newUser.setHash(hash);
        newUser.save();
        engine.disconnect();
        loginUser(credentials);
        return "[>] User: " + credentials.getNickName() + " is registered to the server.";
    }

    public Object loginUser(Credentials credentials) {
        return null;
    }

    public void logoffUser() {

    }
}
