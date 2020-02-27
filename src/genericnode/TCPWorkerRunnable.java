package genericnode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Business logic of the TCP server.
 * Reads a line of command from the client, parses the command, operates the DataStore accordingly, and writes the
 * response back to the client.
 *
 * @see TCPServer
 * @see TCPClient
 */
public class TCPWorkerRunnable implements Runnable {
    protected Socket clientSocket;
    protected DataStore dataStore;
    protected TCPServer server;


    public TCPWorkerRunnable(Socket clientSocket, DataStore dataStore, TCPServer server) {
        this.clientSocket = clientSocket;
        this.dataStore = dataStore;
        this.server = server;
    }

    public void run() {
        try {
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream());

            /* reads a line of command from the client. The command is assumed syntactically correct because the
             * commandline arguments has been checked by GenericNode */
            String commandLine = fromClient.readLine();
            /* if the command is "exit", close the streams, and stop the TCPServer */
            if (commandLine.equals("exit")) {
                toClient.writeBytes("Server is shutdown.");
                fromClient.close();
                toClient.close();
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
                /* writes the response back to the client, and closes the streams (but not TCPServer) */
                toClient.writeBytes(response);
                fromClient.close();
                toClient.close();
            }
        } catch (IOException e) {
            // report exception somewhere.
            e.printStackTrace();
        }
    }
}