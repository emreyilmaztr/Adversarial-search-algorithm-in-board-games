package game.mangala.player;

import game.mangala.MangalaAction;
import searchAlgorithm.algorithm.minimax.Minimax;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class MangalaMinimaxPlayer implements IPlayer {
    private int id;
    private User user;
    private Minimax<MangalaAction> algorithm;
    public MangalaMinimaxPlayer(User user, int id)
    {
        this.user = user;
        this.id = id;
        this.algorithm = new Minimax<MangalaAction>();
    }
    public MangalaMinimaxPlayer(User user, int id, int maximumDepth) {
        this.user = user;
        this.id = id;
        this.algorithm = new Minimax<MangalaAction>(maximumDepth);
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
