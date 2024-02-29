package game.checkers.player;

import game.checkers.CheckersAction;
import searchAlgorithm.algorithm.minimax.Minimax;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class CheckersMinimaxPlayer implements IPlayer {
    private int id;
    private User user;
    private Minimax<CheckersAction> algorithm;
    public CheckersMinimaxPlayer(User user, int id)
    {
        this.user = user;
        this.id = id;
        this.algorithm = new Minimax<CheckersAction>();
    }
    public CheckersMinimaxPlayer(User user, int id, int maximumDepth) {
        this.user = user;
        this.id = id;
        this.algorithm = new Minimax<CheckersAction>(maximumDepth);
    }
    @Override
    public Action getAction(GameState board, User user) {
        return algorithm.getAction(board, user);
    }
    @Override
    public String getName() {
        return "Minimax Player";
    }
    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}
