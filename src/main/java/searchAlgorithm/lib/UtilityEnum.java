package searchAlgorithm.lib;

public enum UtilityEnum {
    UTILITY_1("UTILITY_1", 0),
    UTILITY_2("UTILITY_2", 1),
    UTILITY_3("UTILITY_3", 2);

    private int id;
    private String label;
    UtilityEnum(String label, int id) {
        this.label = label;
        this.id = id;
    }
    public String toString() {
        return label;
    }
    public int toInt() {
        return id;
    }
}
