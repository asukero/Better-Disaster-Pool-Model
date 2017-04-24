package pool;

import threading.ClientRunnable;

import java.util.ArrayList;

/**
 * Singleton permettant la gestion des différents clients connectés au serveur à l'aide de ClientRunnable
 * Definit le nombre maximum de connections clientes autorisées par le server.
 * Chaque nouvelle connexion est sockée dans la liste client.
 * Cette liste aussi permet de récuperer les connexions clientes qui ont indiqué au serveur que leur client pouvait
 * recevoir une demande d'aide.
 */
public class PoolManager {
    private static PoolManager instance = new PoolManager();

    public static PoolManager getInstance() {
        return instance;
    }

    private int maxConnections;

    private ArrayList<ClientRunnable> clients = new ArrayList<>();

    private PoolManager() {
    }
    public void init(int maxConnections){
        this.maxConnections = maxConnections;
    }

    public void addClient(ClientRunnable clientRunnable){
        clients.add(clientRunnable);
    }

    public void removeClient(ClientRunnable clientRunnable){
        clients.remove(clientRunnable);
    }

    public ClientRunnable getClient(int i){
        return clients.get(i);
    }

    /**
     * @return la première connexion cliente qui a indiqué au serveur que son client pouvait recevoir une demande d'aide
     */
    public ClientRunnable getHelperClient(){
        return clients.stream().filter(c -> c.getManager().canHelp()).findFirst().get();
    }
}
