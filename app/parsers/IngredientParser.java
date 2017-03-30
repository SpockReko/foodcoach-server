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
    private String leftover;

    public Ingredient parse(String webString) {

        try {
            JsonNode jsonNode = retrieveWordInfo(webString);
            taggedWords = JsonHelper.getTaggedWords(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Ingredient ingredient;
        try {
            ingredient = findIngredient(webString);
        } catch (IngredientNotFoundException e) {
            Logger.error("No match for \"" + webString + "\"");
            return null;
        }

        return ingredient;
    }

    private Ingredient findIngredient(String line) throws IngredientNotFoundException {
        Logger.info("Parsing \"" + line + "\"");
        Amount amount = findAmount();
        FoodItem food = findFood();
        String comment = leftover;

        if (food != null) {
            if (!comment.matches("[ -.,:]*")) {
                Logger.debug("Added " + comment.trim() + " as comment");
                return new Ingredient(food, amount, comment.trim());
            } else {
                return new Ingredient(food, amount);
            }
        } else {
            throw new IngredientNotFoundException();
        }
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
                            Logger.debug("Added " + unit.name() + " as unit");
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

        // Choose STYCK as unit if no unit is found.
        if (unit == null) {
            unit = Amount.Unit.STYCK;
            Logger.debug("Added " + unit.name() + " as unit");
        }

        if (numeric != null && unit != null) {
            taggedWords = filteredWords;
            return new Amount(numeric, unit);
        } else {
            return null;
        }
    }

    private FoodItem findFood() {
        StringBuilder builder = new StringBuilder(" ");
        for (TaggedWord taggedWord : taggedWords) {
            builder.append(taggedWord.getLemma()).append(" ");
        }

        String line = builder.toString();
        String matchingTag = "";
        int matchingTagLength = 0;
        FoodItem food = null;
        List<FoodItem> items = FoodItem.find.select("searchTags").findList();

        for (FoodItem item : items) {
            List<String> tags = item.searchTags;
            for (String tag : tags) {
                if (line.contains(" " + tag + " ")||
                    line.contains(" " + tag + ",") ||
                    line.contains(" " + tag + ".")) {
                    if (tag.length() > matchingTagLength) {
                        Logger.debug("Found \"" + item.getName());
                        food = item;
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
