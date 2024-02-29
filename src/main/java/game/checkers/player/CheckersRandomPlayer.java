package game.checkers.player;

import game.checkers.CheckersAction;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.List;
import java.util.Random;

public class CheckersRandomPlayer implements IPlayer {
    private int id;
    private User user;
    private Random rand;
    public CheckersRandomPlayer(User user, int id, Random r)
    {
        this.user = user;
        this.rand = r;
        this.id = id;
    }
    @Override
    public Action getAction(GameState board, User user){
        List<CheckersAction> legalMoves = board.getLegalActions();
        int index = rand.nextInt(legalMoves.size());
        return legalMoves.get(index);
    }
    @Override
    public String getName() {
        return "Random Player";
    }

    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}
