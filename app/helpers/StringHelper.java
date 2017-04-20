package helpers;

/**
 * Created by fredrikkindstrom on 2017-04-20.
 */
public class StringHelper {

    public static boolean containsWord(String s, String word) {
        return
            s.toLowerCase().contains(" " + word + " ") ||
            s.toLowerCase().contains(" " + word + ",") ||
            s.toLowerCase().contains(" " + word + ".");
    }
}
