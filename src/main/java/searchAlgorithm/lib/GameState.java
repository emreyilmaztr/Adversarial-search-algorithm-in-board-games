package searchAlgorithm.lib;

import java.util.List;
import java.util.Map;

public abstract class GameState<A extends Action> {
    public abstract GameState<A> getDeepCopy();
    public abstract void process(A action, User user);
    public abstract boolean isTerminal();
    public abstract List<A> getLegalActions();
    public abstract Map<User, Double> getUtilityMap();
    public abstract double getUtility(User user);
    public abstract User getTurn();
    public abstract String toString();
    public abstract void reset();
    public abstract boolean getReplayStatus();
    public abstract int getBoardSize();
}