package algorithms.maze3D;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;

public class Maze3DState extends AState {
    private Position3D pos;
    private boolean visited;

    // constructor
    public Maze3DState(double cost, AState preState, Position3D pos) {
        super(cost, preState);
        this.pos = pos;
        this.visited = false;
    }

    public Position3D getPos() {
        return pos;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * checks if the state we have equals to the state we got
     * @param obj
     * @return true/false according to if the states are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this.pos.getColumnIndex() == ((Maze3DState) obj).getPos().getColumnIndex()
                && this.pos.getRowIndex() == ((Maze3DState) obj).getPos().getRowIndex()
                && this.pos.getDepthIndex() == ((Maze3DState) obj).getPos().getDepthIndex())
            return true;
        return false;
    }

    /**
     * ToString func
     * @return string in {depth,row,col} shape
     */
    @Override
    public String toString() {
        return this.getPos().toString();
    }
}