package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonHelper;
import helpers.TaggedWord;
import models.food.FoodItem;
import models.food.fineli.Food;
import models.food.fineli.FoodGeneral;
import models.food.fineli.Nutrient;
import models.recipe.Amount;
import models.recipe.Ingredient;
import play.Logger;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by emmafahlen on 2017-03-23.
 */
public class IngredientParser {

    private List<TaggedWord> taggedWords;
    private String leftover = "";
    private String insideParenthesis = "";

    public Ingredient parse(String webString) {

        String[] parenthesis = extractParenthesis(webString);
        String line = parenthesis[0];
        if (parenthesis[1] != null) {
            insideParenthesis = parenthesis[1];
        } else {
            insideParenthesis = "";
        }

        try {
            JsonNode jsonNode = retrieveWordInfo(line);
            taggedWords = JsonHelper.getTaggedWords(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Ingredient ingredient;
        try {
            ingredient = findIngredient();
        } catch (IngredientNotFoundException e) {
            Logger.error("No match \"" + webString + "\"");
            return null;
        }

        return ingredient;
    }

    private Ingredient findIngredient() throws IngredientNotFoundException {
        Amount amount = findAmount();
        Food food = findFoodGeneral().defaultFood;

        if (food != null) {
            if (!leftover.isEmpty() && !leftover.matches("[ -.,:]*")) {
                String comment = leftover.replaceAll("\\s+(?=[),])|\\s{2,}", "");
                comment += insideParenthesis;
                Logger.trace("Added " + comment.trim() + " as comment");
                Logger.info("Ingredient { " + amount.getAmount() + ", " +
                    amount.getUnit() + ", " + food.name + ", \"" + comment.trim() + "\" }");
                return new Ingredient(food, amount, comment.trim());
            } else {
                Logger.info("Ingredient { " +
                    amount.getAmount() + ", " + amount.getUnit() + ", " + food.name + " }");
                return new Ingredient(food, amount);
            }
        } else {
            throw new IngredientNotFoundException();
        }
    }

    private String[] extractParenthesis(String input) {
        String[] split = new String[2];
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(input);
        while(m.find()) {
            if (m.group(1) != null) {
                split[1] = m.group(1);
            }
        }
        split[0] = input.replaceAll("\\(([^)]+)\\)", "");
        return split;
    }

    private Amount findAmount() {
        List<TaggedWord> filteredWords = new ArrayList<>(taggedWords);
        Double numeric = null;
        Amount.Unit unit = null;

        for (TaggedWord taggedWord : taggedWords) {
            for (Amount.Unit u : Amount.Unit.values()) {
                for (String identifier : u.getIdentifiers()) {
                    if (identifier.equals(taggedWord.getLemma())) {
                        if (unit == null) {
                            unit = u;
                            filteredWords.remove(taggedWord);
                        }
                    }
                }
            }
            if (taggedWord.getUdPosTag().equals("NUM")) {
                if (numeric == null) {
                    String word = taggedWord.getWord().replace(',', '.');
                    if (word.contains("/")) {
                        String[] part = word.split("/");
                        numeric = Double.parseDouble(part[0]) / Double.parseDouble(part[1]);
                    } else {
                        numeric = Double.parseDouble(word);
                    }
                    Logger.trace("Added " + numeric + " as numeric");
                    filteredWords.remove(taggedWord);
                }
            }
        }

        // Choose STYCK as unit if no unit is found.
        if (unit == null) {
            unit = Amount.Unit.STYCK;
        }
        Logger.trace("Added " + unit.name() + " as unit");
        taggedWords = filteredWords;

        if (numeric != null) {
            return new Amount(numeric, unit);
        } else {
            return new Amount(0.0, Amount.Unit.EMPTY);
        }
    }

    private FoodGeneral findFoodGeneral() {
        StringBuilder builder = new StringBuilder(" ");
        for (TaggedWord taggedWord : taggedWords) {
            builder.append(taggedWord.getLemma()).append(" ");
        }

        String line = builder.toString();
        String matchingTag = "";
        int matchingTagLength = 0;
        FoodGeneral food = null;
        List<FoodGeneral> items = FoodGeneral.find.select("searchTags").findList();

        for (FoodGeneral general : items) {
            List<String> tags = general.searchTags;
            for (String tag : tags) {
                if (line.contains(" " + tag + " ")||
                    line.contains(" " + tag + ",") ||
                    line.contains(" " + tag + ".")) {
                    if (tag.length() > matchingTagLength) {
                        Logger.trace("Found \"" + general.name + "\"");
                        food = general;
                        matchingTag = tag;
                        matchingTagLength = tag.length();
                    }
                }
            }
        }

        leftover = line.replace(matchingTag, "");
        return food;
    }

    /**
     * Calls an external API to get words in a String classified.
     *
     * @param webString
     * @return
     * @throws IOException
     */
    private JsonNode retrieveWordInfo(String webString) throws IOException {
        WSClient ws = WS.newClient(9000);
        CompletionStage<WSResponse> request = ws.url("http://json-tagger.herokuapp.com/tag")
            .setContentType("application/x-www-form-urlencoded").post(webString);
        CompletionStage<JsonNode> jsonPromise = request.thenApply(WSResponse::asJson);
        try {
            return jsonPromise.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException("Illegal webString");
        } finally {
            ws.close();
        }
    }

    private class IngredientNotFoundException extends Throwable {}
}
