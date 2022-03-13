package algorithms.search;
import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
    private AState goal;

     // constructor
    public Solution(AState goal) {
        this.goal = goal;
    }

    /**
     * This function calculate the path from the goal point to the start point.
     * @return ArrayList of AStates.
     */
    public ArrayList<AState> getSolutionPath(){
        AState cur = goal;
        ArrayList<AState> path = new ArrayList<AState>();
        while(cur.getPreState()!= null){
            path.add(cur);
            cur= cur.getPreState();
        }
        path.add(cur);
        path = revPath(path);
        return path;
    }

    /**
     * This function gets the path from goal to start and revers it, returns path from start to end
     * @param path
     * @return the "normal" path from the beginning to the end.
     */
    private  ArrayList<AState> revPath(ArrayList<AState> path){
        ArrayList<AState> revPath =  new ArrayList<AState>();
        for (int i = path.size()-1; i >= 0  ; i--) {
            revPath.add(path.get(i));
        }
        return  revPath;
    }
}