package ZeroSumGame;

public enum AlgorithmEnum {
    alphaBeta("α-β Pruning", 0),
    minimax("Minimax", 1),
    mcts("Mcts", 2),
    mctsWithWu("Mcts with Weak Utility", 3),
    expectimax("Expectimax", 4),

    random("Random", 5);
    private int id;
    private String label;
    AlgorithmEnum(String label, int id) {
        this.id = id;
        this.label = label;
    }
    public String toString() {
        return label;
    }

    public String getShortName() {
        if (this.id == 0)
            return "α-β";
        else if (this.id == 1)
            return "MinMax";
        else if (this.id == 2)
            return "Mcts";
        else if (this.id == 3)
            return "MctsWu";
        else if (this.id == 4)
            return "ExpMax";
        else if (this.id == 5)
            return "Rand";
        else
            return "Inv";
    }
    public int toInt() {return id;}
}
