package genericnode;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI client.
 */
public class RMIClient {

    String serverHost;
    String command;

    public RMIClient(String serverHost, String command) {
        this.serverHost = serverHost;
        this.command = command;
    }

    public void act() {
        try {
            /* gets the Registry, and gets the DataStoreRemote stub from the Registry */
            Registry registry = LocateRegistry.getRegistry(serverHost);
            DataStoreRemote stub = (DataStoreRemote) registry.lookup("genericnode.DataStoreRemote");

            /* invokes the method on the stub, and eventually invokes the corresponding method implemented in RMIServer
             * and gets the return value (server response) */
            String[] commands = command.split(" ");
            String response = "";
            switch (commands[0]) {
                case "exit":
                    try {
                        stub.stop();
                    } catch (Exception e) {
                        // does nothing because it is expected
                    } finally {
                        System.out.println("Server is shutdown.");
                    }
                    break;
                case "put":
                    response = stub.put(commands[1], commands[2]);
                    break;
                case "get":
                    response = stub.get(commands[1]);
                    break;
                case "del":
                    response = stub.del(commands[1]);
                    break;
                case "store":
                    response = stub.store();
                    break;
            }
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws Exception {
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "put foo 123");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "put bar 456");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "put foo 678");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "put foobar edfe");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "del foo");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "del nosuchkey");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "get foo");
        RMIClient client = new RMIClient("127.0.0.1", "exit");
//        genericnode.RMIClient client = new genericnode.RMIClient("127.0.0.1", "store");
        client.act();
    }
}
