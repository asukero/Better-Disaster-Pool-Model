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

    public void run() {
        toClose = false;
        try {
            clientSocket.setEnabledCipherSuites(clientSocket.getSupportedCipherSuites());


            outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            while (!toClose) {

                //récupération de la commande du client
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
