package game.tictactoe.player;

import game.tictactoe.TicTacToeAction;
import searchAlgorithm.algorithm.mctswithweakutility.MctsWithWu;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.Random;

public class TicTacToeMctsWithWuPlayer implements IPlayer {

    private int id;
    private User user;
    private MctsWithWu<TicTacToeAction> mctsWu;

    public TicTacToeMctsWithWuPlayer(User user, int id, int timelimit) {
        this.mctsWu = new MctsWithWu<TicTacToeAction>();
        this.mctsWu.setTimeLimit(timelimit);
        this.mctsWu.setExpandThreshold(1);
        this.user = user;
        this.id = id;
    }

    @Override
    public Action getAction(GameState gameState, User user) {
        return mctsWu.getAction(gameState, user);
    }

    @Override
    public String getName() {
        return "MCTS With Weak Utility Player";
    }

    public void setExpandThreshold(int threshold) {
        mctsWu.setExpandThreshold(threshold);
    }

    public void setTimeLimit(long l) {
        mctsWu.setTimeLimit(l);
    }

    public void setC(double c) {
        mctsWu.setC(c);
    }

    public void setRand(Random r) {
        mctsWu.setRand(r);
    }

    public void setVerbose(boolean v) {
        mctsWu.setVerbose(v);
    }

    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}
