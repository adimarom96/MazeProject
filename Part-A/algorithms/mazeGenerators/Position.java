package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable {

    private int row;
    private int col;

    // constructor
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRowIndex() {
        return row;
    }

    public int getColumnIndex() {
        return col;
    }

    /**
     * format of {row,column}
     * @return string of the position.
     */
    @Override
    public String toString() {
        return "{" + row + "," + col + "}";
    }
}
