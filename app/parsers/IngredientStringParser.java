package parsers;

import helpers.StringHelper;
import models.food.FoodGroup;
import models.recipe.Amount;
import models.recipe.Ingredient;
import play.Logger;
import play.libs.ws.WSClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fredrikkindstrom on 2017-04-20.
 */
public class IngredientStringParser {

    private IngredientFinder ingredientFinder;

    private String header;
    private String insideParenthesis;
    private List<String> alternatives = new ArrayList<>();

    /**
     * Use this only when there is no external list of all {@link FoodGroup}s already created.
     * Constructs by first fetching all {@link FoodGroup}s from the database.
     * @param wsClient The web service client to use when calling Json Tagger API.
     */
    public IngredientStringParser(WSClient wsClient) {
        List<FoodGroup> foodGroupList = FoodGroup.find.select("searchTags").findList();
        ingredientFinder = new IngredientFinder(wsClient, foodGroupList);
    }

    /**
     * * Constructs using the provided list of all {@link FoodGroup}s.
     * @param wsClient The web service client to use when calling Json Tagger API.
     * @param foodGroupList A list of all {@link FoodGroup}s in the database.
     */
    public IngredientStringParser(WSClient wsClient, List<FoodGroup> foodGroupList) {
        ingredientFinder = new IngredientFinder(wsClient, foodGroupList);
    }

    /**
     * Parses a string and tries to find an ingredient with amount in it.
     * @param ingredientString The string to parseHtml.
     * @return An ingredient if found or null otherwise.
     * @throws IOException If the external API used to parseHtml cannot be reached.
     */
    public synchronized Ingredient parse(String ingredientString) throws IOException {
        String line = ingredientString;

        line = handleColon(line).trim();
        line = handleParenthesis(line).trim();
        //TODO disable this functionality for now
        //line = handleAlternatives(line).trim();

        Ingredient ingredient = ingredientFinder.find(line);

        if (ingredient != null) {
            return ingredient;
        } else {
            if (!alternatives.isEmpty()) {
                for (String alternative : alternatives) {
                    Ingredient alt = ingredientFinder.find(alternative);
                    if (alt != null && alt.getAmount().getUnit().getType().equals(Amount.Unit.Type.EMPTY)) {
                        Amount amount = ingredientFinder.extractAmount(line);
                        if (amount != null) {
                            return ingredientFinder.find(alternative, amount);
                        }
                    }
                }
            }
        }
        Logger.error("No ingredient found \"" + ingredientString + "\"");
        return null;
    }

    private String handleColon(String line) {
        if (line.contains(":")) {
            String[] split = line.toLowerCase().split(":");
            String output = split[1];
            header = split[0];
            Logger.trace("Contains colon, added \"" + header + "\" as header");
            return output;
        } else {
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
            return line;
        }
    }
}
