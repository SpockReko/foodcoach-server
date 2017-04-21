package helpers;

/**
 * Created by fredrikkindstrom on 2017-04-20.
 */
public class StringHelper {

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String RESET = "\u001B[0m";

    public static boolean containsWord(String s, String word) {
        return
            s.toLowerCase().contains(" " + word + " ") ||
            s.toLowerCase().contains(" " + word + ",") ||
            s.toLowerCase().contains(" " + word + ".");
    }
}
