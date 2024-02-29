package game.checkers.player;

import game.checkers.CheckersAction;
import searchAlgorithm.algorithm.alphaBetaPruning.AlphaBetaPruning;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class CheckersAlphaBetaPruningPlayer implements IPlayer {

    private int id;
    private User user;
    private AlphaBetaPruning<CheckersAction> algorithm;
    public CheckersAlphaBetaPruningPlayer(User user, int id)
    {
        this.user = user;
        this.id = id;
        this.algorithm = new AlphaBetaPruning<CheckersAction>();
    }
    public CheckersAlphaBetaPruningPlayer(User user, int id, int maximumDepth) {
        this.user = user;
        this.id = id;
        this.algorithm = new AlphaBetaPruning<CheckersAction>(maximumDepth);
    }
    @Override
    public Action getAction(GameState board, User user) {
        return algorithm.getAction(board, user);
    }
    @Override
    public String getName() {
        return "AlphaBetaPruning Player";
    }
    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}
