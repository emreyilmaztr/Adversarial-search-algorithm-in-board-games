package ZeroSumGame;

public enum GameEnum {

    tictactoe("Tic-Tac-Toe", 0),
    checkers("Checkers", 1),
    mangala("Mangala", 2);

    private int id;
    private String label;
    GameEnum(String label, int id) {
        this.label = label;
        this.id = id;
    }
    public String toString() {
        return label;
    }
    public int toInt() {
        return id;
    }
}
