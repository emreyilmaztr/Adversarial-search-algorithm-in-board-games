package game.checkers;

import searchAlgorithm.lib.ColorEnum;

import java.util.LinkedList;
import java.util.Queue;

public class CheckersStone {

    public StoneType type;
    public boolean isKing;
    public CheckerPoint checkerPoint;
    public CheckersStone()
    {
        this.checkerPoint = new CheckerPoint();

        this.type   = StoneType.NULL;
        this.isKing = false;
    }

    public CheckersStone(CheckersStone cs)
    {
        this.checkerPoint = cs.checkerPoint;

        this.type   = cs.type;
        this.isKing = cs.isKing;
    }

    public CheckersStone(CheckerPoint point, StoneType type, boolean isKing)
    {
        this.checkerPoint = point;

        this.type   = type;
        this.isKing = isKing;
    }

    public CheckersStone(int row, int col, StoneType type)
    {
        this.checkerPoint = new CheckerPoint(row, col);

        this.type   = type;
        this.isKing = false;
    }

    public CheckersStone(int row, int col, StoneType type, boolean isKing)
    {
        this.checkerPoint = new CheckerPoint(row, col);

        this.type   = type;
        this.isKing = isKing;
    }

    public boolean isEmpty()
    {
        return this.type == StoneType.NULL ? true : false;
    }

    @Override
    public String toString() {
        if (type == StoneType.NULL)
            return type.toString();
        else if (type == StoneType.BLACK && isKing == false)
            return ColorEnum.COLOR_RED.toString() + type.toString() + ColorEnum.COLOR_RESET.toString();
            //return type.toString();
        else if (type == StoneType.BLACK && isKing == true)
            return ColorEnum.COLOR_RED.toString() + type.toString().toUpperCase() + ColorEnum.COLOR_RESET.toString();
            //return type.toString().toUpperCase();
        else if (type == StoneType.WHITE && isKing == false)
            return ColorEnum.COLOR_BLUE.toString() + type.toString() + ColorEnum.COLOR_RESET.toString();
            //return type.toString();
        else if (type == StoneType.WHITE && isKing == true)
            return ColorEnum.COLOR_BLUE.toString() + type.toString().toUpperCase() + ColorEnum.COLOR_RESET.toString();
            //return type.toString().toUpperCase();
        else
            return type.toString();
    }

    public int toInt() {
        if (type == StoneType.NULL)
            return 0;
        else if (type == StoneType.BLACK)
            return 1;
        else if (type == StoneType.WHITE)
            return 2;
        else
            return 0;
    }

}

