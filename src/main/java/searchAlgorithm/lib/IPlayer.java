package searchAlgorithm.lib;

public interface IPlayer<A extends Action> {
    A getAction(GameState<A> board, User user);
    String getName();
    void setUser(User user);
    User getUser();
    int getId();
}
