package genericnode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main thread of TCP server.
 * Note that this class DOES NOT include business logic
 * (interaction with client, and key-value store, which are in TCPWorkerRunnable).
 * TCPServer merely listens to Socket from TCPClient; once there is a new client,
 * it creates a new thread (TCPWorkerRunnable) to process the client's request.
 *
 * @see TCPWorkerRunnable
 * @see TCPClient
 */
public class TCPServer implements Runnable {

    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    /* dataStore is passed to all TCPWorkerRunnable threads, so all threads can share the same key-value store */
    protected DataStore dataStore;

    public TCPServer(int serverPort) {
        this.serverPort = serverPort;
        dataStore = new DataStore();
    }

    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                /* after stop() (where serverSocket is closed and isStopped is set to true), when serverSocket still
                 * tries to accept a Socket, an Exception is caught and isStopped == true, so run() will return,
                 * i.e., this TCPServer Thread will end. */
                if (isStopped()) {
                    System.out.println("Server is shutdown.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new TCPWorkerRunnable(clientSocket, this.dataStore, this)).start();
        }
        System.out.println("Server is shutdown.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Sets isStopped to true and closes the ServerSocket.
     * (After this, when run() tries to accept a Socket from the ServerSocket,
     * an Exception is caught and isStopped == true, so run() will return.)
     */
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

    public static void main(String[] args) {
        TCPServer server = new TCPServer(9000);
        new Thread(server).start();
    }

}