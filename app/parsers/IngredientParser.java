package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonHelper;
import helpers.TaggedWord;
import models.food.FoodItem;
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

/**
 * Created by emmafahlen on 2017-03-23.
 */
public class IngredientParser {

    private List<TaggedWord> taggedWords;
    private String workString;

    public Ingredient parse(String webString) {
        workString = webString;

        try {
            JsonNode jsonNode = retrieveWordInfo(webString);
            taggedWords = JsonHelper.getTaggedWords(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (workString.contains("eller")) {
            String[] split = workString.split("eller");
        }

        if (workString.contains(":")) {
            String[] split = workString.split(":");
        }

        Ingredient ingredient = null;
        try {
            ingredient = findIngredient();
        } catch (IngredientNotFoundException e) {
            Logger.warn("No match for '" + webString + "'");
            return null;
        }

        return ingredient;
    }

    private Ingredient findIngredient() throws IngredientNotFoundException {
        Logger.info(workString);
        Amount amount = findAmount(workString);
        FoodItem food = findFood(workString);
        String comment = workString;

        if (amount == null) {
            Logger.warn("No amount found for '" + workString + "'");
            if (food != null) {
                if (!comment.isEmpty() && !comment.equals(" ")) {
                    return new Ingredient(food, new Amount(0, Amount.Unit.UNKNOWN), comment.trim());
                } else {
                    return new Ingredient(food, new Amount(0, Amount.Unit.UNKNOWN));
                }
            } else {
                throw new IngredientNotFoundException();
            }
        } else {
            if (!comment.isEmpty() && !comment.equals(" ")) {
                return new Ingredient(food, amount, comment.trim());
            } else {
                return new Ingredient(food, amount);
            }
        }
    }

    private Amount findAmount(String str) {
        List<TaggedWord> filteredWords = new ArrayList<>(taggedWords);
        Double numeric = null;
        Amount.Unit unit = null;

        for (TaggedWord taggedWord : taggedWords) {
            for (Amount.Unit u : Amount.Unit.values()) {
                for (String identifier : u.getIdentifiers()) {
                    if (identifier.equals(taggedWord.getLemma())) {
                        if (unit == null) {
                            unit = u;
                            Logger.debug("Added " + taggedWord.getWord() + " as unit");
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
                    Logger.debug("Added " + word + " as numeric");
                    filteredWords.remove(taggedWord);
                }
            }
        }

        if (numeric != null && unit != null) {
            StringBuilder strBuilder = new StringBuilder();
            for (TaggedWord word : filteredWords) {
                strBuilder.append(word.getWord()).append(" ");
            }
            workString = strBuilder.toString();
            return new Amount(numeric, unit);
        } else {
            return null;
        }
    }

    private FoodItem findFood(String str) {
        String ingredient = " " + str + " ";
        String matchingTag = "";
        int matchingTagLength = 0;
        FoodItem food = null;
        List<FoodItem> items = FoodItem.find.select("searchTags").findList();

        for (FoodItem item : items) {
            List<String> tags = item.searchTags;
            for (String tag : tags) {
                if (ingredient.contains(" " + tag + " ") || ingredient.contains(" " + tag + ",")
                    || ingredient.contains(" " + tag + ".")) {
                    if (tag.length() > matchingTagLength) {
                        Logger.debug("Found \"" + item.getName() + "\" for string '" + str + "'");
                        food = item;
                        matchingTag = tag;
                        matchingTagLength = tag.length();
                    }
                }
            }
        }

        workString = workString.replace(matchingTag, "");

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
