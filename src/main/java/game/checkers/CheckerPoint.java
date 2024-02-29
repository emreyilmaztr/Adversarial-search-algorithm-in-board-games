package game.checkers;

public class CheckerPoint {
    private int row, col;
    private int orjDist;

    public CheckerPoint()
    {
        this.row = 0;
        this.col = 0;
        this.orjDist = 0;
    }
    public CheckerPoint(int row, int col)
    {
        this.row = row;
        this.col = col;
        this.orjDist = (int) (Math.sqrt( (this.row * this.row) + (this.col * this.col) ) / 2.0);
    }

    public CheckerPoint(CheckerPoint b)
    {
        this.row = b.row;
        this.col = b.col;
        this.orjDist = (int) (Math.sqrt( (this.row * this.row) + (this.col * this.col) ) / 2.0);
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setCol(int col)
    {
        this.col = col;
    }

    public int getOrjDist(){return this.orjDist;}

    @Override
    public boolean equals(Object o) {
        if (o instanceof CheckersAction) {
            CheckerPoint point = (CheckerPoint)o;
            return (row == point.row && col == point.col);
        }
        return false;
    }

    @Override
    public String toString() {
        return row + "," + col;
    }
}
