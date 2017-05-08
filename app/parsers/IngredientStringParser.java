package parsers;

import helpers.StringHelper;
import models.food.Food;
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

    private final IngredientFinder ingredientFinder;

    private String header;
    private String parenthesis;
    private final List<String> alternativeStrings = new ArrayList<>();

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
     * @param input The string to parse.
     * @return An ingredient if found or null otherwise.
     * @throws IOException If the external API used to parse cannot be reached.
     */
    public Ingredient parse(String input) throws IOException {
        header = "";
        parenthesis = "";
        alternativeStrings.clear();
        String line = input;

        line = handleColon(line).trim();
        line = handleParenthesis(line).trim();
        line = handleAlternatives(line).trim();

        Ingredient ingredient = ingredientFinder.find(line);

        if (ingredient != null) {
            finishUp(ingredient, input);
            if (!alternativeStrings.isEmpty()) {
                List<Ingredient> alternatives = findAlternatives();
                Amount amount = new Amount(0.0, Amount.Unit.EMPTY);
                // Find at least one amount to use
                for (Ingredient ing : alternatives) {
                    if (ing.hasAmount()) {
                        amount = ing.getAmount();
                        break;
                    }
                }
                if (amount.isEmpty() && ingredient.hasAmount()) {
                    amount = ingredient.getAmount();
                }
                // Set that amount to all alternatives
                for (Ingredient ing : alternatives) {
                    if (!ing.hasAmount()) {
                        ing.setAmount(amount);
                    }
                }
                // Remove if alternative is same food as main
                Food main = ingredient.getFood();
                alternatives.removeIf(i -> i.getFood().equals(main));
                // Also set that amount if the main ingredient doesn't have one
                if (!ingredient.hasAmount()) {
                    ingredient.setAmount(amount);
                }
                if (!alternatives.isEmpty()) {
                    ingredient.alternatives = alternatives;
                }
            }
            return ingredient;
        } else if (!alternativeStrings.isEmpty()) {
            List<Ingredient> alternatives = findAlternatives();
            Amount amount = new Amount(0.0, Amount.Unit.EMPTY);
            FoodGroup group = null;
            // Find at least one amount and food group to use
            for (Ingredient ing : alternatives) {
                if (group == null) {
                    group = ing.getFood().group;
                }
                if (ing.hasAmount()) {
                    amount = ing.getAmount();
                    break;
                }
            }
            ingredient = ingredientFinder.findInGroup(line, group);
            if (ingredient == null) {
                Logger.error("No ingredient found \"" + input + "\"");
                return null;
            }
            // Also set that amount if the main ingredient doesn't have one
            if (!amount.isEmpty()) {
                if (!ingredient.hasAmount()) {
                    ingredient.setAmount(amount);
                }
                // Set that amount to all alternatives
                for (Ingredient ing : alternatives) {
                    if (!ing.hasAmount()) {
                        ing.setAmount(amount);
                    }
                }
            } else if (ingredient.hasAmount()) {
                // Set that amount to all alternatives
                for (Ingredient ing : alternatives) {
                    if (!ing.hasAmount()) {
                        ing.setAmount(ingredient.getAmount());
                    }
                }
            }
            // Remove if alternative is same food as main
            Food main = ingredient.getFood();
            alternatives.removeIf(i -> i.getFood().equals(main));
            if (!alternatives.isEmpty()) {
                ingredient.alternatives = alternatives;
            }
            finishUp(ingredient, input);
            return ingredient;
        }
        Logger.error("No ingredient found \"" + input + "\"");
        return null;
    }

    private String handleColon(String line) {
        String[] split = line.toLowerCase().split(":");
        if (split.length > 1) {
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
            parenthesis = "(" + split[1] + ")";
            Logger.trace("Contains parenthesis, added " + parenthesis);
            return line.replaceAll("\\(([^)]+)\\)", "");
        } else {
            return line;
        }
    }

    private String handleAlternatives(String line) {
        if (StringHelper.containsWord(line, "eller")) {
            String[] split = line.toLowerCase().split(" eller");
            for (int i = 1; i < split.length; i++) {
                alternativeStrings.add(split[i].trim());
                Logger.trace("Contains alternative: " + split[i].trim());
            }
            return split[0];
        } else {
            return line;
        }
    }

    private List<Ingredient> findAlternatives() throws IOException {
        List<Ingredient> list = new ArrayList<>();
        for (String str : alternativeStrings) {
            Ingredient alternative = ingredientFinder.find(str);
            if (alternative != null) {
                list.add(alternative);
            }
        }
        return list;
    }

    private void finishUp(Ingredient ingredient, String input) {
        ingredient.original = input;
        if (!parenthesis.equals("")) {
            if (ingredient.comment != null) {
                ingredient.comment += " " + parenthesis;
            } else {
                ingredient.comment = parenthesis;
            }
        }
        if (ingredient.comment != null) {
            ingredient.comment = StringHelper.clean(ingredient.comment);
        }
    }
}
