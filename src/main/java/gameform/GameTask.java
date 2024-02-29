package gameform;

import searchAlgorithm.lib.GameState;
import searchAlgorithm.lib.User;
import javafx.concurrent.Task;
import searchAlgorithm.lib.IPlayer;
import ZeroSumGame.ZeroSumGame;

public class GameTask extends Task<String> {
    private final int totalIterations;
    private final IPlayer player1, player2, randomPlayer1, randomPlayer2;
    private final GameState gameState;
    private final ZeroSumGame game;
    private int player1WinCount, player2WinCount, drawCount, numOfRandAction;
    private final boolean verbose;

    public GameTask(int totalIterations, IPlayer player1, IPlayer player2, IPlayer randomPlayer1, IPlayer randomPlayer2, GameState gameState, int numOfRandAction, boolean verbose) {
        this.totalIterations = totalIterations;
        this.player1 = player1;
        this.player2 = player2;
        this.randomPlayer1 = randomPlayer1;
        this.randomPlayer2 = randomPlayer2;
        this.gameState = gameState;
        this.verbose = verbose;
        this.numOfRandAction = numOfRandAction;

        // Create game
        this.game = new ZeroSumGame();
    }

    @Override
    protected String call(){

        User winner1, winner2;

        for (int iterations = 1; iterations <= totalIterations; iterations++)
        {
            // Play changing player side
            player1.setUser(User.ONE);
            player2.setUser(User.TWO);
            winner1 = this.game.playGame(this.gameState, this.player1, this.player2, this.randomPlayer1, this.randomPlayer2,this.numOfRandAction, this.verbose);

            if (winner1 == User.ONE)
                player1WinCount++;
            else if (winner1 == User.TWO)
                player2WinCount++;
            else
                drawCount++;

            // Reset state.
            this.gameState.reset();

            // Play changing player side
            player1.setUser(User.TWO);
            player2.setUser(User.ONE);
            winner2 = this.game.playGame(this.gameState, this.player2, this.player1, this.randomPlayer1, this.randomPlayer2,this.numOfRandAction, this.verbose);

            if (winner2 == User.TWO)
                player1WinCount++;
            else if (winner2 == User.ONE)
                player2WinCount++;
            else
                drawCount++;

            updateProgress(iterations, totalIterations);

            // Reset state.
            this.gameState.reset();
        }

        double player1Rate = (double) player1WinCount / (totalIterations * 2) * 100;
        double player2Rate = (double) player2WinCount / (totalIterations * 2) * 100;
        double drawRate    = (double) drawCount / (totalIterations * 2) * 100;

        String player1Str = player1.getName() + " : %" + player1Rate;
        String player2Str = player2.getName() + " : %" + player2Rate;
        String ans = player1Str + '\n' + player2Str + '\n' + "Draw : %" + drawRate;

        updateMessage(ans);

        return ans;
    }





}
