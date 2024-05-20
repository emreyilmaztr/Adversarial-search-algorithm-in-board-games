package ZeroSumGame;

import searchAlgorithm.lib.*;

import java.util.HashMap;
import java.util.Map;
public class ZeroSumGame{

    private Map<User, Double> prevScore;
    private int statChangeCounter;
    private boolean isStatChangeLimOver;
    private final int STAT_CHANGE_LIM = 100;
    public ZeroSumGame()
    {
        this.statChangeCounter = 0;
        this.prevScore = new HashMap<>();
        this.prevScore.put(User.ONE, 0.0);
        this.prevScore.put(User.TWO, 0.0);
        this.isStatChangeLimOver = false;
    }
    public Map<User, Double> multiplePlayGame(int numOfIteration, GameState gameState, IPlayer player1, IPlayer player2, IPlayer randPlayer1, IPlayer randPlayer2, int numOfRandAction, boolean verbose)
    {
        User winner1, winner2;
        Map<User, Double> score = new HashMap<User, Double>();
        int player1WinCount = 0, player2WinCount = 0, drawCount = 0;

        for (int iter = 1; iter <= numOfIteration; iter++)
        {
            // Set player user
            player1.setUser(User.ONE);
            player2.setUser(User.TWO);
            winner1 = playGame(gameState, player1, player2, randPlayer1, randPlayer2, numOfRandAction, verbose);

            if (winner1 == User.ONE)
                player1WinCount++;
            else if (winner1 == User.TWO)
                player2WinCount++;
            else
                drawCount++;

            // Reset state.
            gameState.reset();

            // Change player user
            player1.setUser(User.TWO);
            player2.setUser(User.ONE);
            winner2 = playGame(gameState, player2, player1, randPlayer1, randPlayer2, numOfRandAction, verbose);

            if (winner2 == User.TWO)
                player1WinCount++;
            else if (winner2 == User.ONE)
                player2WinCount++;
            else
                drawCount++;

            // Reset state.
            gameState.reset();
        }

        double player1Rate = (double) player1WinCount / (numOfIteration * 2) * 100;
        double player2Rate = (double) player2WinCount / (numOfIteration * 2) * 100;
        double drawRate    = (double) drawCount       / (numOfIteration * 2) * 100;

        score.put(User.NULL, drawRate);
        score.put(User.ONE, player1Rate);
        score.put(User.TWO, player2Rate);

        return score;
    }

