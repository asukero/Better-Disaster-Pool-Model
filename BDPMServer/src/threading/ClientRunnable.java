package threading;

import pool.PoolManager;
import serializable.Message;
import serializable.MessageType;

import javax.net.ssl.SSLSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**

 */
public class ClientRunnable implements Runnable {

    protected SSLSocket clientSocket = null;
    private PoolManager manager;

    public ClientRunnable(SSLSocket clientSocket, PoolManager manager) {
        this.clientSocket = clientSocket;
        this.manager = manager;
    }

    public void run() {
        boolean toClose = false;
        try {
            clientSocket.setEnabledCipherSuites(clientSocket.getSupportedCipherSuites());


            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());

            while (!toClose) {

                //récupération de la commande du client
                Object clientObject = inFromClient.readObject();
                Object objectToSendBack = null;

                if (clientObject instanceof Message) {
                    Message message = (Message) clientObject;
                    objectToSendBack = manager.processMessage(message);

                    if(message.getMessageType() == MessageType.DISCONNECT){
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
                }
            }
        } catch (Exception e) {
            System.out.println("[!] Error: " + e.toString());
        }
    }
}
