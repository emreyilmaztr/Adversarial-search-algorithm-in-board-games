package game.checkers;

public enum ActionType {
    MOVE,
    ATTACK;

    public int toInt()
    {
        if (this == MOVE)
            return 0;
        else if(this == ATTACK)
            return 1;
        return 0;
    }
}
