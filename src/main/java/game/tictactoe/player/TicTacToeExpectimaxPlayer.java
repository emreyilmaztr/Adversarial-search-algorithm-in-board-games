package game.tictactoe.player;

import game.tictactoe.TicTacToeAction;
import searchAlgorithm.algorithm.expectimax.Expectimax;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

public class TicTacToeExpectimaxPlayer implements IPlayer {
    private int id;
    private User user;
    private Expectimax<TicTacToeAction> algorithm;
    public TicTacToeExpectimaxPlayer(User user, int id)
    {
        this.id = id;
        this.user = user;
        this.algorithm = new Expectimax<TicTacToeAction>();
    }
    public TicTacToeExpectimaxPlayer(User user, int id, int maximumDepth) {
        this.id = id;
        this.user = user;
        this.algorithm = new Expectimax<TicTacToeAction>(maximumDepth);
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
