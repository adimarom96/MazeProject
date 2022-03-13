package algorithms.search;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    static int count = 0;
    protected String name;
    protected int numberOfNodesEvaluated;
    static boolean isBest = false;

    //constructor
    public ASearchingAlgorithm(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    public int getNumOfNode() {
        return numberOfNodesEvaluated;
    }
}