package test;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RunCommunicateWithServers {
    static ReentrantLock m;

    public static void main(String[] args) {
        m = new ReentrantLock();
        //Initializing servers
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

        //Starting servers
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();

        Thread[] Tarr = new Thread[10];
        for (int i = 0; i < Tarr.length; i++) {
            Tarr[i] = new Thread(RunCommunicateWithServers::CommunicateWithServer_SolveSearchProblem);
            Tarr[i].start();

        }
        for (int i = 0; i < Tarr.length; i++) {
            try {
                Tarr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < Tarr.length; i++) {
            Tarr[i] = new Thread(RunCommunicateWithServers::CommunicateWithServer_MazeGenerating);
            Tarr[i].start();

        }
        for (int i = 0; i < Tarr.length; i++) {
            try {
                Tarr[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Communicating with servers
        //CommunicateWithServer_MazeGenerating();
        //CommunicateWithServer_SolveSearchProblem();

        //Stopping all servers
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    private static void CommunicateWithServer_MazeGenerating() {
        try {
            Client client1 = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int x = 100;
                        int[] mazeDimensions = new int[]{x, x};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor)from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] b = new byte[x * x + 24]; //allocating byte[] for the decompressed maze -
                        is.read(b); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(b);
                        maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client1.communicateWithServer();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(7, 6);

                        toServer.writeObject(maze); //send maze to server

                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        //Print Maze Solution retrieved from the server
                        m.lock();
                        //System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            //System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }
                        m.unlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}