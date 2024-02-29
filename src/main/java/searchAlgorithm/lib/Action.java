package searchAlgorithm.lib;

public abstract class Action {
    @Override
    public abstract boolean equals(Object o);
    @Override
    public abstract int hashCode();
    @Override
    public abstract String toString();
}