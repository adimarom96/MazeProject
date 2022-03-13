package algorithms.maze3D;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.util.LinkedList;
import java.util.Random;

public class MyMaze3DGenerator extends AMaze3DGenerator {
    /**
     * this function gets maze and set him all to 1, all walls.
     *
     * @param maze
     * @return maze with all 1's
     */
    private Maze3D init_with_one(Maze3D maze) {
        int [][][] matrix = maze.getMap();
        for (int z = 0; z < maze.getNumOfDepth(); z++) {
            for (int i = 0; i < maze.getNumOfRow(); i++) {
                for (int j = 0; j < maze.getNumOfCol(); j++) {
                    matrix[z][i][j] = 1;
                }
            }
        }
        return maze;
    }

    /**
     * This function makes way throw all the 1's in the maze. using Prim's algorithm to breaks walls and set them to 0 instead of 1.
     *
     * @param depth
     * @param row
     * @param column
     * @return a new maze with possible way from the start to the end point.
     */
    @Override
    public Maze3D generate(int depth, int row, int column) {
        if (row < 2 || column < 2 || depth < 2)
            throw new RuntimeException("Wrong parameters !");

        if (row == 2 && column == 2 && depth == 2) { //generate 2 * 2 *2 maze
            MyMazeGenerator mazeGenerator =  new MyMazeGenerator();
            Maze maze1 = mazeGenerator.generate(2, 2);
            Maze maze2 = mazeGenerator.generate(2, 2);
            Maze3D maze = new Maze3D(2,2,2);
            int [][][] matrix = maze.getMap();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    matrix[0][i][j]=maze1.getMazeArr()[i][j];
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    matrix[0][i][j]=maze2.getMazeArr()[i][j];
                }
            }

            maze.setStartPosition(new Position3D(0,maze1.getStartPosition().getRowIndex(),maze1.getStartPosition().getColumnIndex()));
            maze.setGoalPosition(new Position3D(1,maze2.getGoalPosition().getRowIndex(),maze1.getGoalPosition().getColumnIndex()));
            return maze;
        }

        //initialize
        int z, x, y;
        LinkedList<int[]> designated = new LinkedList<>();// list of arrays that contain the possible path to the next move
        final Random random = new Random();
        Maze3D maze = init_with_one(new Maze3D(depth, row, column));// init
        z = 0;
        x = 0;
        y = random.nextInt(column);
        int [][][] matrix = maze.getMap();
        designated.add(new int[]{z, x, y, z, x, y});
        while (!designated.isEmpty()) {
            final int[] f = designated.remove(random.nextInt(designated.size()));
            z = f[3];
            x = f[4];
            y = f[5];
            if (matrix[z][x][y] == 1) {
                matrix[f[0]][f[1]][f[2]] = matrix[z][x][y] = 0;

                if (z >= 2 && matrix[z - 2][x][y] == 1)
                    designated.add(new int[]{z - 1, x, y, z - 2, x, y});

                if (x >= 2 && matrix[z][x - 2][y] == 1)
                    designated.add(new int[]{z, x - 1, y, z, x - 2, y});

                if (y >= 2 && matrix[z][x][y - 2] == 1)
                    designated.add(new int[]{z, x, y - 1, z, x, y - 2});

                if (z < depth - 2 && matrix[z + 2][x][y] == 1)
                    designated.add(new int[]{z + 1, x, y, z + 2, x, y});

                if (x < row - 2 && matrix[z][x + 2][y] == 1)
                    designated.add(new int[]{z, x + 1, y, z, x + 2, y});

                if (y < column - 2 && matrix[z][x][y + 2] == 1)
                    designated.add(new int[]{z, x, y + 1, z, x, y + 2});
            }
        }
        setPos(maze);// function that set the start and goal point
        return maze;
    }

    /**
     * This function gets a maze and set his start and end points.
     *
     * @param maze
     */
    private void setPos(Maze3D maze) {
        int [][][] matrix = maze.getMap();

        // set start point
        for (int i = 0; i < maze.getNumOfCol(); i++) {
            if (matrix[0][0][i] == 0) {
                maze.setStartPosition(new Position3D(0, 0, i));
               matrix[0][0][i] = 0;
                break;
            }
        }
        // set goal point
        outerloop:
        // label to break the nested loop
        for (int i = maze.getNumOfCol() - 1; i > 0; i--) {
            for (int j = maze.getNumOfRow() - 1; j > 0; j--) {
                if (matrix[maze.getNumOfDepth() - 1][j][i] == 0) {
                    maze.setGoalPosition(new Position3D(maze.getNumOfDepth() - 1, j, i));
                    matrix[maze.getNumOfDepth() - 1][j][i] = 0;
                    break outerloop; // break both loops !
                }
                if (matrix[maze.getNumOfDepth() - 2][j][i] == 0) {
                    maze.setGoalPosition(new Position3D(maze.getNumOfDepth() - 1, j, i));
                  matrix[maze.getNumOfDepth() - 1][j][i] = 0;
                    break outerloop; // break both loops !
                }
            }
            //if here so there is no end point!
        }
    }
}