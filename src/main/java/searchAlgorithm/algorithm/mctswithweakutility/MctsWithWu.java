package searchAlgorithm.algorithm.mctswithweakutility;

import searchAlgorithm.lib.*;

import java.util.*;

public class MctsWithWu<A extends Action> implements ISearchAlgorithm {
    private int EXPAND_THRESHOLD;
    private long TIME_LIMIT;
    private double C;
    private Random rand;
    private boolean verbose;

    public MctsWithWu() {
        this.EXPAND_THRESHOLD = 1;
        this.TIME_LIMIT = 1000;
        this.C = Math.sqrt(2);
        this.rand = new Random();
        this.verbose = false;
    }

    /**
     * Set EXPAND_THRESHOLD (default 1)
     * The MCTS expand a node when visits number is EXPAND_THRESHOLD
     * @param threshold
     */
    public void setExpandThreshold(int threshold) {
        this.EXPAND_THRESHOLD = threshold;
    }

    /**
     * Set TIME_LIMIT (default 1000)
     * The MCTS finish the search
     * @param l time limit in millisecond
     */
    public void setTimeLimit(long l) {
        this.TIME_LIMIT = l;
    }

    /**
     * Set C (default sqrt(2))
     * The MCTS select a node maxixmize (average value) + C * sqrt(log(parent visits)/(visits))
     * @param c
     */
    public void setC(double c) {
        this.C = c;
    }

    /**
     * set random
     * @param r
     */
    public void setRand(Random r) {
        this.rand = r;
    }

    /**
     * set verbose (default false)
     * @param v
     */
    public void setVerbose(boolean v) {
        this.verbose = v;
    }

    /**
     * Selct an action with Monte Carlo Tree Search
     * @param gameState
     * @return action
     */
    @Override
    public A getAction(GameState gameState, User user)
    {
        GameState<A> root = gameState.getDeepCopy();
        MctsWithWuSearcher<A> searcher = new MctsWithWuSearcher<A>(this.EXPAND_THRESHOLD, this.TIME_LIMIT, this.C, this.rand.nextLong(), root);
        searcher.applyAlgorithm(user);

        // Sum each thread value
        Map<A, Node<A>> children = searcher.getRootChildren();

        // Final move selection.
        A ret = null;
        double bestValue = -100.0;

        for (Map.Entry<A, Node<A>> entry : children.entrySet())
        {
            A action = entry.getKey();
            Node<A> node = entry.getValue();

            if (verbose) {
                System.out.println("action:"+action+"  average value:"+(node.numWin /node.numVisits)+"  num visits:"+node.numVisits);
            }
            double value = node.numWin / node.numVisits;
            if (ret == null || bestValue < value) {
                bestValue = value;
                ret = action;
            }
        }

        return ret;
    }


}