package helpers;

/**
 * Various helper functions for string used in the application.
 *
 * @author Fredrik Kindstrom
 */
public class StringHelper {

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String RESET = "\u001B[0m";

    /**
     * Return whether a string of words contains a word.
     * @param str The string to look in.
     * @param word The word to look for.
     * @return True if the word is by itself in the string, false if not.
     */
    public static boolean containsWord(String str, String word) {
        return
            str.toLowerCase().contains(" " + word + " ") ||
            str.toLowerCase().contains(" " + word + ",") ||
            str.toLowerCase().contains(" " + word + ".");
    }

    /**
     * Returns the first positive number found in a string.
     * If a string contains approximations (4-5) it returns the mean of those numbers;
     * @param str The string to look in.
     * @return A number if found.
     * @throws IllegalArgumentException if no number is found in string.
     */
    public static int parseFirstNumber(String str) {
        for (String word : str.split("\\s+")) {
            if (word.matches("\\d+")) {
                return Integer.parseInt(word);
            } else if (word.matches("[\\d-]+")) {
                String[] split = word.split("-");
                int sum = 0;
                for (String s : split) {
                    sum += Integer.parseInt(s);
                }
                return Math.round(sum / split.length);
            }
        }
        throw new IllegalArgumentException("No numbers in string");
    }
}
