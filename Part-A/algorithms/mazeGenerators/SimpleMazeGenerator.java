package algorithms.mazeGenerators;

import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {

    /**
     * This function randomly set 0 and 1 all over the maze.
     * @param row
     * @param col
     * @return a randomly maze with possible way
     */
    @Override
    public Maze generate(int row, int col) {
        if (row < 2 || col < 2)
            throw new RuntimeException("Wrong parameters !");
        Maze maze = new Maze(row, col);
        Position start = new Position(0, 0);
        Position goal = new Position(row - 1, col - 1);
        maze.setStartPosition(start);
        maze.setGoalPosition(goal);
        int[][] map = maze.getMazeArr();
        //maze.mazeArr = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Random r = new Random();
                int rnd = r.nextInt((1 - 0) + 1);
                map[i][j] = rnd;
            }
        }
        // make one way from start to goal manually
        int [][]matrix = maze.getMazeArr();
        for (int i = 0; i < row; i++) {
            matrix[i][0] = 0;
        }
        for (int i = 0; i < col; i++) {
            matrix[row - 1][i] = 0;
        }

        return maze;
    }
}