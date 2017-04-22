package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientModel {

    private InetAddress serverHostName;
    private int portNumber;
    private Socket clientSocket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    boolean initialized = false;

    public void initialize(InetAddress inetAddress, int port) {
        serverHostName = inetAddress;
        portNumber = port;
        initialized = true;
        connectToServer();
    }

    public void connectToServer() {
        try {
            System.out.println("[*] Connecting to " + serverHostName.toString() + ":" + portNumber + "...");
            clientSocket = new Socket(serverHostName, portNumber);
            clientSocket.setKeepAlive(true);

            outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            System.out.println("[!] Error couldn't establish a connection with " + serverHostName.toString() + ":" + portNumber);
        }
    }

    public void sendToServer(Object objectToSend) {
        Object response;
        try {
            //Ouverture des flux d'entrée et de sortie du socket

            //envoi de l'objet Commande serialisé
            outToServer.writeObject(objectToSend);

            //Objet réponse du serveur
            response = inFromServer.readObject();
            System.out.println(response.toString());


        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("[!] Error connection was lost: " + ex.getMessage());
        }
    }

    public void closeConnection() {
        try {
            // nettoyage du flux de sortie et fermeture du socket lors que le serveur a répondu.
            outToServer.flush();
            outToServer.close();
            inFromServer.close();
            clientSocket.close();
            initialized = false;
        } catch (IOException ex) {
            System.out.println("[!] Error while closing the socket");
        }
    }

    public boolean isConnected() {
        if (clientSocket != null) {
            return clientSocket.isConnected();
        }
        return false;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
