package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonHelper;
import helpers.StringHelper;
import helpers.TaggedWord;
import models.food.Food;
import models.food.FoodGeneral;
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
public class IngredientFinder {

    private List<TaggedWord> taggedWords;
    private String leftover = "";

    /**
     * Tries to find a complete ingredient with amount and food in a string.
     * @param line The string to look in.
     * @return An ingredient if found, null if not.
     * @throws IOException If the external API is not available.
     */
    public Ingredient find(String line) throws IOException {
        Ingredient ingredient;
        leftover = "";

        JsonNode jsonNode = retrieveWordInfo(line);
        taggedWords = JsonHelper.getTaggedWords(jsonNode);

        ingredient = findIngredient();
        if (ingredient == null) {
            Logger.error("No match \"" + line + "\"");
            return null;
        }

        return ingredient;
    }

    /**
     * Tries to find a complete ingredient in a string,
     * skips looking for amount uses provided instead.
     * @param line The string to look in.
     * @param amount The amount to use in ingredient.
     * @return An ingredient if found, null if not.
     * @throws IOException If the external API is not available.
     */
    public Ingredient find(String line, Amount amount) throws IOException {
        Ingredient ingredient;
        leftover = "";

        JsonNode jsonNode = retrieveWordInfo(line);
        taggedWords = JsonHelper.getTaggedWords(jsonNode);

        ingredient = findIngredient(amount);
        if (ingredient == null) {
            Logger.error("No match \"" + line + "\"");
            return null;
        }

        return ingredient;
    }

    /**
     * Tries to find an amount in a string and returns it if it found it, null otherwise.
     * @param line The string to look in.
     * @return An amount if found, otherwise null.
     * @throws IOException If the external API is not available.
     */
    public Amount extractAmount(String line) throws IOException {
        JsonNode jsonNode = retrieveWordInfo(line);
        taggedWords = JsonHelper.getTaggedWords(jsonNode);
        Amount amount = findAmount();
        return amount.getUnit().getType().equals(Amount.Unit.Type.EMPTY) ? null : amount;
    }

    /**
     * Finds an amount in the given string using the information obtained from the external API.
     * @return An amount containing amount and unit or empty amount.
     */
    private Amount findAmount() {
        List<TaggedWord> filteredWords = new ArrayList<>(taggedWords);
        Double numeric = null;
        Amount.Unit unit = null;

        for (TaggedWord taggedWord : taggedWords) {
            // Find unit
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
            // Find amount
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

    /**
     * Tries to find an ingredient.
     * @return A complete ingredient if found, else null.
     */
    private Ingredient findIngredient() {
        return findIngredient(findAmount());
    }

    /**
     * Tries to find an ingredient. Skips looking for amount and uses provided instead.
     * @param amount The amount to use instead of trying to find one.
     * @return A complete ingredient if found, else null.
     */
    private Ingredient findIngredient(Amount amount) {
        FoodGeneral foodGeneral = findFoodGeneral();

        if (foodGeneral != null) {
            Food food = findFood(foodGeneral);
            if (!leftover.isEmpty() && !leftover.matches("[ -.,:]*")) {
                String comment = leftover.replaceAll("\\s+(?=[),])|\\s{2,}", "");
                Logger.trace("Added " + comment.trim() + " as comment");
                Logger.info("Ingredient { " + amount.getAmount() + ", " + amount.getUnit() + ", "
                    + food.name + ", \"" + comment.trim() + "\" }");
                return new Ingredient(food, amount, comment.trim());
            } else {
                Logger.info("Ingredient { " + amount.getAmount() + ", " + amount.getUnit() + ", "
                    + food.name + " }");
                return new Ingredient(food, amount);
            }
        } else {
            return null;
        }
    }

    /**
     * Tries to find a general food in the given string.
     * @return A FoodGeneral if found, null if not.
     */
    private FoodGeneral findFoodGeneral() {
        String line = JsonHelper.taggedToString(taggedWords);
        String matchingTag = "";
        int matchingTagLength = 0;
        FoodGeneral foodGeneral = null;
        List<FoodGeneral> items = FoodGeneral.find.select("searchTags").findList();

        for (FoodGeneral general : items) {
            List<String> tags = general.searchTags;
            tags.add(general.name.toLowerCase());
            for (String tag : tags) {
                if (StringHelper.containsWord(line, tag)) {
                    if (tag.length() > matchingTagLength) {
                        Logger.trace("Found \"" + general.name + "\"");
                        foodGeneral = general;
                        matchingTag = tag;
                        matchingTagLength = tag.length();
                    }
                }
            }
        }

        if (leftover.isEmpty()) {
            leftover = line.replace(matchingTag, "");
        } else {
            leftover = leftover.replace(matchingTag, "");
        }

        // If no food is found right away, try and find with different spelling.
        if (foodGeneral == null) {
            AutoCorrecter correcter = new AutoCorrecter();
            String[] listLine = line.trim().split("\\s++");
            for (String word : listLine) {
                if (correcter.autoCorrect(word) != null) {
                    foodGeneral = correcter.autoCorrect(word);
                    if (!leftover.isEmpty()) {
                        leftover = leftover.replace(word, "");
                    } else {
                        leftover = line.replace(word, "");
                    }
                }

            }
        }

        return foodGeneral;
    }

    /**
     * Tries to find a food within a general food that matches better than the default.
     * @param foodGeneral The general food to find foods within.
     * @return A better matching food, or default if no better is found.
     */
    private Food findFood(FoodGeneral foodGeneral) {
        String line = JsonHelper.taggedToString(taggedWords);
        List<String> tags;
        Food food;
        String currentTag = null;
        Boolean hasTag = false;
        int tagAmount = Integer.MAX_VALUE;
        int currentTagAmount;
        int matchingTagAmount = 0;
        int currentMatchingTagAmount = 0;

        food = foodGeneral.defaultFood;
        for (Food f : foodGeneral.foods) {
            tags = f.tags;
            for (String tag : tags) {
                currentTagAmount = tags.size();
                for (TaggedWord tagged : taggedWords) {
                    if (line.contains(tag) || tagged.getLemma().equals(tag) || tagged.getWord().equals(tag)) {
                        currentMatchingTagAmount++;
                        if (currentTagAmount < tagAmount
                            && currentMatchingTagAmount >= matchingTagAmount) {
                            food = f;
                            tagAmount = currentTagAmount;
                            matchingTagAmount = currentMatchingTagAmount;
                            currentTag = tag;
                            hasTag = true;
                        }
                    }
                }
            }
            currentMatchingTagAmount = 0;
        }

        if (hasTag) {
            leftover = leftover.replace(currentTag, "");
        }

        return food;
    }

    /**
     * Calls an external API to get words in a String classified.
     *
     * @param line String to send to the API.
     * @return A json respone with info about each word.
     * @throws IOException When the HTTP request cannot be made.
     */
    private JsonNode retrieveWordInfo(String line) throws IOException {
        WSClient ws = WS.newClient(9000);
        CompletionStage<WSResponse> request = ws.url("https://json-tagger.com/tag")
            .setContentType("application/x-www-form-urlencoded").post(line);
        CompletionStage<JsonNode> jsonPromise = request.thenApply(WSResponse::asJson);
        try {
            return jsonPromise.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException("Illegal webString");
        } finally {
            ws.close();
        }
    }
}
