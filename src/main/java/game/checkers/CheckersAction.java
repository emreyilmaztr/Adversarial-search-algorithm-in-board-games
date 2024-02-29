package game.checkers;
import searchAlgorithm.lib.Action;

public class CheckersAction extends Action{

    private ActionType actionType;
    private CheckersStone sourceStone;
    private CheckerPoint destPoint, delPoint;

    public CheckersAction()
    {
        this.sourceStone = new CheckersStone();
        this.destPoint   = new CheckerPoint(2,2);
        this.actionType  = ActionType.MOVE;
        this.delPoint    = null;
    }

    public CheckersAction(CheckersStone stone, CheckerPoint dest, ActionType actionType)
    {
        this.sourceStone = stone;
        this.destPoint   = dest;
        this.actionType  = actionType;
        this.delPoint    = null;
    }

    public void addDeletedPoint(CheckerPoint checkerPoint)
    {
        delPoint = checkerPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CheckersAction) {
            CheckersAction action = (CheckersAction)o;
            return (sourceStone == action.sourceStone && destPoint == action.destPoint && actionType == action.actionType);
        }
        return false;
    }
    public CheckersStone getSourceStone() {return sourceStone;}
    public CheckerPoint getDestPoint() {return destPoint;}
    public CheckerPoint getDeletedPoint() {return delPoint;}
    public ActionType getActionType() {return actionType;}
    @Override
    public int hashCode() {return 0;}
    @Override
    public String toString() {return sourceStone.checkerPoint.toString() + "->" + destPoint.toString();}
}