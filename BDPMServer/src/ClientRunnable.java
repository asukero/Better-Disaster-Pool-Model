import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**

 */
public class ClientRunnable implements Runnable {

    protected Socket clientSocket = null;

    public ClientRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        boolean toClose = false;
        try {
            ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());

            while (!toClose) {

                //récupération de la commande du client
                Object clientObject = inFromClient.readObject();

                String message = (String) clientObject;
                Object objectToSendBack = null;


                switch (message) {
                    case "CONNECT":
                        System.out.println("[*] A client is connected");
                        objectToSendBack = "[>] Connection was a success!";
                        break;
                    case "DISCONNECT":
                        System.out.println("[*] A client wants to be deconnected");
                        objectToSendBack = "[>] Deconnection was a success!";
                        toClose = true;
                        break;
                    case "HELP":
                        System.out.println("[*] A client need help!");
                        objectToSendBack = "[>] Requested help was a success!";
                        break;
                    default:
                        break;
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
