package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress serverIP;
    private int serverPort;
    private IClientStrategy strategy;

    /**
     * constructor
     * @param serverIP - server ip.
     * @param serverPort - the port to listen to.
     * @param strategy - what the server should do.
     */
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    public void communicateWithServer() {
        try (Socket serverSocket = new Socket(serverIP, serverPort)) {
            //System.out.println("connected to server - IP = " + serverIP + ", Port = " + serverPort);
            strategy.clientStrategy(serverSocket.getInputStream(), serverSocket.getOutputStream());
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}