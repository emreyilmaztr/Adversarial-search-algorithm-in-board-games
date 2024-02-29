package searchAlgorithm.lib;

public enum ColorEnum {
    COLOR_RED,
    COLOR_BLUE,
    COLOR_RESET;

    @Override
    public String toString() {
        if (this == COLOR_RESET) {
            return "\033[0m";
        } else if (this == COLOR_RED) {
            return "\033[0;31m";
        } else if (this == COLOR_BLUE) {
            return "\033[0;34m";
        }
        return "\033[0m";
    }
}
