package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ServerStrategySolveSearchProblem implements IServerStrategy, Serializable {

    private static ConcurrentHashMap<String, String> map;
    private static ReentrantLock m;

    public ServerStrategySolveSearchProblem() {
        map = new ConcurrentHashMap<>();
        m = new ReentrantLock();
    }

    @Override
    public void ServerStrategy(InputStream inFromClient, OutputStream outToClient) throws IOException {
        ISearchingAlgorithm searcher;
        m.lock();// read from file - lock to prevent interaction.
        File file = new File(System.getProperty("java.io.tmpdir"), "hash");
        file.createNewFile();// read the file or create new one.
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String l;
            while ((l = br.readLine()) != null) {
                //read the file and foreach line that holds: [key,value] add to the map
                String[] args = l.split("],", 2);
                if (args.length != 2) continue;
                String p = args[0]+"]";
                String b = args[1];
                map.put(p, b);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        m.unlock();//release the lock so others can read/write.

        // read from config which search algo to use.
        Solution sol;
        Configurations config = Configurations.getInstance();
        String search = config.getP("problemSolver");
        if (search.equals("BreadthFirstSearch"))
            searcher = new BreadthFirstSearch();
        else if (search.equals("DepthFirstSearch"))
            searcher = new DepthFirstSearch();
        else
            searcher = new BestFirstSearch();
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");//C:\Users\mille\AppData\Local\Temp\
            //System.out.println(tempDirectoryPath);
            m.lock(); // lock and read the maze.
            Maze clientMaze = (Maze) fromClient.readObject();
            m.unlock();
            //find the key that represent the maze and check if already solved it.
            byte[] mazeKey = clientMaze.toByteArray();
            String mazeKeyString = Arrays.toString(mazeKey);
            int hash = mazeKeyString.hashCode();
            if (map.containsKey(mazeKeyString)) {// check if we already solved this maze
                //if so - read it from his file, put it in "sol"
                String solFile = map.get(mazeKeyString);
                File solutionFileRead = new File(tempDirectoryPath, solFile);
                FileInputStream inFile = new FileInputStream(solutionFileRead);
                ObjectInputStream inPut = new ObjectInputStream(inFile);
                sol = (Solution) inPut.readObject();
                inPut.close();
                //System.out.println("old");
            }
            else { // solve for the first time.
                //System.out.println("new");
                SearchableMaze searchableMaze = new SearchableMaze(clientMaze);
                sol = searcher.solve(searchableMaze);
                String solFileName = "Sol" + hash + ".Solution"; // create value for the hashMap
                m.lock();
                map.put(mazeKeyString, solFileName); // add to hashMap
                File newSol = new File(tempDirectoryPath, solFileName);// create file
                ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(newSol));
                outFile.writeObject(sol);// write to the file.
                try { // write the hashMap to the file for backup.
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    for (String p : map.keySet()) {
                        bw.write(p + "," + map.get(p));
                        bw.newLine();
                    }
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.unlock();
            }
            // send the solution to the client.
            toClient.writeObject(sol);
            toClient.flush();
            toClient.close();
            fromClient.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}