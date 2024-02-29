package game.tictactoe.player;

import game.tictactoe.TicTacToeAction;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.Scanner;

public class TicTacToeManualPlayer implements IPlayer {
    private int id;
    private User user;
    private String name;
    public TicTacToeManualPlayer(User user, int id, String name)
    {
        this.id = id;
        this.user = user;
        this.name = name;
    }
    @Override
    public Action getAction(GameState board, User user)
    {
        int row, col = 0;
        boolean isValid = true;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Row: ");
        row = sc.nextInt();
        System.out.println("Enter Col: ");
        col = sc.nextInt();

        return new TicTacToeAction(row, col);
    }

    @Override
    public String getName() {
        return "Player " + name;
    }
    @Override
    public void setUser(User user) { this.user = user;}
    @Override
    public User getUser() { return this.user;}
    @Override
    public int getId() { return id;}
}


