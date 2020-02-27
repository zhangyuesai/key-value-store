package genericnode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Main thread of UDP server.
 * Note that this class DOES NOT include business logic
 * (interaction with client, and key-value store, which are in UDPWorkerRunnable).
 * UDPServer merely receives DatagramPacket from UDPClient; one it receives one,
 * it creates a new thread (UDPWorkerRunnable) to process the client's request.
 *
 * @see UDPWorkerRunnable
 * @see UDPClient
 */
public class UDPServer implements Runnable {

    protected int serverPort;
    private DatagramSocket socket = null;

    protected boolean isStopped = false;
    /* dataStore is passed to all TCPWorkerRunnable threads, so all threads can share the same key-value store */
    protected DataStore dataStore;

    public UDPServer(int serverPort) {
        this.serverPort = serverPort;
        dataStore = new DataStore();
    }

    public void run() {
        openSocket();
        while (!isStopped()) {
            byte[] buf = new byte[65536];
            DatagramPacket fromClient = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(fromClient);
            } catch (IOException e) {
                /* after stop() (where DatagramSocket is closed and isStopped is set to true), when DatagramSocket still
                 * tries to receive a DatagramPacket, an Exception is caught and isStopped == true, so run() will return,
                 * i.e., this UDPServer Thread will end. */
                if (isStopped()) {
                    System.out.println("Server is shutdown.");
                    return;
                }
                throw new RuntimeException("Error receiving client datagram packet", e);
            }
            new Thread(new UDPWorkerRunnable(socket, fromClient, this.dataStore, this)).start();
        }
        System.out.println("Server is shutdown.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Sets isStopped to true and closes the DatagramSocket.
     * (After this, when run() tries to receive a DatagramPacket from the DatagramSocket,
     * an Exception is caught and isStopped == true, so run() will return.)
     */
    public synchronized void stop() {
        this.isStopped = true;
        this.socket.close();
    }

    private void openSocket() {
        try {
            socket = new DatagramSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer(9000);
        new Thread(server).start();
    }

}