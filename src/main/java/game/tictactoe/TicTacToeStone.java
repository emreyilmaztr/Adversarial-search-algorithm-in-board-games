package game.tictactoe;

import game.checkers.StoneType;
import searchAlgorithm.lib.ColorEnum;

public enum TicTacToeStone {
    NULL,
    X,
    O;
    public static TicTacToeStone opposite(TicTacToeStone stone) {
        if (stone == X) {
            return O;
        } else if (stone == O) {
            return X;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEmpty()
    {
        if (this == NULL)
            return true;
        return false;
    }

    public int toInt() {
        if (this == NULL) {
            return 0;
        } else if (this == X) {
            return 1;
        } else if (this == O) {
            return 2;
        }
        return 0;
    }
    public String toString() {
        if (this == NULL) {
            return "-";
        } else if (this == X) {
            //return ColorEnum.COLOR_RED.toString() + "X" + ColorEnum.COLOR_RESET.toString();
            return "X";
        } else if (this == O) {
            //return ColorEnum.COLOR_BLUE.toString() + "O" + ColorEnum.COLOR_RESET.toString();
            return "O";
        }
        return "XXX";
    }

    public static TicTacToeStone getStone(int integer) {
        if (integer == 0) {
            return NULL;
        } else if (integer == 1) {
            return X;
        } else if (integer == 2) {
            return O;
        }
        return NULL;
    }

}

