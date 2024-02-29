package game.mangala.player;

import game.mangala.MangalaAction;
import searchAlgorithm.algorithm.mctswithweakutility.MctsWithWu;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.Random;

public class MangalaMctsWithWuPlayer implements IPlayer {
    private int id;
    private User user;
    private MctsWithWu<MangalaAction> mcts;
    public MangalaMctsWithWuPlayer(User user, int id, int timelimit) {
        this.mcts = new MctsWithWu<MangalaAction>();
        this.mcts.setTimeLimit(timelimit);
        this.mcts.setExpandThreshold(1);
        this.user = user;
        this.id = id;
    }

    @Override
    public Action getAction(GameState gameState, User user) {
        return mcts.getAction(gameState, user);
    }

    @Override
    public String getName() {
        return "MCTS With Weak Utility Player";
    }

    public void setExpandThreshold(int threshold) {
        mcts.setExpandThreshold(threshold);
    }

    public void setTimeLimit(long l) {
        mcts.setTimeLimit(l);
    }

    public void setC(double c) {
        mcts.setC(c);
    }

    public void setRand(Random r) {
        mcts.setRand(r);
    }

    public void setVerbose(boolean v) {
        mcts.setVerbose(v);
    }

    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}
