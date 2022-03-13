package algorithms.search;
import java.util.ArrayList;

public interface ISearchable {
    AState getStart();
    AState getGoal();
    ArrayList<AState> getAllSuccessors(AState state);
    void restStates();
}