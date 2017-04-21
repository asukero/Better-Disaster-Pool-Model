import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BDPMServer implements Runnable {
    private PrintWriter sortieWriter;
    private ServerSocket serverSocket;

    protected boolean isStopped = false;
    protected Thread runningThread = null;

    // Réponse à renvoyer au client
    private Object objectToSendBack = null;

    private int maxClientConnection;
    private int serverPort;

    public BDPMServer(int port, int max_client) throws IOException {
        maxClientConnection = max_client;
        serverPort = port;
    }

    @Override
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();

        System.out.println("Waiting for client connections...");
        while (!isStopped()) {
            Socket clientSocket = null;

            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }

            new Thread(new ClientRunnable(clientSocket)).start();
        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port", e);
        }
    }

    public static void main(String[] args) {

        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Veuillez indiquer 2 arguments");
            } else {
                BDPMServer server = new BDPMServer(new Integer(args[0]), new Integer(args[1]));

                System.out.println("Starting server...");
                new Thread(server).start();

                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        System.out.println("SERVEUR: interruption recue, fermeture du serveur...");
                        server.sortieWriter.close();
                        server.stop();
                    }
                });
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
            System.out.println("Usage:\n" +
                    "\tjava -jar BDPMServer.jar [port number] [max_clients_connection]\n" +
                    "\tex: java -jar ApplicationServer.jar 4242 10");
        }


    }
}
