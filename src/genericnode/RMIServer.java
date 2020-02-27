package genericnode;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * RMI server.
 * Implements the methods defined in DataStoreRemote.
 */
public class RMIServer implements DataStoreRemote {

    protected DataStore dataStore;

    Registry registry;

    public RMIServer() {
        this.dataStore = new DataStore();
    }

    @Override
    public String put(String key, String value) {
        return dataStore.put(key, value);
    }

    @Override
    public String get(String key) {
        return dataStore.get(key);
    }

    @Override
    public String del(String key) {
        return dataStore.del(key);
    }

    @Override
    public String store() {
        return dataStore.store();
    }

    @Override
    public void stop() {
        try {
            registry.unbind("genericnode.DataStoreRemote");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void start() {
        try {
            /* exports a remote object */
            DataStoreRemote stub = (DataStoreRemote) UnicastRemoteObject.exportObject(this, 0);

            /* binds the remote object's stub in the registry */
            registry = LocateRegistry.getRegistry();
            registry.bind("genericnode.DataStoreRemote", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RMIServer server = new RMIServer();
        server.start();
    }
}