    public User playGame(GameState gameState, IPlayer player1, IPlayer player2, IPlayer randPlayer1, IPlayer randPlayer2, int numOfRandAction, boolean verbose)
    {
        // If number of random iteration is greater 0, play n step with random player
        for (int i = 0; i < numOfRandAction; i++)
        {
            playOneStep(gameState, randPlayer1, randPlayer2, verbose, false);
        }

        if (verbose)
        {
            System.out.println("After " + numOfRandAction + " Random Actions");
            printBoard(gameState);
        }

        // play with player1 and player2 respectively
        play(gameState, player1, player2, verbose);

        // When limit is over, end game in a draw
        if ( this.isStatChangeLimOver == true)
            return User.NULL;

        return showResult(gameState, player1, player2, verbose);
    }
    public String getPlayerColor(IPlayer player)
    {
        return player.getUser() == User.ONE ? ColorEnum.COLOR_RED.toString() : ColorEnum.COLOR_BLUE.toString();
    }
    private void takeAction(GameState gameState, IPlayer player, boolean verbose)
    {
        boolean play = true;
        User turn = gameState.getTurn();

        if (verbose)
            System.out.println("# " + getPlayerColor(player) + player.getName() + ColorEnum.COLOR_RESET.toString());

        if ( turn == player.getUser() )
        {
            while (play == true && gameState.isTerminal() == false)
            {
                Action move = player.getAction(gameState, player.getUser());
                gameState.process(move, player.getUser());

                if (verbose)
                    printBoard(gameState);

                // update replay status.
                play = gameState.getReplayStatus();
                if (play == true && verbose == true)
                {
                    System.out.println("# JOKER - " + player.getName());
                }
            }
        }
        else
        {
            System.out.println(turn + " Turn!");
        }
    }
    private void play(GameState gameState, IPlayer player1, IPlayer player2, boolean verbose)
    {
        if (verbose)
        {
            System.out.println("\nPlayer1 : " + getPlayerColor(player1) + player1.getName() + ColorEnum.COLOR_RESET.toString() + " vs " +
                                 "Player2 : " + getPlayerColor(player2) + player2.getName() + ColorEnum.COLOR_RESET.toString() + "\n");
            printBoard(gameState);
            System.out.println("\n  Game is starting...\n");
        }

        // Each iteration reset state.
        this.isStatChangeLimOver = false;
        this.statChangeCounter   = 0;

        while (gameState.isTerminal() == false && this.isStatChangeLimOver == false)
        {
            takeAction(gameState, player1, verbose);

            if (gameState.isTerminal() == false && this.isStatChangeLimOver == false)
            {
                takeAction(gameState, player2, verbose);
            }

            // Compare previous score
            boolean isScoreEqual = compareScore(prevScore, gameState.getUtilityMap());

            if (isScoreEqual == true) {
                this.statChangeCounter++;
            }else {
                this.statChangeCounter = 0;
            }

            // When limit is over, end game in a draw
            if (this.statChangeCounter > STAT_CHANGE_LIM)
            {
                this.isStatChangeLimOver = true;
            }
            // else do nothing.

            // hold and update score
            prevScore = gameState.getUtilityMap();
        }
    }
    public void playOneStep(GameState gameState, IPlayer player1, IPlayer player2, boolean verbose, boolean showTime)
    {
        long start = 0, end = 0;
        if (gameState.isTerminal() == false)
        {
            //start = System.nanoTime();
            start = System.currentTimeMillis();
            takeAction(gameState, player1, verbose);
            //end = System.nanoTime();
            end = System.currentTimeMillis();

            if(showTime)
                System.out.println("Player1: Elapsed Time in milli seconds: "+ (end-start));

            if (gameState.isTerminal() == false)
            {
                //start = System.nanoTime();
                start = System.currentTimeMillis();
                takeAction(gameState, player2, verbose);
                //end = System.nanoTime();
                end = System.currentTimeMillis();

                if(showTime)
                    System.out.println("Player2: Elapsed Time in milli seconds: "+ (end-start));
            }
        }
    }
    private void printBoard(GameState gameState)
    {
        System.out.println(gameState.toString());
    }
    public User showResult(GameState gameState, IPlayer player1, IPlayer player2, boolean verbose)
    {
        Map<User, Double> score = gameState.getUtilityMap();

        Double player1Score = score.get(player1.getUser());
        Double player2Score = score.get(player2.getUser());

        if (player1Score > player2Score)
        {
            if (verbose)
            {
                System.out.println();
                System.out.println("### " + player1.getName() + " WIN ###");
                System.out.println("### " + player1.getName() + " : " + player1Score + " " + player2.getName() + " : " + player2Score);
            }

            return player1.getUser();
        }
        else if (player1Score < player2Score)
        {
            if (verbose)
            {
                System.out.println();
                System.out.println("### " + player2.getName() + " WIN ###");
                System.out.println("### " + player1.getName() + " : " + player1Score + " " + player2.getName() + " : " + player2Score);
            }

            return player2.getUser();
        }
        else
        {
            if (verbose)
            {
                System.out.println();
                System.out.println("### DRAW ###");
                System.out.println("### " + player1.getName() + " : " + player1Score + " " + player2.getName() + " : " + player2Score);
            }

            return User.NULL;
        }
    }

    private boolean compareScore(Map<User, Double> s1, Map<User, Double> s2)
    {
        if ( (s1.get(User.ONE).intValue() == s2.get(User.ONE).intValue() ) && ( s1.get(User.TWO).intValue() == s2.get(User.TWO).intValue() ) )
            return true;

        return false;
    }
}
