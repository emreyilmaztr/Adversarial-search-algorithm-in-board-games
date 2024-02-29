package gameStatistic;

import ZeroSumGame.*;
import searchAlgorithm.lib.GameState;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GamePlayThread extends Thread{

    private ZeroSumGame zeroSumGame;
    Map<User, Double> score;
    private Random random;
    private IPlayer player1, player2, randPlayer1, randPlayer2;
    private GameState state;
    private int numOfIteration, numOfRandAction, gamePlayed;
    private boolean verbose;
    private GameEnum game;
    private AlgorithmEnum alg1, alg2;
    public GamePlayThread(GameEnum game, GameState state, AlgorithmEnum alg1, AlgorithmEnum alg2, int numOfIteration, int numOfRandAction, int gamePlayed, boolean verbose)
    {
        // Set Value
        this.game = game;
        this.state = state;
        this.numOfIteration = numOfIteration;
        this.numOfRandAction = numOfRandAction;
        this.gamePlayed = gamePlayed;
        this.verbose = verbose;
        this.alg1 = alg1;
        this.alg2 = alg2;
        this.state.reset();

        // Init object
        this.zeroSumGame = new ZeroSumGame();
        this.score = new HashMap<>();
        this.random = new Random();

        this.player1 = PlayerSetting.initializePlayer(game, alg1, User.ONE, random, 1, gamePlayed);
        this.player2 = PlayerSetting.initializePlayer(game, alg2, User.TWO, random, 2, gamePlayed);

        this.randPlayer1 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.ONE, random, 98, gamePlayed);
        this.randPlayer2 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.TWO, random, 99, gamePlayed);
    }
    @Override
    public void run() {
        this.score = zeroSumGame.multiplePlayGame(numOfIteration, state, player1, player2, randPlayer1, randPlayer2, numOfRandAction, verbose);
        System.out.println(game + " : " + gamePlayed + " game played is completed ");
        System.out.println(alg1.toString() + " : " + score.get(User.ONE) + " - " + alg2.toString() + " : " + score.get(User.TWO) );
    }

    public Map<User, Double> getScore()
    {
        return score;
    }

    public int getGamePlayed()
    {
        return gamePlayed;
    }

}
