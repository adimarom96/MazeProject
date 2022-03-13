package algorithms.maze3D;

public class Maze3D {

   private int numOfDepth;
    private int numOfRow;
    private int numOfCol;
    private int[][][] map;
    private Position3D StartPosition;
    private Position3D GoalPosition;

    public int[][][] getMap() {
        return map;
    }

    // constructor
    public Maze3D(int depth, int row, int col) {
        numOfDepth = depth;
        numOfCol = col;
        numOfRow = row;
        map = new int[depth][row][col];
    }

    public Position3D getStartPosition() {
        return StartPosition;
    }

    public void setStartPosition(Position3D startPosition) {
        StartPosition = startPosition;
    }

    public Position3D getGoalPosition() {
        return GoalPosition;
    }

    public void setGoalPosition(Position3D goalPosition) {
        GoalPosition = goalPosition;
    }

    public int getNumOfDepth() {
        return numOfDepth;
    }

    public int getNumOfRow() {
        return numOfRow;
    }

    public int getNumOfCol() {
        return numOfCol;
    }

    /**
     * this function checks if the position we got is good to go ( = 0 ).
     */
    public Boolean possibleToGo(int z, int x, int y) {
        if (map[z][x][y] == 0)
            return true;
        return false;
    }

    /**
     * this function print the 3D maze as requested.
     */
    public void print() {
        System.out.println("{");
        for (int depth = 0; depth < map.length; depth++) {
            for (int row = 0; row < map[0].length; row++) {
                System.out.print("{ ");
                for (int col = 0; col < map[0][0].length; col++) {
                    if (depth == this.getStartPosition().getDepthIndex() && row == this.getStartPosition().getRowIndex() && col == this.getStartPosition().getColumnIndex()) // if the position is the start - mark with S
                        System.out.print("S ");
                    else {
                        if (depth == this.getGoalPosition().getDepthIndex() && row == this.getGoalPosition().getRowIndex() && col == this.getGoalPosition().getColumnIndex()) // if the position is the goal - mark with E
                            System.out.print("E ");
                        else
                            System.out.print(map[depth][row][col] + " ");
                    }
                }
                System.out.println("}");
            }
            if (depth < map.length - 1) {
                System.out.print("---");
                for (int i = 0; i < map[0][0].length; i++)
                    System.out.print("--");
                System.out.println();
            }
        }
        System.out.println("}");
    }
}