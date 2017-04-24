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

/**
 * Principale classe s'occupant de la gestion des messages reçus par un ClientRunnable
 * Cette classe lit les message fournit pour les client puis effectue les tâches demandées par le client
 * Dispose d'une connexion à la BDD pour l'authentification/inscription/connexion des utilisateurs
 */
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
        //initialisation des Stores pour effecter des requêtes groupées à la BDD.
        engine.connect();
        usersStore = new UsersStore(engine);
        devicesStore = new DevicesStore(engine);
        serversStore = new ServersStore(engine);
        engine.disconnect();
    }

    /**
     * Traite les message clients reçus
     * @param message
     * @return
     */
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

    /**
     * Enregistrement d'un nouvel utilisateur dans la BDD
     * @param credentials
     * @return
     * @throws SQLException
     */
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

    /**
     * Connexion d'un utilisateur. (La deconnexion s'effectue simplement en fermant le socket)
     * @param credentials
     * @throws SQLException
     */
    private void loginUser(Credentials credentials) throws SQLException {
        String fHash = Hashing.sha256().hashString(credentials.getPassword(), StandardCharsets.UTF_8).toString();
        String hash = Hashing.sha256().hashString(fHash + salt, StandardCharsets.UTF_8).toString();
        engine.connect();
        user = usersStore.getByName(credentials.getNickName());
        engine.disconnect();

        loggedIn = user.getHash().equals(hash);
    }


    /**
     * Enregistre un nouveau device dans la BDD
     * @param message
     */
    private void registerNewDevice(Message message) {
        try {
            engine.connect();
            device = devicesStore.getByName(message.getSenderName());
            engine.disconnect();
        } catch (SQLException ex) {
            System.out.println("[*] No device found for client " + user.getNickName() + ":" + message.getSenderIp().toString() + ", adding a new one");
            try {
                device = new DevicesEntity(engine);
                device.setDeviceName(message.getSenderName());
                device.setIp(message.getSenderIp().getHostAddress());
                device.setUser(user);
                device.save();
                engine.disconnect();
            } catch (SQLException e) {
                System.out.println("[!] Error while registering new device for client " + user.getNickName() + ":" + message.getSenderIp().toString() + ".");
            }

        }
    }

    /**
     * Traitement de la demande d'aide du client, recupère dans le PoolManager une connexion cliente s'étant déclarée
     * comme pouvant recevoir de l'aide puis cette demande d'aide à cette connexion cliente.
     * @param message
     */
    private void requestHelp(Message message) {
        try {
            ClientRunnable helperClient = PoolManager.getInstance().getHelperClient();
            //éviter que le demandeur d'aide soit celui choisit pour recevoir l'aide!
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
