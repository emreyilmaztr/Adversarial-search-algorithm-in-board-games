package searchAlgorithm.algorithm.mctswithweakutility;

import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.Node;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.*;

public class MctsWithWuSearcher<A extends Action>{

    private int EXPAND_THRESHOLD;
    private long TIME_LIMIT;
    private double C;
    private Random rand;

    private Node<A> rootNode;

    MctsWithWuSearcher(int e, long t, double c, long seed, GameState<A> rootGameState) {
        this.EXPAND_THRESHOLD = e;
        this.TIME_LIMIT = t;
        this.C = c;

        this.rand = new Random(seed);
        this.rootNode = new Node<A>(rootGameState, null);
    }
    public void applyAlgorithm() {
        //long sTime = System.currentTimeMillis();
        //while (System.currentTimeMillis() - sTime < this.TIME_LIMIT)
        int iteration = 0;

        while (iteration < this.TIME_LIMIT)
        {
            Node<A> selected = select(this.rootNode);
            if (selected.numVisits >= this.EXPAND_THRESHOLD - 1) {
                expand(selected);
            }
            Map<User, Double> score = rollout(selected);
            backPropagate(selected, score);

            iteration++;
        }
    }

    public Map<A, Node<A>> getRootChildren() {
        return this.rootNode.children;
    }

    private Node<A> select(Node<A> node) {

        // In the selection phase, if number of sampled is zero or state is final, do rollout.
        if (node.gameState.isTerminal() || node.numVisits < EXPAND_THRESHOLD) {
            return node;
        }

        // Select randomly choose from among the leaf nodes.
        ArrayList<Node<A>> candidates = new ArrayList<Node<A>>();
        for (Node<A> n : node.children.values()) {
            if (n.numVisits < this.EXPAND_THRESHOLD) {
                candidates.add(n);
            }
        }
        int size = candidates.size();
        if (size != 0) {
            return candidates.get(this.rand.nextInt(size));
        }

        Node<A> bestNode = null;
        double bestScore = Double.MIN_VALUE;

        for (Node<A> n : node.children.values()) {

            double gamma = -0.1;
            double alpha = Math.exp(gamma * n.numVisits);
            double ui = n.gameState.getUtility(n.parent.gameState.getTurn());

            //double exploit = n.value / n.numVisits;
            //double explore = (alpha * ui) + (1 - alpha) * C * Math.sqrt(Math.log(n.parent.numVisits)/n.numVisits);
            //double explore = (alpha * ui) + (ui + 1) / 2 * C * Math.sqrt(Math.log(n.parent.numVisits)/n.numVisits);

            double exploit = (alpha * ui) + (1 - alpha) * n.numWin / n.numVisits;
            double explore = C * Math.sqrt(Math.log(n.parent.numVisits)/n.numVisits);

            double score = exploit + explore;

            if (bestNode == null || score > bestScore) {
                bestScore = score;
                bestNode = n;
            }
        }
        return select(bestNode);
    }

    private void expand(Node<A> node) {
        node.children = new HashMap<A, Node<A>>();

        List<A> actions = node.gameState.getLegalActions();
        for (A a : actions) {
            GameState<A> nextGameState = node.gameState.getDeepCopy();
            nextGameState.process(a, User.NULL);
            Node<A> child = new Node<A>(nextGameState, node);
            node.children.put(a, child);
        }
    }
    private Map<User, Double> rollout(Node<A> node) {

        GameState<A> gameState = node.gameState.getDeepCopy();
        while (!gameState.isTerminal()) {
            List<A> actions = gameState.getLegalActions();
            int getN = rand.nextInt(actions.size());

            A m = actions.get(getN);
            gameState.process(m, User.NULL);
        }
        return gameState.getScoreMap();
    }

    private void backPropagate(Node<A> node, Map<User, Double> score) {
        node.numVisits += 1;
        while (node.parent != null) {
            node.numWin += score.get(node.parent.gameState.getTurn());
            node = node.parent;
            node.numVisits += 1;
        }
    }
}