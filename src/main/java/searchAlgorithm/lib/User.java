package searchAlgorithm.lib;
public enum User {
    NULL,
    ONE,
    TWO,
    COUNT;

    public int toInt() {
        if (this == NULL) {
            return 0;
        } else if (this == ONE) {
            return 1;
        } else if (this == TWO) {
            return 2;
        }
        else if (this == COUNT) {
            return 3;
        }

        return 0;
    }
    public String toString() {
        if (this == NULL) {
            return "NULL";
        } else if (this == ONE) {
            return "ONE";
        } else if (this == TWO) {
            return "TWO";
        }
        return "NULL";
    }

    public static User opposite(User user) {
        if (user == ONE) {
            return TWO;
        } else if (user == TWO) {
            return ONE;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
