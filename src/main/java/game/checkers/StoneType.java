package game.checkers;

public enum StoneType {
    NULL,
    BLACK,
    WHITE;

    @Override
    public String toString()
    {
        if (this == NULL)
            return ".";
        else if (this == BLACK)
            return "b";
        else if (this == WHITE)
            return "w";
        else
            return ".";
    }

    public int toInt()
    {
        if (this == NULL)
            return 0;
        else if (this == BLACK)
            return 1;
        else if (this == WHITE)
            return 2;
        else
            return 0;
    }
}
