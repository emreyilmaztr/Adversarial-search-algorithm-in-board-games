package gameStatistic;

import ZeroSumGame.*;
import com.aspose.cells.*;
import game.checkers.CheckersGameState;
import game.mangala.MangalaGameState;
import game.tictactoe.TicTacToeGameState;
import searchAlgorithm.lib.GameState;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;

import java.util.*;

public class GameStatistic{

    public static void main(String[] args) {

        int numOfIteration = 1000,  numOfRandAction = 0, gamePlayedInit = 100,  gamePlayedLimit = 2000, gamePlayedStep = 100, maxDepth = 5;
        AlgorithmEnum alg1 = AlgorithmEnum.mcts;
        AlgorithmEnum alg2 = AlgorithmEnum.mctsWithWu;
        GameEnum game = null;
        GameState state = null;

        // Tic-Tac-Toe
        game= GameEnum.tictactoe;
        state = getState(game, 5);
        getGameStatistic(game, state, alg1, alg2, numOfIteration, numOfRandAction, maxDepth, gamePlayedInit, gamePlayedLimit, gamePlayedStep, false);

        // Checkers
        game = GameEnum.checkers;
        state = getState(game, 0);
        getGameStatistic(game, state, alg1, alg2, numOfIteration, numOfRandAction, maxDepth, gamePlayedInit, gamePlayedLimit, gamePlayedStep, false);

        // Mangala
        game = GameEnum.mangala;
        state = getState(game, 0);
        getGameStatistic(game, state, alg1, alg2, numOfIteration, numOfRandAction, maxDepth, gamePlayedInit, gamePlayedLimit, gamePlayedStep, false);

    }

    public static GameState getState(GameEnum game, int boardSize)
    {
        GameState state = null;
        switch (game)
        {
            case tictactoe:
                state = new TicTacToeGameState(boardSize);
                break;
            case checkers:
                state = new CheckersGameState();
                break;
            case mangala:
                state = new MangalaGameState();
                break;
        }
        // end of the case.
        return state;
    }

