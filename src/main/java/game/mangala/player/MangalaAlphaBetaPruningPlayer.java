package game.mangala.player;

import game.mangala.MangalaAction;
import searchAlgorithm.algorithm.alphaBetaPruning.AlphaBetaPruning;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class MangalaAlphaBetaPruningPlayer implements IPlayer {
    private int id;
    private User user;
    private AlphaBetaPruning<MangalaAction> algorithm;
    public MangalaAlphaBetaPruningPlayer(User user, int id)
    {
        this.id = id;
        this.user = user;
        this.algorithm = new AlphaBetaPruning<MangalaAction>();
    }
    public MangalaAlphaBetaPruningPlayer(User user, int id, int maximumDepth) {
        this.id = id;
        this.user = user;
        this.algorithm = new AlphaBetaPruning<MangalaAction>(maximumDepth);
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
