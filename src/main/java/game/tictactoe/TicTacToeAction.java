package game.tictactoe;
import searchAlgorithm.lib.Action;

public class TicTacToeAction extends Action{

    private int row, col;

    public TicTacToeAction(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TicTacToeAction) {
            TicTacToeAction action = (TicTacToeAction)o;
            return (row == action.row && col == action.col);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return row + " , " + col;
    }
}
