package genericnode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Business logic of the UDP server.
 * Gets the client command from the DatagramPacket received from UDPServer, parses the command, operates the DataStore
 * accordingly, and sends back to the client a DatagramPacket containing the response.
 *
 * @see UDPServer
 * @see UDPClient
 */
public class UDPWorkerRunnable implements Runnable {
    protected DatagramSocket socket;
    protected DatagramPacket fromClient;
    protected DataStore dataStore;
    protected UDPServer server;


    public UDPWorkerRunnable(DatagramSocket socket, DatagramPacket fromClient, DataStore dataStore, UDPServer server) {
        this.socket = socket;
        this.fromClient = fromClient;
        this.dataStore = dataStore;
        this.server = server;
    }

    public void run() {
        try {
            InetAddress clientAddress = fromClient.getAddress();
            int clientPort = fromClient.getPort();

            /* gets a line of command from the client. The command is assumed syntactically correct because the
             * commandline arguments has been checked by GenericNode */
            String commandLine = new String(fromClient.getData(), 0, fromClient.getLength());
            /* if the command is "exit", stop the UDPServer */
            if (commandLine.equals("exit")) {
                String response = "Server is shutdown.";
                byte[] buf = response.getBytes();
                DatagramPacket toClient = new DatagramPacket(buf, buf.length, clientAddress, clientPort);
                socket.send(toClient);
                server.stop();
            } else {
                /* operates the DataStore accordingly */
                String[] commands = commandLine.split(" ");
                String response = "";
                switch (commands[0]) {
                    case "put":
                        response = dataStore.put(commands[1], commands[2]);
                        break;
                    case "get":
                        response = dataStore.get(commands[1]);
                        break;
                    case "del":
                        response = dataStore.del(commands[1]);
                        break;
                    case "store":
                        response = dataStore.store();
                        break;
                }
                /* sends back to the client a DatagramPacket containing the response */
                byte[] buf = response.getBytes();
                DatagramPacket toClient = new DatagramPacket(buf, buf.length, clientAddress, clientPort);
                socket.send(toClient);
            }
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}