package core.tools;

public class Tools {
    public Tools() {
    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
