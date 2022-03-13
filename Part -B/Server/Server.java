package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private ThreadPoolExecutor executor;
    private boolean stop;

    // constructor
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        // read from config the basic properties.
        Configurations config = Configurations.getInstance();
        String x = Configurations.getInstance().getP("threadPoolSize");
        String y = Configurations.getInstance().getP("problemSolver");
        String z = Configurations.getInstance().getP("generateMaze");
        //if any of them is null, reset it to default.
        if (x == null) {
            Configurations.setP("threadPoolSize", "4");
        }
        if (y == null) {
            Configurations.setP("problemSolver", "BreadthFirstSearch");
        }
        if (z == null) {
            Configurations.setP("generateMaze", "MyMazeGenerator");
        }
        String str = config.getP("threadPoolSize");
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(str));
    }

    public void start() {
        new Thread(() -> runServer()).start();
    }

    private void runServer() {
        try {
            // make new server socket
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    InputStream inFromClient = clientSocket.getInputStream();
                    OutputStream outToClient = clientSocket.getOutputStream();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            setServerStrategy(inFromClient, outToClient, clientSocket);
                        }
                    });
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timeout ");
                }
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);
            serverSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setServerStrategy(InputStream inFromClient, OutputStream outToClient, Socket clientSocket) {
        try {
            this.strategy.ServerStrategy(inFromClient, outToClient);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
    }
}