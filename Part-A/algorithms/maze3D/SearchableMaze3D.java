package algorithms.maze3D;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.ISearchable;
import algorithms.search.MazeState;
import java.util.ArrayList;
import java.util.Random;

public class SearchableMaze3D implements ISearchable {
    private Maze3D maze; // new private
    private Maze3DState[][][] statesArray;

    // constructor
    public SearchableMaze3D(Maze3D maze) {
        this.maze = maze;
        Random random = new Random();
        int x;
        Maze3DState[][][] statesArray1 = new Maze3DState[maze.getNumOfDepth()][maze.getNumOfRow()][maze.getNumOfCol()];
        for (int z = 0; z < maze.getNumOfDepth(); z++) {
            for (int i = 0; i < maze.getNumOfRow(); i++) {
                for (int j = 0; j < maze.getNumOfCol(); j++) {
                    //x = random.nextInt(50);// random for the cost of each state
                    statesArray1[z][i][j] = new Maze3DState(10/*cost*/, null, new Position3D(z, i, j));
                }
            }
        }
        this.statesArray = statesArray1;
    }

    @Override
    public AState getStart() {
        Maze3DState state = new Maze3DState(1, null, maze.getStartPosition());
        return state;
    }

    @Override
    public AState getGoal() {
        Maze3DState state = new Maze3DState(1, null, maze.getGoalPosition());
        return state;
    }

    /**
     * this function checks if the position we got is legal.
     * @param z
     * @param x
     * @param y
     * @return
     */
    private boolean inBorder(int z, int x, int y) {
        if (x > -1 && x < maze.getNumOfRow()) {
            if (z > -1 && z < maze.getNumOfDepth())
                return y > -1 && y < maze.getNumOfCol();
        }
        return false;
    }

    /**
     * this function returns all the possible states from the AState we got
     * @param state
     * @return ArrayList of all the neighbors we can travel to from the state param
     */
    @Override
    public ArrayList<AState> getAllSuccessors(AState state) {
        ArrayList<AState> possibleState = new ArrayList<AState>();
        int z = ((Maze3DState) state).getPos().getDepthIndex();
        int x = ((Maze3DState) state).getPos().getRowIndex();
        int y = ((Maze3DState) state).getPos().getColumnIndex();

        //-----------------------------right
        if (inBorder(z, x, y + 1)) {
            if (maze.possibleToGo(z, x, y + 1)) {
                possibleState.add(statesArray[z][x][y + 1]);
            }
        }
        //------------------------------left
        if (inBorder(z, x, y - 1)) {
            if (maze.possibleToGo(z, x, y - 1)) {
                possibleState.add(statesArray[z][x][y - 1]);
            }
        }
        //-------------------------------forward
        if (inBorder(z, x - 1, y)) {
            if (maze.possibleToGo(z, x - 1, y)) {
                possibleState.add(statesArray[z][x - 1][y]);
            }
        }
        // ------------------------------backward
        if (inBorder(z, x + 1, y)) {
            if (maze.possibleToGo(z, x + 1, y)) {
                possibleState.add(statesArray[z][x + 1][y]);
            }
        }
        //-------------------------------up
        if (inBorder(z + 1, x, y)) {
            if (maze.possibleToGo(z + 1, x, y)) {
                possibleState.add(statesArray[z + 1][x][y]);
            }
        }
        // ------------------------------down
        if (inBorder(z - 1, x, y)) {
            if (maze.possibleToGo(z - 1, x, y)) {
                possibleState.add(statesArray[z - 1][x][y]);
            }
        }
        return possibleState;
    }

    /**
     * this function reset all the "preState" board before we try to solve it again.
     */
    @Override
    public void restStates() {
        for (int z = 0; z < maze.getNumOfDepth(); z++) {
            for (int i = 0; i < maze.getNumOfRow(); i++) {
                for (int j = 0; j < maze.getNumOfCol(); j++) {
                    statesArray[z][i][j].setPreState(null);
                    statesArray[z][i][j].setVisited(false);
                }
            }
        }
    }
}