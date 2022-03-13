package algorithms.maze3D;

public class Position3D {
    private int row;
    private int col;
    private int depth;

    // constructor
    public Position3D(int depth, int row, int col) {
        this.row = row;
        this.col = col;
        this.depth = depth;
    }

    public int getRowIndex() {
        return row;
    }

    public int getColumnIndex() {
        return col;
    }

    public int getDepthIndex() {
        return depth;
    }

    /**
     * format of {depth,row,column}
     * @return string of the position.
     */
    @Override
    public String toString() {
        return "{" + depth + "," + row + "," + col + "}";
    }
}
