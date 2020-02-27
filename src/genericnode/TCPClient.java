package genericnode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * TCP client.
 * Creates a Socket to connect to TCPServer, writes the command to server and reads response from server.
 *
 * @see TCPServer
 * @see TCPWorkerRunnable
 */
public class TCPClient {

    String serverHost;
    int serverPort;
    String command;

    public TCPClient(String serverHost, int serverPort, String command) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.command = command;
    }

    public void act() {
        try (Socket socket = new Socket(serverHost, serverPort)) {
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            /* writes the command to server*/
            toServer.writeBytes(command + '\n');
            toServer.flush();

            /* reads response from server */
            StringBuilder sb = new StringBuilder();
            String responseLine;
            while ((responseLine = fromServer.readLine()) != null) {
                sb.append(responseLine).append('\n');
            }
            String s = sb.toString();
            s = s.substring(0, s.length() - 1);     // remove trailing empty line

            System.out.println(s);

            /* closes the I/O streams */
            toServer.close();
            fromServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000,"put foo 123");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "put bar 456");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "put foo 678");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "put foobar edfe");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "del foo");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "del nosuchkey");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "get foo");
        TCPClient client = new TCPClient("127.0.0.1", 9000, "exit");
//        genericnode.TCPClient client = new genericnode.TCPClient("127.0.0.1", 9000, "store");
        client.act();
    }
}
