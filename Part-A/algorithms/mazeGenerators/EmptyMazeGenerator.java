package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {

    /**
     *
     * @param row
     * @param col
     * @return new maze with all 0 - empty maze.
     */
    @Override
    public Maze generate(int row, int col) {
        if (row < 2 || col < 2)
            throw new RuntimeException("Wrong parameters !");
        Maze empty_maze = new Maze(row, col);
        int[][] map = empty_maze.getMazeArr();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = 0;
            }
        }
        empty_maze.setStartPosition(new Position(0, 0));
        empty_maze.setGoalPosition(new Position(row - 1, col - 1));
        return empty_maze;
    }
}
