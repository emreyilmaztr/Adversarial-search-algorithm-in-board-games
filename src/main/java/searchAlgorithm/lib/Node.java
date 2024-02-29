package searchAlgorithm.lib;
import java.util.Map;

public class Node<A extends Action>{
    public GameState<A> gameState;
    public Node<A> parent;
    public Map<A, Node<A>> children;
    public int numVisits;
    public double numWin;

    public Node(GameState<A> s, Node<A> p) {
        this.gameState = s;
        this.parent = p;
        this.numVisits = 0;
        this.numWin = 0.0;
    }

    @Override
    public String toString() {
        return gameState.toString();
    }

}