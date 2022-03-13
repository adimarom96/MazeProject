package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator {

    @Override
    public long measureAlgorithmTimeMillis(int row, int col) {
        long start_time, end_time;
        start_time = System.currentTimeMillis();
        generate(row, col);
        end_time = System.currentTimeMillis();
        return end_time - start_time;
    }
}