package genericnode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * UDP client.
 * Sends to server a DatagramPacket containing the command,
 * and reads from server a DatagramPacket containing the response
 *
 * @see UDPServer
 * @see UDPWorkerRunnable
 */
public class UDPClient {

    InetAddress serverAddress;
    int serverPort;
    String command;


    public UDPClient(String serverHost, int serverPort, String command) {
        try {
            this.serverAddress = InetAddress.getByName(serverHost);
            this.serverPort = serverPort;
            this.command = command;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void act() {
        try (DatagramSocket socket = new DatagramSocket()) {
            /* sends to server a DatagramPacket containing the command */
            byte[] buf = command.getBytes();
            DatagramPacket toServer = new DatagramPacket(buf, buf.length, serverAddress, serverPort);
            socket.send(toServer);

            /* reads from server a DatagramPacket containing the response */
            buf = new byte[65536];
            DatagramPacket fromServer = new DatagramPacket(buf, buf.length);
            socket.receive(fromServer);
            String response = new String(fromServer.getData(), 0, fromServer.getLength());

            System.out.println(response);

            /* there is no connection in UDP and therefore no I/O streams */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "put foo 123");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "put bar 456");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "put foo 678");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "put foobar edfe");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "del foo");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "del nosuchkey");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "get foo");
        UDPClient client = new UDPClient("127.0.0.1", 9000, "exit");
//        genericnode.UDPClient client = new genericnode.UDPClient("127.0.0.1", 9000, "store");
        client.act();
    }
}
