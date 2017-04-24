package pool;

import threading.ClientRunnable;

import java.util.ArrayList;

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

    public ClientRunnable getHelperClient(){
        return clients.stream().filter(c -> c.getManager().canHelp()).findFirst().get();
    }
}