    public static void debugGame(GameEnum game, GameState state, AlgorithmEnum alg1, AlgorithmEnum alg2, int numOfRandAction, int gamePlayed)
    {
        ZeroSumGame zeroSumGame = new ZeroSumGame();
        Map<Integer, Map<User, Double>> totalScore = new HashMap<>();

        Random random = new Random();
        IPlayer player1, player2, randPlayer1, randPlayer2;

        player1 = PlayerSetting.initializePlayer(game, alg1, User.ONE, random, 1, gamePlayed);
        player2 = PlayerSetting.initializePlayer(game, alg2, User.TWO, random, 2, gamePlayed);

        randPlayer1 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.ONE, random, 98, gamePlayed);
        randPlayer2 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.TWO, random, 99, gamePlayed);

        User winner = zeroSumGame.playGame(state, player1, player2,randPlayer1, randPlayer2, numOfRandAction, true);

        System.out.println("Winner: " + winner);
    }

    public static void playGameWithThread(GameEnum game, int boardSize, AlgorithmEnum alg1, AlgorithmEnum alg2, int numOfIteration, int numOfRandAction, int maxDepth, int gamePlayed, int gamePlayedLimit, int gamePlayedStep, boolean verbose)
    {
        Map<Integer, Map<User, Double>> totalScore = new HashMap<>();
        List<GamePlayThread> threadList = new ArrayList<>();
        for (int gp = gamePlayed; gp <= gamePlayedLimit; gp = gp + gamePlayedStep)
        {
            threadList.add(new GamePlayThread(game, getState(game, boardSize), alg1, alg2, numOfIteration, numOfRandAction, gp, verbose) );
        }

        // Start all thred
        for(GamePlayThread thread : threadList)
        {
            thread.start();
        }

        // Wait until all threads are finished
        for(GamePlayThread thread : threadList)
        {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Get result
        for(GamePlayThread thread : threadList)
        {
            totalScore.put(thread.getGamePlayed(), thread.getScore());
        }

        // Parse filename
        String fileName;
        switch (game)
        {
            case tictactoe:
            {
                fileName = game.toString() + " - " + boardSize;
                break;
            }

            case mangala, checkers:
            {
                fileName = game.toString();
                break;
            }

            default:
            {
                fileName = "empty";
                break;
            }
        }
        // end of the switch;earth wall

        // Write to excel file.
        writeToExcel(fileName, alg1, alg2, gamePlayedStep, gamePlayedStep, totalScore);
    }

    public static void getGameStatistic(GameEnum game, GameState state, AlgorithmEnum alg1, AlgorithmEnum alg2, int numOfIteration, int numOfRandAction, int maxDepth, int gamePlayed, int gamePlayedLimit, int gamePlayedStep, boolean verbose)
    {
        ZeroSumGame zeroSumGame = new ZeroSumGame();
        Map<Integer, Map<User, Double>> totalScore = new HashMap<>();

        Random random = new Random();
        IPlayer player1, player2, randPlayer1, randPlayer2;

        Map<User, Double> score;

        for (int gp = gamePlayed; gp <= gamePlayedLimit; gp = gp + gamePlayedStep)
        {
            if (alg1 == AlgorithmEnum.mcts || alg1 == AlgorithmEnum.mctsWithWu)
                player1 = PlayerSetting.initializePlayer(game, alg1, User.ONE, random, 1, gamePlayed);
            else
                player1 = PlayerSetting.initializePlayer(game, alg1, User.ONE, random, 1, maxDepth);

            if (alg2 == AlgorithmEnum.mcts || alg2 == AlgorithmEnum.mctsWithWu)
                player2 = PlayerSetting.initializePlayer(game, alg2, User.TWO, random, 2, gamePlayed);
            else
                player2 = PlayerSetting.initializePlayer(game, alg2, User.TWO, random, 2, maxDepth);

            randPlayer1 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.ONE, random, 98, gamePlayed);
            randPlayer2 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.TWO, random, 99, gamePlayed);

            score = zeroSumGame.multiplePlayGame(numOfIteration, state, player1, player2, randPlayer1, randPlayer2, numOfRandAction, verbose);

            totalScore.put(gp, score);

            System.out.println(game + " : " + gp + " game played is completed ");
            System.out.println(alg1.toString() + " : " + score.get(User.ONE) + " - " + alg2.toString() + " : " + score.get(User.TWO) );
        }

        String fileName;
        switch (game)
        {
            case tictactoe:
            {
                fileName = game.toString() + " - " + state.getBoardSize() + "-" + alg1.getShortName() + "-" + alg2.getShortName();
                break;
            }

            case mangala, checkers:
            {
                fileName = game.toString() + "-" + alg1.getShortName() + "-" + alg2.getShortName();
                break;
            }

            default:
            {
                fileName = "empty";
                break;
            }
        }
        // end of the switch;

        writeToExcel(fileName, alg1, alg2, gamePlayed, gamePlayedStep, totalScore);
    }

    public static void writeToExcel(String filename, AlgorithmEnum alg1,  AlgorithmEnum alg2, int gp, int gpStep, Map<Integer, Map<User, Double>> totalScore) {
        Workbook workbook = new Workbook();

        // Obtain the reference of the first worksheet
        Worksheet worksheet = workbook.getWorksheets().get(0);

        // Add sample values to cells
        worksheet.getCells().get("B1").putValue(alg1.toString());
        worksheet.getCells().get("C1").putValue(alg2.toString());

        final String gpPosix = "A", alg1Posix = "B", alg2Posix = "C";
        String gpCol, alg1Col, alg2Col;

        String endCol = alg2Posix + (totalScore.size() + 1);

        int gamePlayed = gp;

        for (int i = 0; i < totalScore.size(); i++)
        {
            gpCol   = gpPosix   + (i + 2);
            alg1Col = alg1Posix + (i + 2);
            alg2Col = alg2Posix + (i + 2);

            Map<User, Double> score = totalScore.get(gamePlayed);

            worksheet.getCells().get(gpCol).putValue(gamePlayed);
            worksheet.getCells().get(alg1Col).putValue(score.get(User.ONE));
            worksheet.getCells().get(alg2Col).putValue(score.get(User.TWO));

            gamePlayed = gamePlayed + gpStep;
        }


        // Add a chart to the worksheet
        int chartIndex = worksheet.getCharts().add(ChartType.LINE, 5, 0, 15, 5);

        // Access the instance of the newly added chart
        Chart chart = worksheet.getCharts().get(chartIndex);

        // Set chart data source as the range "A1:C4"
        chart.setChartDataRange("A1:" + endCol, true);

        String writeFilename = filename + ".xls";

        // Save the Excel file
        try {
            workbook.save(writeFilename, SaveFormat.XLSX);
            System.out.println("Statistics are written in " + writeFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}