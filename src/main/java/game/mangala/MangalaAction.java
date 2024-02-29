package game.mangala;

import searchAlgorithm.lib.Action;

public class MangalaAction extends Action {
    private int index;
    public MangalaAction(int index)
    {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }
}
