package parsers;

import helpers.StringHelper;
import models.recipe.Ingredient;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fredrikkindstrom on 2017-04-20.
 */
public class IngredientStringParser {

    private IngredientFinder ingredientFinder = new IngredientFinder();

    private String header;
    private String insideParenthesis;
    private List<String> alternatives = new ArrayList<>();

    public synchronized List<Ingredient> parse(String ingredientString) {
        String line = ingredientString;
        List<Ingredient> ingredients = new ArrayList<>();

        line = handleColon(line).trim();
        line = handleParenthesis(line).trim();
        line = handleAlternatives(line).trim();

        Ingredient ingredient = ingredientFinder.find(line);
        ingredients.add(ingredient);

        return ingredients;
    }

    private String handleColon(String line) {
        if (line.contains(":")) {
            String[] split = line.toLowerCase().split(":");
            String output = split[1];
            header = split[0];
            Logger.trace("Contains colon, added \"" + header + "\" as header");
            return output;
        } else {
            Logger.trace("Contains no colon");
            return line;
        }
    }

    private String handleParenthesis(String line) {
        String[] split = new String[2];
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
        while (m.find()) {
            if (m.group(1) != null) {
                split[1] = m.group(1);
            }
        }
        if (split[1] != null) {
            insideParenthesis = "(" + split[1] + ")";
            Logger.trace("Contains parenthesis, added " + insideParenthesis);
            return line.replaceAll("\\(([^)]+)\\)", "");
        } else {
            Logger.trace("Contains no parenthesis");
            return line;
        }
    }

    private String handleAlternatives(String line) {
        if (StringHelper.containsWord(line, "eller")) {
            String[] split = line.toLowerCase().split(" eller");
            for (int i = 1; i < split.length; i++) {
                alternatives.add(split[i].trim());
                Logger.trace("Contains alternative: " + split[i].trim());
            }
            return split[0];
        } else {
            Logger.trace("Contains no alternatives");
            return line;
        }
    }
}
