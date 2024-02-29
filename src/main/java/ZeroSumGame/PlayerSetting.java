package ZeroSumGame;

import game.checkers.player.*;
import game.mangala.player.*;
import game.tictactoe.player.*;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;

import java.util.Random;

public class PlayerSetting {

    public static IPlayer initializePlayer(GameEnum game, AlgorithmEnum algorithm, User user, Random random, int id, int value)
    {
        switch (game)
        {
            case tictactoe:
            {
                if (algorithm == AlgorithmEnum.alphaBeta)
                    return new TicTacToeAlphaBetaPruningPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.minimax)
                    return new TicTacToeMinimaxPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.mcts)
                    return new TicTacToeMctsPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.mctsWithWu)
                    return new TicTacToeMctsWithWuPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.expectimax)
                    return new TicTacToeExpectimaxPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.random)
                    return new TicTacToeRandomPlayer(user, id, random);
                break;
            }
            case checkers:
            {
                if (algorithm == AlgorithmEnum.alphaBeta)
                    return new CheckersAlphaBetaPruningPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.minimax)
                    return new CheckersMinimaxPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.mcts)
                    return new CheckersMctsPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.mctsWithWu)
                    return new CheckersMctsWithWuPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.expectimax)
                    return new CheckersExpectimaxPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.random)
                    return new CheckersRandomPlayer(user, id, random);
                break;
            }
            case mangala:
            {
                if (algorithm == AlgorithmEnum.alphaBeta)
                    return new MangalaAlphaBetaPruningPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.minimax)
                    return new MangalaMinimaxPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.mcts)
                    return new MangalaMctsPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.mctsWithWu)
                    return new MangalaMctsWithWuPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.expectimax)
                    return new MangalaExpectimaxPlayer(user, id, value);
                else if (algorithm == AlgorithmEnum.random)
                    return new MangalaRandomPlayer(user, id, random);
                break;
            }
            default:
            {
                break;
            }
        }

        return null;
    }
}
