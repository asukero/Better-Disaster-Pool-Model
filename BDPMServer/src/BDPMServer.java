import pool.PoolManager;
import threading.ClientRunnable;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Application serveur multi-threading, permet d'intéragir avec plusieurs clients à la fois en utilisant des connexions
 * chiffrées SSL
 */
public class BDPMServer implements Runnable {
    private SSLServerSocket serverSocket;

    protected boolean isStopped = false;
    protected Thread runningThread = null;


    private int serverPort;

    public BDPMServer(int port, int max_client) throws SQLException {
        PoolManager.getInstance().init(max_client);
        serverPort = port;
    }

    /**
     * Ouverture du Socket SSL puis le serveur attend d'accepter des demandes de connexion provenant de clients.
     * Pour chaque nouveau client se connectant, un thread relié au socket client est créée.
     */
    @Override
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();

        System.out.println("[*] Waiting for client connections...");
        while (!isStopped()) {
            SSLSocket clientSocket = null;

            try {
                clientSocket = (SSLSocket) this.serverSocket.accept();
                ClientRunnable clientRunnable = new ClientRunnable(clientSocket);
                PoolManager.getInstance().addClient(clientRunnable);
                new Thread(clientRunnable).start();

            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("[*] Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "[!] Error accepting client connection", e);
            } catch (SQLException ex){
                System.out.println("[!] Error while creating client manager");
            }


        }
        System.out.println("[*] Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Interruption de la connexion
     */
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("[!] closing server", e);
        }
    }

    /**
     * Ouverture du Socket SSL, le serveur écoute sur le port indiqué par l'utilisateur en @param du programme
     */
    private void openServerSocket() {
        try {
            SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            this.serverSocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("[!] Cannot open port", e);
        }
    }

    public static void main(String[] args) {

        //lecture des arguments
        try {
            if (args.length != 4) {
                throw new IllegalArgumentException("[*] Please enter 4 arguments");
            } else {
                System.setProperty("javax.net.ssl.keyStore", args[0]);
                System.setProperty("javax.net.ssl.keyStorePassword", args[1]);
                BDPMServer server = new BDPMServer(new Integer(args[2]), new Integer(args[3]));

                System.out.println("[*] Starting server...");
                new Thread(server).start();

                //interruption de la connexion lorsque l'utilisateur veut fermer le programme
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        System.out.println("[!] : server interruption received, closing server...");
                        server.stop();
                    }
                });
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
            System.out.println("Usage:\n" +
                    "\tjava -jar BDPMServer.jar [jks cert path] [jks password] [port number] [max_clients_connection]\n" +
                    "\tex: java -jar ApplicationServer.jar 4242 10");
        }


    }
}
