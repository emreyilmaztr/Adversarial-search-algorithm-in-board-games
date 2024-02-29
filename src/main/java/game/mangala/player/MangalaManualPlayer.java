package game.mangala.player;

import game.mangala.MangalaAction;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.Scanner;

public class MangalaManualPlayer implements IPlayer {

    private int id;
    private User user;
    private String name;
    public MangalaManualPlayer(User user, int id, String name)
    {
        this.user = user;
        this.id = id;
        this.name = name;
    }

    @Override
    public Action getAction(GameState board, User user)
    {
        int row = 0;
        boolean isValid = true;

        Scanner sc = new Scanner(System.in);
        System.out.println("Row: ");
        row = sc.nextInt();
        return new MangalaAction(row);
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


