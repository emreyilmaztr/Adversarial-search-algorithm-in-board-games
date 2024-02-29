package searchAlgorithm.algorithm.minimax;

import searchAlgorithm.lib.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Minimax <A extends Action> implements ISearchAlgorithm {

    private Node<A> rootNode;
    public int maximumDepth;
    private A nextAction;

    public Minimax()
    {
        this.maximumDepth = Integer.MAX_VALUE;
    }

    public Minimax(int maximumDepth)
    {
        this.maximumDepth = maximumDepth;
    }


    @Override
    public A getAction(GameState root, User user)
    {
        nextAction = null;
        rootNode = new Node<A>(root, null);

        User opponent = (user == User.ONE? User.TWO : User.ONE);
        double utility = getNextMode(rootNode, user, opponent, 0, true);

        return nextAction;
    }

    private double getNextMode(Node<A> node, User user, User opponent, int depth, boolean isMax)
    {
        double utility = 0;

        double currentUtility = node.gameState.getUtility(user);

        if (depth == maximumDepth || node.gameState.isTerminal())
        {
            currentUtility = node.gameState.getUtility(user);
            return currentUtility;
        }

        if (isMax)
        {
            double maxUtility = Double.NEGATIVE_INFINITY;
            Map<A, Node<A>> children = getChild(node, user);

            for (Map.Entry<A, Node<A>> entry: children.entrySet())
            {
                Node<A> child = entry.getValue();
                utility = getNextMode(child, user, opponent, depth + 1, !isMax);

                if (depth == 0 && utility > maxUtility)
                {
                    nextAction = entry.getKey();
                }
                maxUtility = Math.max(utility, maxUtility);
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
                utility = getNextMode(child, user, opponent, depth + 1, !isMax);

                minUtility = Math.min(utility, minUtility);

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
