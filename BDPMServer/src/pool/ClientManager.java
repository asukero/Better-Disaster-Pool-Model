package pool;

import com.google.common.hash.Hashing;
import serializable.Credentials;
import serializable.Message;
import serializable.MessageType;
import serializable.ReturnMessage;
import storage.StorageEngine;
import storage.entities.DevicesEntity;
import storage.entities.UsersEntity;
import storage.stores.DevicesStore;
import storage.stores.ServersStore;
import storage.stores.UsersStore;
import threading.ClientRunnable;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ClientManager {
    private final static StorageEngine engine = new StorageEngine();
    private UsersStore usersStore;
    private DevicesStore devicesStore;
    private ServersStore serversStore;

    private static final String salt = "398^fefe2~54kui#{2dzshjt#";
    private UsersEntity user;
    private DevicesEntity device;
    private boolean canHelp = false;
    private boolean loggedIn = false;

    public ClientManager() throws SQLException {
        engine.connect();
        usersStore = new UsersStore(engine);
        devicesStore = new DevicesStore(engine);
        serversStore = new ServersStore(engine);
        engine.disconnect();
    }

    public ReturnMessage processMessage(Message message) {
        try {
            switch (message.getMessageType()) {
                case CONNECT:
                    System.out.println("[*] A client is connected");
                    return new ReturnMessage(MessageType.CONNECT, "[>] Server connection was a success!", true);

                case DISCONNECT:
                    System.out.println("[*] A client wants to be disconnected");
                    return new ReturnMessage(MessageType.DISCONNECT, "[>] Server disconnection was a success!", true);

                case HELP:
                    if (!loggedIn) {
                        throw new Exception("user was not logged in");
                    }
                    System.out.println("[*] client " + user.getNickName() + ":" + message.getSenderIp().toString() + " needs help!");
                    requestHelp(message);
                    return new ReturnMessage(MessageType.HELP, "[>] The server accept that client " + user.getNickName() + ":" + message.getSenderIp().toString() + " needs help.", true);

                case HELPER:
                    if (!loggedIn) {
                        throw new Exception("user was not logged in");
                    }
                    if (!canHelp) {
                        System.out.println("[*] client " + user.getNickName() + ":" + message.getSenderIp().toString() + " is waiting to help");
                        canHelp = true;
                        return new ReturnMessage(MessageType.HELP, "[>] The server accept that client " + user.getNickName() + ":" + message.getSenderIp().toString() + " is waiting to help", true);
                    } else {
                        System.out.println("[*] client " + user.getNickName() + ":" + message.getSenderIp().toString() + " don't want to help anymore");
                        canHelp = false;
                        return new ReturnMessage(MessageType.HELP, "[>] The server accept that client " + user.getNickName() + ":" + message.getSenderIp().toString() + " is no longer waiting to help", true);
                    }

                case REGISTER:
                    System.out.println("[*] A client demand a registration");
                    if (message.getContent() instanceof Credentials) {
                        Credentials credentials = (Credentials) message.getContent();
                        if (registerNewUser(credentials)) {
                            loginUser(credentials);
                            return new ReturnMessage(MessageType.REGISTER, "[>] User: " + credentials.getNickName()
                                    + " is registered to the server.", true);
                        }
                    } else {
                        throw new ClassNotFoundException("Credentials doesn't exist");
                    }

                case LOGIN:
                    System.out.println("[*] A client wants to login");
                    if (message.getContent() instanceof Credentials) {
                        Credentials credentials = (Credentials) message.getContent();
                        loginUser(credentials);
                        if (loggedIn) {
                            registerNewDevice(message);
                            return new ReturnMessage(MessageType.LOGIN, "[>] User: " + credentials.getNickName()
                                    + " is logged in.", true);
                        } else {
                            return new ReturnMessage(MessageType.LOGIN, "[>] User: " + credentials.getNickName()
                                    + " is not logged in, wrong password", false);
                        }
                    } else {
                        throw new ClassNotFoundException("Credentials doesn't exist");
                    }

                default:
                    return new ReturnMessage(message.getMessageType(), "Unknown message type", false);
            }
        } catch (Exception ex) {
            String error = "[!] Error during " + message.getMessageType().toString() + ": " + ex.getMessage();
            System.out.println(error);
            engine.disconnect();
            return new ReturnMessage(message.getMessageType(), error, false);
        }
    }

    private boolean registerNewUser(Credentials credentials) throws SQLException {
        String fHash = Hashing.sha256().hashString(credentials.getPassword(), StandardCharsets.UTF_8).toString();
        String hash = Hashing.sha256().hashString(fHash + salt, StandardCharsets.UTF_8).toString();
        engine.connect();
        user = new UsersEntity(engine);
        user.setNickName(credentials.getNickName());
        user.setHash(hash);
        user.save();
        engine.disconnect();
        return true;
    }

    private void loginUser(Credentials credentials) throws SQLException {
        String fHash = Hashing.sha256().hashString(credentials.getPassword(), StandardCharsets.UTF_8).toString();
        String hash = Hashing.sha256().hashString(fHash + salt, StandardCharsets.UTF_8).toString();
        engine.connect();
        user = usersStore.getByName(credentials.getNickName());
        engine.disconnect();

        loggedIn = user.getHash().equals(hash);
    }

    private void logoffUser() {

    }

    private void registerNewDevice(Message message) {
        try {
            engine.connect();
            DevicesEntity devicesEntity = devicesStore.getByName(message.getSenderName());
            engine.disconnect();
        } catch (SQLException ex) {
            System.out.println("[*] No device found for client " + user.getNickName() + ":" + message.getSenderIp().toString() + ", adding a new one");
            try {
                device = new DevicesEntity(engine);
                device.setDeviceName(message.getSenderName());
                device.setIp(message.getSenderIp().toString());
                device.setUser(user);
                device.save();
            } catch (SQLException e) {
                System.out.println("[!] Error while registering new device for client " + user.getNickName() + ":" + message.getSenderIp().toString() + ".");
            }

        }
    }

    private void requestHelp(Message message) {
        try {
            ClientRunnable helperClient = PoolManager.getInstance().getHelperClient();
            if (helperClient.getManager() != this) {
                helperClient.sendHelpToClient(new Message(MessageType.HELP, "Help request received from " + user.getNickName() + ":" + message.getSenderIp().toString()));
                helperClient.getManager().canHelp = false;
            } else {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException ex) {
            System.out.println("[!] No helper are available in the pool.");
        }


    }

    public boolean canHelp() {
        return canHelp;
    }
}
