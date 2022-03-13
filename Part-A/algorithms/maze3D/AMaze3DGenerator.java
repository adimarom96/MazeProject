package algorithms.maze3D;

public abstract class AMaze3DGenerator implements IMaze3DGenerator {
    /**
     * this function generate maze with the sizes accepted and measure the time is took in ms.
     * @param depth
     * @param row
     * @param column
     * @return the time it took to create the maze.
     */
    @Override
    public long measureAlgorithmTimeMillis(int depth, int row, int column) {
        long start_time, end_time;
        start_time = System.currentTimeMillis();
        generate(depth, row, column);
        end_time = System.currentTimeMillis();
        return end_time - start_time;
    }
}