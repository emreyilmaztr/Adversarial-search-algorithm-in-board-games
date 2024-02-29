package game.checkers.player;

import game.checkers.*;
import searchAlgorithm.lib.Action;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.Scanner;

public class CheckersManualPlayer implements IPlayer {

    private int id;
    private String name;
    private User user;
    public CheckersManualPlayer(User user, int id, String name)
    {
        this.user = user;
        this.name = name;
        this.id = id;
    }

    @Override
    public Action getAction(GameState board, User user)
    {
        CheckerPoint source = new CheckerPoint();
        CheckerPoint dest = new CheckerPoint();

        boolean isValid = true;

        Scanner sc = new Scanner(System.in);
        System.out.println("Source Row: ");
        source.setRow(sc.nextInt());
        System.out.println("Source Col: ");
        source.setCol(sc.nextInt());
        System.out.println("Destination Row: ");
        dest.setRow(sc.nextInt());
        System.out.println("Destination Col: ");
        dest.setCol(sc.nextInt());

        StoneType type = (user == User.ONE ? StoneType.WHITE : StoneType.BLACK);

        // TBD.
        return new CheckersAction(new CheckersStone(source.getRow(), source.getCol(), type), dest, ActionType.MOVE);
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


