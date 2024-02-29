package game.tictactoe.player;

import game.tictactoe.TicTacToeAction;
import searchAlgorithm.algorithm.minimax.Minimax;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class TicTacToeMinimaxPlayer implements IPlayer {
    private int id;
    private User user;
    private Minimax<TicTacToeAction> algorithm;
    public TicTacToeMinimaxPlayer(User user, int id)
    {
        this.id = id;
        this.user = user;
        this.algorithm = new Minimax<TicTacToeAction>();
    }
    public TicTacToeMinimaxPlayer(User user, int id, int maximumDepth) {
        this.id = id;
        this.user = user;
        this.algorithm = new Minimax<TicTacToeAction>(maximumDepth);
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
