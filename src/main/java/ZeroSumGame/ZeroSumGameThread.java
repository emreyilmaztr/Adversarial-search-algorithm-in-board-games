package ZeroSumGame;

import searchAlgorithm.lib.*;

import java.util.HashMap;
import java.util.Map;

public class ZeroSumGameThread extends Thread{

    private ZeroSumGame zeroSumGame;
    private int numOfIteration;
    private GameState gameState;
    private IPlayer player1, player2, randPlayer1, randPlayer2;
    private int numOfRandAction;
    private boolean verbose;
    private Map<User, Double> score;
    public ZeroSumGameThread(int numOfIteration, GameState gameState, IPlayer player1, IPlayer player2, IPlayer randPlayer1, IPlayer randPlayer2, int numOfRandAction, boolean verbose)
    {
        this.zeroSumGame = new ZeroSumGame();
        this.score = new HashMap<User, Double>();

        // Initialize parameter.
        this.numOfIteration  = numOfIteration;
        this.gameState       = gameState;
        this.player1         = player1;
        this.player2         = player2;
        this.randPlayer1     = randPlayer1;
        this.randPlayer2     = randPlayer2;
        this.numOfRandAction = numOfRandAction;
        this.verbose         = verbose;

        // Reset state.
        this.gameState.reset();
    }

    @Override
    public void run()
    {
        this.score = this.zeroSumGame.multiplePlayGame(numOfIteration, gameState, player1, player2, randPlayer1, randPlayer2, numOfRandAction, verbose);
    }

    public Map<User, Double> getScore()
    {
        return score;
    }
}
