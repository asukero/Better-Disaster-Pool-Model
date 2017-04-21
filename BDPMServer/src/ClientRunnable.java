import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

/**

 */
public class ClientRunnable implements Runnable {

    protected Socket clientSocket = null;

    public ClientRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        Object clientObject;
        try {
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("new client connection");
            //récupération de la commande du client
            clientObject = inFromClient.readObject();
            /*Commande commande = (Commande) clientObject;
            traiteCommande(commande);

            //envoi d'une réponse au client
            if (objectToSendBack != null) {
                outToClient.writeObject(objectToSendBack);
                objectToSendBack = null;
            } else {
                outToClient.writeObject("Le serveur ne renvoie rien pour cette commande");
            }
            */
            //nettoyage et fermeture
            outToClient.flush();
            inFromClient.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
