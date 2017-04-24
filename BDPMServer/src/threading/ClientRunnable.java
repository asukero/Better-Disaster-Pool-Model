package threading;

import pool.ClientManager;
import pool.PoolManager;
import serializable.Message;
import serializable.MessageType;
import serializable.ReturnMessage;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

/**
    Runnable permettant de traiter les messages provenant du client. Recupère les messages entrants avec
 l'ObjectOuputStream puis transmet le message au ClientManager qui va se charger de traiter la demande du client
 */
public class ClientRunnable implements Runnable {

    protected SSLSocket clientSocket = null;
    private ClientManager manager;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private boolean toClose;

    public ClientRunnable(SSLSocket clientSocket) throws SQLException {
        this.clientSocket = clientSocket;
        this.manager = new ClientManager();

    }

    /**
     * initialise les entrées/sorties des flux d'objets et lis en continu ce qui arrive dans inFromClient tant que le
     * client n'a pas fermé la connexion.
     */
    public void run() {
        toClose = false;
        try {
            clientSocket.setEnabledCipherSuites(clientSocket.getSupportedCipherSuites());


            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            while (!toClose) {

                //récupération du message du client
                Object clientObject = inFromClient.readObject();
                Object objectToSendBack = null;

                if (clientObject instanceof Message) {
                    Message message = (Message) clientObject;
                    objectToSendBack = manager.processMessage(message);

                    if (message.getMessageType() == MessageType.DISCONNECT) {
                        toClose = true;
                    }
                }

                //envoi d'une réponse au client
                if (objectToSendBack != null) {
                    outToClient.writeObject(objectToSendBack);
                } else {
                    outToClient.writeObject("[!] Server returned nothing");
                }


                if (toClose) {
                    //nettoyage et fermeture
                    outToClient.flush();
                    inFromClient.close();
                    clientSocket.close();
                    PoolManager.getInstance().removeClient(this);
                }
            }
        } catch (Exception e) {
            System.out.println("[!] Error: " + e.toString());
        }
    }

    /**
     * méthode spécifique appelée par un ClientManager qui permet d'envoyer directement un message au client attaché à
     * ce Runnable.
     * @param message
     */
    public void sendHelpToClient(Message message){
        if(!toClose){
            try{
                outToClient.writeObject(message);
            }catch (IOException ex){
                System.out.println("[!] Error while sending help to client");
            }

        }
    }

    public ClientManager getManager() {
        return manager;
    }
}
