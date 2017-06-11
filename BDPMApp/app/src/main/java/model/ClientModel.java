package model;

import android.content.Context;

import asynctasks.ConnectTask;
import asynctasks.DisconnectTask;
import asynctasks.HelpTask;
import asynctasks.HelperTask;
import asynctasks.LoginTask;
import asynctasks.RegisterTask;
import serializable.Message;
import serializable.MessageType;
import serializable.ReturnMessage;

import javax.net.ssl.SSLSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

/**
 * Logique interne de l'application cliente, réalise tous les appels vers le serveur
 * Une fois la connexion SSL établie, le cient peut envoyer à n'importe quel moment des objets Message vers le serveur.
 * Le socket reste ouvert tant que l'utilisateur ne se déconnecte pas.
 * */
public class ClientModel  {

    public InetAddress serverHostName;
    public int portNumber;
    public SSLSocket clientSocket;
    public ObjectOutputStream outToServer;
    public ObjectInputStream inFromServer;
    public Thread waitingToHelpThread = new Thread();
    public boolean initialized = false;
    public boolean authenticated = false;
    public Context context;
    public MessageType messageType=null;

    public void initialize(InetAddress inetAddress, int port,Context context) {
        serverHostName = inetAddress;
        portNumber = port;
        initialized = true;
        this.context=context;
    }
    public void connect(){
        ConnectTask task = new ConnectTask();
        task.model=this;
        task.execute();
    }

    public void disconnect(){
        DisconnectTask task = new DisconnectTask();
        task.model=this;
        task.execute();
    }
    public void login(String login, String pass){
        LoginTask task = new LoginTask(login,pass);
        task.model=this;
        task.execute();
    }

    public void register(String login, String pass){
        RegisterTask task = new RegisterTask(login,pass);
        task.model=this;
        task.execute();
    }
    public void help(){
        HelpTask task = new HelpTask();
        task.model=this;
        task.execute();
    }
    public void helper(){
        HelperTask task = new HelperTask();
        task.model=this;
        task.execute();
    }


    /**
     * sendToServer() est appellée une fois la connexion établie pour envoyer des message au serveur.
     * @param objectToSend
     * @param messageType
     */
    public void sendToServer(Object objectToSend, MessageType messageType) {
        clientSocket.setEnabledCipherSuites(clientSocket.getSupportedCipherSuites());
        try {
            Message message = new Message(messageType, objectToSend);
            outToServer.writeObject(message);

            //Objet réponse du serveur
            Object response = inFromServer.readObject();
            if (response instanceof ReturnMessage) {
                ReturnMessage rMessage = (ReturnMessage) response;
                processReturnMessage(rMessage);
            } else if (response instanceof Message) {
                Message newMessage = (Message) response;
                processNewMessage(newMessage);
            }

        } catch (Exception ex) {
            System.out.println("[!] Error sending message failed: " + ex.getMessage());
        }
    }

    /**
     * Cette méthode est appellée lorsque le client se signale comme disponible pour recevoir une demande d'aide d'un
     * autre client. Un thread est alors lancé qui attend un message du serveur correspond à la demande d'un autre
     * client qui a transité par le serveur.
     */
    public void waitingToHelp(){
        if(waitingToHelpThread.isAlive()){
            waitingToHelpThread.interrupt();
        } else{
            waitingToHelpThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean helpReceived = false;
                    System.out.println("[*] Waiting to help...");
                    try{
                        while (!helpReceived){
                            Object response = inFromServer.readObject();
                            if (response instanceof Message) {
                                Message newMessage = (Message) response;
                                processNewMessage(newMessage);
                                helpReceived = true;
                            }
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        System.out.println("[!] Error while waiting for help: " + ex.getMessage());
                    }

                }
            });
            waitingToHelpThread.start();
        }

    }

    /**
     * fermeture de le connexion en cas de deconnexion demandée par l'utilisateur
     */
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

    /**
     * méthode qui traite le message de retour envoyé par le serveur.
     * @param rMessage
     */
    private void processReturnMessage(ReturnMessage rMessage) {
        if (rMessage.isSuccess()) {
            System.out.println("[*] Success!");
        } else {
            System.out.println("[!] Error!");
        }
        System.out.println(rMessage.getContent().toString());
        switch (rMessage.getMessageType()) {
            case CONNECT:
                break;
            case DISCONNECT:
                authenticated = false;
                break;
            case LOGIN:
                if (rMessage.isSuccess()) {
                    authenticated = true;
                }
                break;
            case REGISTER:
                if (rMessage.isSuccess()) {
                    authenticated = true;
                }
                break;
            case HELP:
                break;
            case HELPER:
                break;
            case ERROR:
                break;
            default:
                break;
        }
    }

    /**
     * méthode qui traite les messages provenant du serveur reçu par le client mais qui ne sont pas des messages de
     * réponse du serveur suite à l'envoi d'un message de ce client. (ex : une demande d'aide d'un autre client recu par
     * ce client)
     * @param message
     */
    private void processNewMessage(Message message) {
        switch (message.getMessageType()) {
            case CONNECT:
                break;
            case DISCONNECT:
                authenticated = false;
                break;
            case LOGIN:
                break;
            case HELP:
                System.out.println(message.getContent().toString());
                break;
            case HELPER:
                break;
            case REGISTER:
                break;
            case ERROR:
                break;
            default:
                break;
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

    public boolean isAuthenticated() {
        return authenticated;
    }

}
