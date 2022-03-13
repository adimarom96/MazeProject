package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;
import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy, Serializable {
    @Override
    public void ServerStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        MyCompressorOutputStream comp;
        Configurations config = Configurations.getInstance();
        AMazeGenerator generator = new SimpleMazeGenerator();
        String gen = config.getP("generateMaze");
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            // find which generator to use.
            int[] size = (int[]) fromClient.readObject();
            if (gen.equals("MyMazeGenerator"))
                generator = new MyMazeGenerator();
            else if (gen.equals("EmptyMazeGenerator"))
                generator = new EmptyMazeGenerator();
            Maze maze = generator.generate(size[0], size[1]);//generate
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            comp = new MyCompressorOutputStream(bais);//compress the maze
            comp.write(maze.toByteArray());
            //send the maze to the client.
            toClient.writeObject(((ByteArrayOutputStream) comp.out).toByteArray());
            toClient.flush();
            toClient.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}