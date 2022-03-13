package algorithms.search;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class BestFirstSearch extends BreadthFirstSearch {

    /**
     * This class implements "Compare class" and the 'compare function'
     * use to compare between 2 states and decide which 1 of them cost less so we can prioritize him in the Q.
     */
    public static class costCompare implements Comparator<AState> {
        public int compare(AState t0, AState t1) {
            if (t0.getCost() > t1.getCost())
                return 1;
            else if (t0.getCost() < t1.getCost())
                return -1;
            return 0;
        }
    }

    // constructor
    public BestFirstSearch() {
        super("BestFirstSearch", new PriorityQueue<AState>(2, new costCompare()));
        //super("BestFS", new PriorityQueue<AState>((AState a1, AState a2) -> Double.compare(a2.getCost(), a1.getCost())));
        isBest = true;
    }
}