package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
import java.util.Random;

import static algorithms.search.BreadthFirstSearch.isBest;

public class SearchableMaze implements ISearchable {
    static double regularStepCost = 10;
    static double diagonalCost = 15;

    private Maze maze;// new private!
    private MazeState[][] statesArray;// new private!

    // constructor
    public SearchableMaze(Maze maze) {
        this.maze = maze;
        Random random = new Random();
        double cost;
        MazeState[][] statesArray1 = new MazeState[maze.getNumOfRow()][maze.getNumOfCol()];
        for (int i = 0; i < maze.getNumOfRow(); i++) {
            for (int j = 0; j < maze.getNumOfCol(); j++) {
                cost = regularStepCost;
                statesArray1[i][j] = new MazeState(cost, null, new Position(i, j));
            }
        }
        Position p = maze.getStartPosition();
        statesArray1[p.getRowIndex()][p.getColumnIndex()].setCost(0);
        this.statesArray = statesArray1;
    }

    @Override
    public AState getStart() {
        MazeState state = new MazeState(0, null, maze.getStartPosition());
        return state;
    }

    @Override
    public AState getGoal() {
        MazeState state = new MazeState(1, null, maze.getGoalPosition());
        return state;
    }

    /**
     * this function checks if the position we got is legal.
     * @param x
     * @param y
     * @return
     */
    private boolean inBorder(int x, int y) {
        if (x > -1 && x < maze.getNumOfRow()) {
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
        // return all the neighbors that i can go from this current pos
        ArrayList<AState> possibleState = new ArrayList<AState>();
        int x = ((MazeState) state).getPos().getRowIndex();
        int y = ((MazeState) state).getPos().getColumnIndex();
        double curCost = statesArray[x][y].getCost();
        //if (maze.getStartPosition().getColumnIndex() == y && maze.getStartPosition().getRowIndex() == x)
         //   curCost = 0;
        //-----------------------------right
        if (inBorder(x, y + 1)) {
            if (maze.possibleToGo(x, y + 1)) {

                //right up
                if (inBorder(x - 1, y + 1)) {
                    if (maze.possibleToGo(x - 1, y + 1) && !possibleState.contains(statesArray[x - 1][y + 1])) {
                        updateCost(x - 1, y + 1, curCost + diagonalCost);
                        //statesArray[x - 1][y + 1].setCost(curCost + diagonalCost);// diagonal step (ohad)
                        possibleState.add(statesArray[x - 1][y + 1]);
                    }
                }

                // right bottom
                if (inBorder(x + 1, y + 1)) {
                    if (maze.possibleToGo(x + 1, y + 1) && !possibleState.contains(statesArray[x + 1][y + 1])) {
                        updateCost(x + 1, y + 1, curCost + diagonalCost);
                        //statesArray[x + 1][y + 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x + 1][y + 1]);
                    }
                }
                updateCost(x, y + 1, curCost + regularStepCost);
                possibleState.add(statesArray[x][y + 1]);
            }
        }
        //------------------------------left
        if (inBorder(x, y - 1)) {
            if (maze.possibleToGo(x, y - 1)) {


                //left up
                if (inBorder(x - 1, y - 1)) {
                    if (maze.possibleToGo(x - 1, y - 1) && !possibleState.contains(statesArray[x - 1][y - 1])) {
                        updateCost(x - 1, y - 1, curCost + diagonalCost);
                        //statesArray[x - 1][y - 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x - 1][y - 1]);
                    }
                }

                // left bottom
                if (inBorder(x + 1, y - 1)) {
                    if (maze.possibleToGo(x + 1, y - 1) && !possibleState.contains(statesArray[x + 1][y - 1])) {
                        updateCost(x + 1, y - 1, curCost + diagonalCost);
                        //statesArray[x + 1][y - 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x + 1][y - 1]);
                    }
                }
                updateCost(x, y - 1, curCost + regularStepCost);
                possibleState.add(statesArray[x][y - 1]);
            }
        }
        //-------------------------------up
        if (inBorder(x - 1, y)) {
            if (maze.possibleToGo(x - 1, y)) {

                //up left
                if (inBorder(x - 1, y - 1)) {
                    if (maze.possibleToGo(x - 1, y - 1) && !possibleState.contains(statesArray[x - 1][y - 1])) {
                        updateCost(x - 1, y - 1, curCost + diagonalCost);
                        //statesArray[x - 1][y - 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x - 1][y - 1]);
                    }
                }

                // up right
                if (inBorder(x - 1, y + 1)) {
                    if (maze.possibleToGo(x - 1, y + 1) && !possibleState.contains(statesArray[x - 1][y + 1])) {
                        updateCost(x - 1, y + 1, curCost + diagonalCost);
                        //statesArray[x - 1][y + 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x - 1][y + 1]);
                    }
                }
                updateCost(x - 1, y, curCost + regularStepCost);
                possibleState.add(statesArray[x - 1][y]);
            }
        }
        // ------------------------------bottom
        if (inBorder(x + 1, y)) {
            if (maze.possibleToGo(x + 1, y)) {

                //bottom left
                if (inBorder(x + 1, y - 1)) {
                    if (maze.possibleToGo(x + 1, y - 1) && !possibleState.contains(statesArray[x + 1][y - 1])) {
                        updateCost(x + 1, y - 1, curCost + diagonalCost);
                        //statesArray[x + 1][y - 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x + 1][y - 1]);
                    }
                }
                // bottom right
                if (inBorder(x + 1, y + 1)) {
                    if (maze.possibleToGo(x + 1, y + 1) && !possibleState.contains(statesArray[x + 1][y + 1])) {
                        updateCost(x + 1, y + 1, curCost + diagonalCost);
                        //statesArray[x + 1][y + 1].setCost(curCost + diagonalCost);
                        possibleState.add(statesArray[x + 1][y + 1]);
                    }
                }
                updateCost(x + 1, y, curCost + regularStepCost);
                possibleState.add(statesArray[x + 1][y]);
            }
        }
        return possibleState;
    }

    /**
     * this function reset all the "preState" board before we try to solve it again.
     */
    @Override
    public void restStates() {
        for (int i = 0; i < maze.getNumOfRow(); i++) {
            for (int j = 0; j < maze.getNumOfCol(); j++) {
                statesArray[i][j].setPreState(null);
                statesArray[i][j].setVisited(false);
                statesArray[i][j].setCost(regularStepCost);//regularStepCost
            }
        }
        Position p = maze.getStartPosition();
        statesArray[p.getRowIndex()][p.getColumnIndex()].setCost(0);
    }

    /**
     * This function gets location and new cost, checks if need to update to the new cost.
     * @param x
     * @param y
     * @param newCost
     */
    private void updateCost(int x, int y, double newCost) {
        // new cost is the cost of the current cell + the cost of the way to him
        double curCost = statesArray[x][y].getCost();
        if (curCost == regularStepCost && !statesArray[x][y].isVisited()) {//if not visited and has original cost
            statesArray[x][y].setVisited(true);
            statesArray[x][y].setCost(newCost);
            return;
        }
        if (isBest && newCost < curCost) { // if this is the best algorithm  and the update is better.
            statesArray[x][y].setCost(newCost);
            statesArray[x][y].setPreState(null);

        }
    }
}