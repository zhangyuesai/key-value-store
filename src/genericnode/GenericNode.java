package genericnode;

import java.util.ArrayList;

/**
 * A single entry point for TCP/UDP/RMI server/client.
 */
public class GenericNode {

    /**
     * Parses the whole command line.
     * @param args the command line from main()
     * @return a String array for parsed command line. If syntax is incorrect, returns null.
     */
    public static ArrayList<String> parseCommandlineArgs(String[] args) {
        ArrayList<String> commandLine = new ArrayList<>();

        if (args.length == 0) {
            commandLine = null;
        } else if (args.length == 2 && args[0].equals("ts")) {
            commandLine.add(args[0]);
            commandLine.add(args[1]);
        } else if (args.length == 2 && args[0].equals("us")) {
            commandLine.add(args[0]);
            commandLine.add(args[1]);
        } else if (args.length == 1 && args[0].equals("rmis")) {
            commandLine.add(args[0]);
        } else if (args.length > 3 && args[0].equals("tc")) {
            commandLine = parseClientCommandLine(args, 3);
        } else if (args.length > 3 && args[0].equals("uc")) {
            commandLine = parseClientCommandLine(args, 3);
        } else if (args.length > 2 && args[0].equals("rmic")) {
            commandLine = parseClientCommandLine(args, 2);
        } else {
            commandLine = null;
        }

        return commandLine;
    }

    /**
     * Parses the whole command line for client.
     * @param args the command line from main(), args[0] is "tc", "uc" or "rmic"
     * @param commandStartingIndex for args[0] == "tc" or "uc", commandStartingIndex == 3,
     *                             for args[0] == "rmic,        commandStartingIndex == 2
     * @return a String array for the parsed command line.
     *      The last element is a space-separated String for the "real" command for client
     *      (i.e., begins with "get", "put", "del", "store" or "exit").
     *      Returns null if the "real" command's syntax is incorrect.
     */
    private static ArrayList<String> parseClientCommandLine(String[] args, int commandStartingIndex) {
        ArrayList<String> clientCommandLine = null;
        StringBuilder sb = new StringBuilder();
        for (int i = commandStartingIndex; i < args.length; i++) {
            sb.append(args[i]).append(' ');
        }
        String command = sb.toString();
        command = command.substring(0, command.length() - 1);
        if (clientCommandIsCorrect(command)) {
            clientCommandLine = new ArrayList<>();
            for (int i = 0; i < commandStartingIndex; i++) {
                clientCommandLine.add(args[i]);
            }
            clientCommandLine.add(command);
        }
        return clientCommandLine;
    }

    /**
     * Checks if the "real" command's syntax is correct.
     * @param command a space-separated String for the "real" command for client
     *              (i.e., begins with "get", "put", "del", "store" or "exit")
     * @return if the "real" command's syntax is correct
     */
    private static boolean clientCommandIsCorrect(String command) {
        boolean isCorrect = false;

        String[] commands = command.split(" ");
        if (commands[0].equals("put") && commands.length == 3) {
            isCorrect = true;
        } else if (commands[0].equals("get") && commands.length == 2) {
            isCorrect = true;
        } else if (commands[0].equals("del") && commands.length == 2) {
            isCorrect = true;
        } else if (commands[0].equals("store") && commands.length == 1) {
            isCorrect = true;
        } else if (commands[0].equals("exit") && commands.length == 1) {
            isCorrect = true;
        }

        return isCorrect;
    }

    public static String helpMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage:").append('\n').append('\n')
          .append("Client:").append('\n')
          .append("uc/tc <address> <port> put <key> <msg>\t\tUDP/TCP CLIENT: Put an object into store.").append('\n')
          .append("uc/tc <address> <port> get <key>\t\tUDP/TCP CLIENT: Get an object from store by key.").append('\n')
          .append("uc/tc <address> <port> del <key>\t\tUDP/TCP CLIENT: Delete an object from store by key.").append('\n')
          .append("uc/tc <address> <port> store\t\t\tUDP/TCP CLIENT: Display object store.").append('\n')
          .append("uc/tc <address> <port> exit\t\t\tUDP/TCP CLIENT: Shutdown server.").append('\n')
          .append("rmic <address> put <key> <msg>\t\t\tRMI CLIENT: Put an object into store.").append('\n')
          .append("rmic <address> get <key>\t\t\tRMI CLIENT: Get an object from store by key.").append('\n')
          .append("rmic <address> del <key>\t\t\tRMI CLIENT: Delete an object from store by key.").append('\n')
          .append("rmic <address> store\t\t\t\tRMI CLIENT: Display object store.").append('\n')
          .append("rmic <address> exit\t\t\t\tRMI CLIENT: Shutdown server.").append('\n').append('\n')
          .append("Server:").append('\n')
          .append("us/ts <port>\t\t\t\t\tUDP/TCP SERVER: Run server on <port>.").append('\n')
          .append("rmis\t\t\t\t\t\tRMI Server.").append('\n');
        return sb.toString();
    }

    public static void main(String[] args) {
        ArrayList<String> commandLine = parseCommandlineArgs(args);
        if (commandLine == null) {
            System.out.print(helpMessage());
        } else if (commandLine.get(0).equals("ts")) {
            TCPServer server = new TCPServer(Integer.parseInt(commandLine.get(1)));
            new Thread(server).start();
        } else if (commandLine.get(0).equals("us")) {
            UDPServer server = new UDPServer(Integer.parseInt(commandLine.get(1)));
            new Thread(server).start();
        } else if (commandLine.get(0).equals("rmis")) {
//            System.out.println("rmis");
            RMIServer server = new RMIServer();
            server.start();
        } else if (commandLine.get(0).equals("tc")) {
            TCPClient client =
                    new TCPClient(commandLine.get(1), Integer.parseInt(commandLine.get(2)), commandLine.get(3));
            client.act();
        } else if (commandLine.get(0).equals("uc")) {
            UDPClient client =
                    new UDPClient(commandLine.get(1), Integer.parseInt(commandLine.get(2)), commandLine.get(3));
            client.act();
        } else if (commandLine.get(0).equals("rmic")) {
//            System.out.println("rmic");
            RMIClient client = new RMIClient(commandLine.get(1), commandLine.get(2));
            client.act();
        }
    }
}
