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

        int numOfIteration = 1000,  numOfRandAction = 0;
        AlgorithmEnum mctsWithWu = AlgorithmEnum.mctsWithWu;
        AlgorithmEnum mcts = AlgorithmEnum.mcts;
        AlgorithmEnum alphaBeta = AlgorithmEnum.alphaBeta;

        GameEnum game = null;
        GameState state = null;

        int player1Param[] = {100, 500, 1000};
        int player2Param[] = {5, 7, 9};

        // Tic-Tac-Toe
        game= GameEnum.tictactoe;
        state = getState(game, 5);
        create2DimStats(game, state, mctsWithWu, alphaBeta, numOfIteration, numOfRandAction, player1Param, player2Param, false);
        create2DimStats(game, state, mcts, alphaBeta, numOfIteration, numOfRandAction, player1Param, player2Param, false);
        createDimStats(game, state, mcts, mctsWithWu, numOfIteration, numOfRandAction, player1Param, false);

        // Mangala
        game = GameEnum.mangala;
        state = getState(game, 0);
        create2DimStats(game, state, mctsWithWu, alphaBeta, numOfIteration, numOfRandAction, player1Param, player2Param, false);
        create2DimStats(game, state, mcts, alphaBeta, numOfIteration, numOfRandAction, player1Param, player2Param, false);
        createDimStats(game, state, mcts, mctsWithWu, numOfIteration, numOfRandAction, player1Param, false);


        // Checkers
        game = GameEnum.checkers;
        state = getState(game, 0);
        create2DimStats(game, state, mctsWithWu, alphaBeta, numOfIteration, numOfRandAction, player1Param, player2Param, false);
        create2DimStats(game, state, mcts, alphaBeta, numOfIteration, numOfRandAction, player1Param, player2Param, false);
        createDimStats(game, state, mcts, mctsWithWu, numOfIteration, numOfRandAction, player1Param, false);
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

    public static void create2DimStats(GameEnum game, GameState state, AlgorithmEnum alg1, AlgorithmEnum alg2, int numOfIteration, int numOfRandAction, int[] player1Params, int[] player2Params, boolean verbose)
    {
        ZeroSumGame zeroSumGame = new ZeroSumGame();
        Map< Integer, Map<User, Double> > totalScore = new HashMap<>();

        Random random = new Random();
        IPlayer player1, player2, randPlayer1, randPlayer2;

        Map<User, Double> score;

        final int defPairId = 1;
        int pairId = defPairId;
        for (int p1Param: player1Params)
        {
            for (int p2Param: player2Params)
            {
                player1 = PlayerSetting.initializePlayer(game, alg1, User.ONE, random, 1, p1Param);
                player2 = PlayerSetting.initializePlayer(game, alg2, User.TWO, random, 2, p2Param);

                randPlayer1 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.ONE, random, 98, p1Param);
                randPlayer2 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.TWO, random, 99, p2Param);

                score = zeroSumGame.multiplePlayGame(numOfIteration, state, player1, player2, randPlayer1, randPlayer2, numOfRandAction, verbose);

                totalScore.put(pairId, score);

                System.out.println(game + " : " + "Player1: " + p1Param + " Player2: " + p2Param + " game played is completed ");
                System.out.println(alg1.toString() + " : " + score.get(User.ONE) + " - " + alg2.toString() + " : " + score.get(User.TWO) );

                pairId++;
            }
        }

        String fileName;
        switch (game)
        {
            case tictactoe:
            {
                fileName = game + " - " + state.getBoardSize() + "-" + alg1.getShortName() + "-" + alg2.getShortName();
                break;
            }

            case mangala, checkers:
            {
                fileName = game + "-" + alg1.getShortName() + "-" + alg2.getShortName();
                break;
            }

            default:
            {
                fileName = "empty";
                break;
            }
        }
        // end of the switch;

        writeMatrixToExcel(fileName, alg1, alg2, player1Params, player2Params, defPairId, totalScore);
    }

    public static void createDimStats(GameEnum game, GameState state, AlgorithmEnum alg1, AlgorithmEnum alg2, int numOfIteration, int numOfRandAction, int[] playerParams, boolean verbose)
    {
        ZeroSumGame zeroSumGame = new ZeroSumGame();
        Map< Integer, Map<User, Double> > totalScore = new HashMap<>();

        Random random = new Random();
        IPlayer player1, player2, randPlayer1, randPlayer2;

        Map<User, Double> score;

        for (int param: playerParams)
        {
            player1 = PlayerSetting.initializePlayer(game, alg1, User.ONE, random, 1, param);
            player2 = PlayerSetting.initializePlayer(game, alg2, User.TWO, random, 2, param);

            randPlayer1 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.ONE, random, 98, param);
            randPlayer2 = PlayerSetting.initializePlayer(game, AlgorithmEnum.random, User.TWO, random, 99, param);

            score = zeroSumGame.multiplePlayGame(numOfIteration, state, player1, player2, randPlayer1, randPlayer2, numOfRandAction, verbose);

            totalScore.put(param, score);

            System.out.println(game + " : " + param + " game played is completed ");
            System.out.println(alg1.toString() + " : " + score.get(User.ONE) + " - " + alg2.toString() + " : " + score.get(User.TWO) );
        }

        String fileName;
        switch (game)
        {
            case tictactoe:
            {
                fileName = game + " - " + state.getBoardSize() + "-" + alg1.getShortName() + "-" + alg2.getShortName();
                break;
            }

            case mangala, checkers:
            {
                fileName = game + "-" + alg1.getShortName() + "-" + alg2.getShortName();
                break;
            }

            default:
            {
                fileName = "empty";
                break;
            }
        }
        // end of the switch;

        writeVectorToExcel(fileName, alg1, alg2, playerParams, totalScore);
    }

    public static void writeMatrixToExcel(String filename, AlgorithmEnum alg1,  AlgorithmEnum alg2, int[] player1Params, int[] player2Params, int defPairId, Map< Integer, Map<User, Double> > totalScore) {
        Workbook workbook = new Workbook();

        // Obtain the reference of the first worksheet
        Worksheet worksheet = workbook.getWorksheets().get(0);

        // Add Algorithm info
        worksheet.getCells().get("A1").putValue(alg1.toString() + " / " + alg2.toString());

        // Add player1 params
        String player1ParamPosix = "A", player1Param;
        for (int i = 0; i < player1Params.length; i++)
        {
            player1Param = player1ParamPosix + (i + 2);
            worksheet.getCells().get(player1Param).putValue(player1Params[i]);
        }

        // Add player2 params
        char player2ParamPosix = 'B';
        String player2Param;
        for (int i = 0; i < player2Params.length; i++)
        {
            player2Param = String.valueOf( (char) (player2ParamPosix  + i) );
            player2Param = player2Param + 1;
            worksheet.getCells().get(player2Param).putValue(player2Params[i]);
        }

        int scoreRow = 2;
        String scoreCell;
        for (int p1Param: player1Params)
        {
            char scoreCol = 'B';

            for (int p2Param : player2Params)
            {
                // Find cell
                scoreCell = String.valueOf(scoreCol) + scoreRow;
                // Get score
                Map<User, Double> score = totalScore.get(defPairId);
                // Write value
                worksheet.getCells().get(scoreCell).putValue(score.get(User.ONE) + " / " + score.get(User.TWO));

                scoreCol = (char)(scoreCol + 1);

                defPairId = defPairId + 1;
            }
            scoreRow++;
        }

        String writeFilename = "Matrix "+ filename + ".xls";

        // Save the Excel file
        try {
            workbook.save(writeFilename, SaveFormat.XLSX);
            System.out.println("Statistics are written in " + writeFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeVectorToExcel(String filename, AlgorithmEnum alg1,  AlgorithmEnum alg2, int[] playerParams, Map<Integer, Map<User, Double>> totalScore) {
        Workbook workbook = new Workbook();

        // Obtain the reference of the first worksheet
        Worksheet worksheet = workbook.getWorksheets().get(0);

        // Write algorithm info to cells
        worksheet.getCells().get("B1").putValue(alg1.toString());
        worksheet.getCells().get("C1").putValue(alg2.toString());

        int startRow = 2;
        String paramPosix = "A", alg1Posix = "B", alg2Posix = "C", paramCell, alg1Cell, alg2Cell;

        for (int param: playerParams)
        {
            paramCell = paramPosix + startRow;
            alg1Cell  = alg1Posix  + startRow;
            alg2Cell  = alg2Posix  + startRow;

            Map<User, Double> score = totalScore.get(param);

            worksheet.getCells().get(paramCell).putValue(param);
            worksheet.getCells().get(alg1Cell).putValue(score.get(User.ONE));
            worksheet.getCells().get(alg2Cell).putValue(score.get(User.TWO));

            startRow = startRow + 1;
        }

        String writeFilename = "Vector " + filename + ".xls";

        // Save the Excel file
        try {
            workbook.save(writeFilename, SaveFormat.XLSX);
            System.out.println("Statistics are written in " + writeFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}