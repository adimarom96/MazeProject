package algorithms.mazeGenerators;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Maze implements Serializable {
    int[][] mazeArr;
    private int numOfRow;
    private int numOfCol;
    private Position StartPosition;
    private Position GoalPosition;

    // constructor
    public Maze(int row, int col) {
        numOfCol = col;
        numOfRow = row;
        mazeArr = new int[row][col];
    }
    // constructor
    public Maze(byte[] savedMazeBytes) {
        // constructor for the decompressor
        byte [] first = Arrays.copyOfRange(savedMazeBytes, 0, 24);
        IntBuffer intBuf = ByteBuffer.wrap(first).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        numOfRow = array[0];
        numOfCol = array[1];
        Position start = new Position(array[2], array[3]);
        Position goal = new Position(array[4], array[5]);

        this.setMazeArr(toTwoD(numOfRow, numOfCol, Arrays.copyOfRange(savedMazeBytes, 24, savedMazeBytes.length)));
        this.setStartPosition(start);
        this.setGoalPosition(goal);
    }

    public int[][] getMazeArr() {
        return mazeArr;
    }

    public void setMazeArr(int[][] mazeArr) {
        this.mazeArr = mazeArr;
    }

    public Position getStartPosition() {
        return StartPosition;
    }

    public void setStartPosition(Position startPosition) {
        StartPosition = startPosition;
    }

    public Position getGoalPosition() {
        return GoalPosition;
    }

    public void setGoalPosition(Position goalPosition) {
        GoalPosition = goalPosition;
    }

    public int getNumOfRow() {
        return numOfRow;
    }

    public int getNumOfCol() {
        return numOfCol;
    }

    /**
     * @param x - row index.
     * @param y - column index.
     * @return rather the spot (x,y) is reasonable to go - if holds 0.
     */
    public Boolean possibleToGo(int x, int y) {
        if (mazeArr[x][y] == 0)
            return true;
        return false;
    }

    public void print() {
        for (int i = 0; i < numOfRow; i++) {
            System.out.print(" { "); // new row
            for (int j = 0; j < numOfCol; j++) {
                if (this.getStartPosition() != null && this.getStartPosition().getColumnIndex() == j && this.getStartPosition().getRowIndex() == i)
                    System.out.print("S "); // start point as S
                else if (this.getGoalPosition() != null && this.getGoalPosition().getColumnIndex() == j && this.getGoalPosition().getRowIndex() == i)
                    System.out.print("E "); // end point as E
                else
                    System.out.print(this.mazeArr[i][j] + " ");
            }
            System.out.println("}"); // end of row
        }
    }

    /**
     * this function took the maze and shrink it into one byte array.
     * the format is: row, col, X start, Y start, X end, Y end, all the maze
     *
     * @return byte array with all the information of the maze.
     */
    public byte[] toByteArray() {
        // to check use this website: https://cryptii.com/pipes/integer-converter
        int row = getNumOfRow();
        int col = getNumOfCol();
        Position start = getStartPosition();
        Position goal = getGoalPosition();

        int[] oneDarr = toOneD();
        int[] Metadata = {row, col, start.getRowIndex(), start.getColumnIndex(), goal.getRowIndex(), goal.getColumnIndex()};
        int[] all = new int[oneDarr.length + Metadata.length];
        System.arraycopy(Metadata, 0, all, 0, Metadata.length);
        System.arraycopy(oneDarr, 0, all, Metadata.length, oneDarr.length);

        ByteBuffer byteBuffer = ByteBuffer.allocate(all.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(all);
        byte[] array = byteBuffer.array();
        return array;
    }

    /**
     * private function that flatten the matrix to only 1 dimension.
     * @return array of all the data in the maze.
     */
    private int[] toOneD() {
        int count = 0;
        int[] arr = new int[this.getNumOfRow() * this.getNumOfCol()];
        for (int i = 0; i < numOfRow; i++) {
            for (int j = 0; j < numOfCol; j++) {
                arr[count] = mazeArr[i][j];
                count++;
            }
        }
        return arr;
    }

    /**
     * @param row
     * @param col
     * @param oneD - only the 0\1 of the matrix but in 1D array
     * @return
     */
    private int[][] toTwoD(int row, int col, byte[] oneD) {
        int count = 0;
        int[][] matrix = new int[row][col];
        for (int i = 0; i < numOfRow; i++) {
            for (int j = 0; j < numOfCol; j++) {
                matrix[i][j] = oneD[count];
                count++;
            }
        }
        return matrix;
    }
}