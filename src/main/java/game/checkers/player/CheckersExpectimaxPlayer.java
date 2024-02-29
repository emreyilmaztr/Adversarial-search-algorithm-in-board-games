package game.checkers.player;

import game.checkers.CheckersAction;
import searchAlgorithm.algorithm.expectimax.Expectimax;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class CheckersExpectimaxPlayer implements IPlayer {

    private int id;
    private User user;
    private Expectimax<CheckersAction> algorithm;
    public CheckersExpectimaxPlayer(User user, int id)
    {
        this.user = user;
        this.id = id;
        this.algorithm = new Expectimax<CheckersAction>();
    }
    public CheckersExpectimaxPlayer(User user, int id, int maximumDepth) {
        this.user = user;
        this.id = id;
        this.algorithm = new Expectimax<CheckersAction>(maximumDepth);
    }
    @Override
    public Action getAction(GameState board, User user) {
        return algorithm.getAction(board, user);
    }
    @Override
    public String getName() {
        return "Expectimax Player";
    }
    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}
