package searchAlgorithm.algorithm.alphaBetaPruningWu;

import searchAlgorithm.lib.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaBetaPruningWu<A extends Action> implements ISearchAlgorithm {

    private Node<A> rootNode;
    public int maximumDepth;
    private A nextAction;

    public AlphaBetaPruningWu()
    {
        this.maximumDepth = Integer.MAX_VALUE;
    }

    public AlphaBetaPruningWu(int maximumDepth)
    {
        this.maximumDepth = maximumDepth;
    }

    @Override
    public A getAction(GameState root, User user)
    {
        nextAction = null;
        rootNode = new Node<A>(root, null);

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        User opponent = (user == User.ONE? User.TWO : User.ONE);
        double utility = getNextMode(rootNode, user, opponent, 0, alpha, beta,true);

        return nextAction;
    }

    private double getNextMode(Node<A> node, User user, User opponent, int depth, double alpha, double beta, boolean isMax)
    {
        double utility = 0;

        double currentUtility = node.gameState.getUtility(user);

        if (depth == maximumDepth || node.gameState.isTerminal())
            return currentUtility;

        if (isMax)
        {
            double maxUtility = Double.NEGATIVE_INFINITY;
            Map<A, Node<A>> children = getChild(node, user);

            for (Map.Entry<A, Node<A>> entry: children.entrySet())
            {
                Node<A> child = entry.getValue();
                utility = getNextMode(child, user, opponent, depth + 1,alpha, beta, !isMax);

                if (depth == 0 && utility > maxUtility)
                {
                    nextAction = entry.getKey();
                }
                maxUtility = Math.max(utility, maxUtility);

                alpha = Math.max(alpha, utility);
                if (beta <= alpha) {
                    break;
                }
            }

            return maxUtility;
        }
        else
        {
            double minUtility = Double.POSITIVE_INFINITY;

            Map<A, Node<A>> children = getChild(node, opponent);

            for (Map.Entry<A, Node<A>> entry: children.entrySet())
            {
                Node<A> child = entry.getValue();
                utility = getNextMode(child, user, opponent, depth + 1,alpha, beta, !isMax);

                minUtility = Math.min(utility, minUtility);

                beta = Math.min(beta, utility);
                if (beta <= alpha) {
                    break;
                }
            }

            return minUtility;
        }
    }

    private Map<A, Node<A>> getChild(Node<A> node, User user) {
        List<A> actions = node.gameState.getLegalActions();
        Map<A, Node<A>> children = new HashMap<A, Node<A>>();

        for (A a : actions) {
            GameState<A> nextGameState = node.gameState.getDeepCopy();
            nextGameState.process(a, user);

            Node<A> child = new Node<A>(nextGameState, node);
            children.put(a, child);
        }
        return children;
    }
}
