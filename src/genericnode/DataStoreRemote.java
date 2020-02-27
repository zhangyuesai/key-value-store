package genericnode;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A remote interface for RMI.
 * Defines interfaces of put(), get(), del(), store(), and stop() methods.
 *
 * @see DataStore
 * @see RMIServer
 * @see RMIClient
 */
public interface DataStoreRemote extends Remote {
    String put(String key, String value) throws RemoteException;

    String get(String key) throws RemoteException;

    String del(String key) throws RemoteException;

    String store() throws RemoteException;

    void stop() throws RemoteException;
}
