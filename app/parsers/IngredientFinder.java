package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.Constants;
import helpers.StringHelper;
import helpers.TaggerHelper;
import models.food.Food;
import models.food.FoodGroup;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.word.TaggedWord;
import play.Logger;
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
class IngredientFinder {

    private final List<FoodGroup> foodGroupList;
    private final WSClient wsClient;
    private List<TaggedWord> taggedWords;
    private String leftover = "";

    /**
     * @param wsClient The web service client to use when calling Json Tagger API.
     * @param foodGroupList A list of all {@link FoodGroup}s in the database.
     */
    IngredientFinder(WSClient wsClient, List<FoodGroup> foodGroupList) {
        this.foodGroupList = foodGroupList;
        this.wsClient = wsClient;
    }

    /**
     * Tries to find a complete ingredient with amount and food in a string.
     * @param line The string to look in.
     * @return An ingredient if found, null if not.
     * @throws IOException If the external API is not available.
     */
    Ingredient find(String line) throws IOException {
        Ingredient ingredient;
        leftover = "";

        JsonNode jsonNode = retrieveWordInfo(line);
        taggedWords = TaggerHelper.getTaggedWords(jsonNode);

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
    Ingredient find(String line, Amount amount) throws IOException {
        Ingredient ingredient;
        leftover = "";

        JsonNode jsonNode = retrieveWordInfo(line);
        taggedWords = TaggerHelper.getTaggedWords(jsonNode);

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
    Amount extractAmount(String line) throws IOException {
        JsonNode jsonNode = retrieveWordInfo(line);
        taggedWords = TaggerHelper.getTaggedWords(jsonNode);
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
                    } else if (word.contains("-")) {
                        String[] part = word.split("-");
                        numeric = (Double.parseDouble(part[0]) + Double.parseDouble(part[1])) / 2;
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
        FoodGroup foodGroup = findFoodGroup();

        if (foodGroup != null) {
            Food food = findFood(foodGroup);
            Ingredient ingredient = new Ingredient(food, amount);
            if (!leftover.isEmpty() && !leftover.matches("[ -.,:]*")) {
                String comment = leftover.replaceAll("\\s+(?=[),])|\\s{2,}", "");
                Logger.trace("Added " + comment.trim() + " as comment");
                Logger.info("Ingredient { " + amount.getQuantity() + ", " + amount.getUnit() + ", "
                    + food.name + ", \"" + comment.trim() + "\" }");
                ingredient.comment = comment.trim();
            } else {
                Logger.info("Ingredient { " + amount.getQuantity() + ", " + amount.getUnit() + ", "
                    + food.name + " }");
            }
            return ingredient;
        } else {
            return null;
        }
    }

    /**
     * Tries to find a group food in the given string.
     * @return A FoodGroup if found, null if not.
     */
    private FoodGroup findFoodGroup() {
        String line = TaggerHelper.taggedToString(taggedWords);
        String matchingTag = "";
        int matchingTagLength = 0;
        FoodGroup foodGroup = null;

        for (FoodGroup group : foodGroupList) {
            List<String> tags = group.searchTags;
            tags.add(group.name.toLowerCase());
            for (String tag : tags) {
                if (StringHelper.containsWord(line, tag)) {
                    if (tag.length() > matchingTagLength) {
                        Logger.trace("Found \"" + group.name + "\"");
                        foodGroup = group;
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
        if (foodGroup == null) {
            AutoCorrecter correcter = new AutoCorrecter();
            String[] listLine = line.trim().split("\\s++");
            for (String word : listLine) {
                if (correcter.autoCorrect(word) != null) {
                    foodGroup = correcter.autoCorrect(word);
                    if (!leftover.isEmpty()) {
                        leftover = leftover.replace(word, "");
                    } else {
                        leftover = line.replace(word, "");
                    }
                }

            }
        }

        return foodGroup;
    }

    /**
     * Tries to find a food within a group food that matches better than the default.
     * @param foodGroup The group food to find foods within.
     * @return A better matching food, or default if no better is found.
     */
    private Food findFood(FoodGroup foodGroup) {
        String line = TaggerHelper.taggedToString(taggedWords);
        List<String> tags;
        Food food;
        String currentTag = null;
        Boolean hasTag = false;
        int tagAmount = Integer.MAX_VALUE;
        int currentTagAmount;
        int matchingTagAmount = 0;
        int currentMatchingTagAmount = 0;

        food = foodGroup.defaultFood;
        for (Food f : foodGroup.foods) {
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
    private JsonNode retrieveWordInfo(String line) {
        CompletionStage<WSResponse> request = wsClient.url(Constants.JSON_TAGGER_URL)
            .setContentType("application/x-www-form-urlencoded").post(line);
        CompletionStage<JsonNode> jsonPromise = request.thenApply(WSResponse::asJson);
        try {
            return jsonPromise.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException("Illegal webString");
        }
    }
}
