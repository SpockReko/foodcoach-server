package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonHelper;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by emmafahlen on 2017-03-23.
 */
public class IngredientParser {

    private List<String> afterEller = new ArrayList<>();
    private List<String> afterOch = new ArrayList<>();

    private List<TaggedWord> taggedWords;
    private String leftover = "";
    private String insideParenthesis = "";
    private String title = "";
    private boolean hasAmount;

    public List<Ingredient> parse(String webString) {
        List<Ingredient> ingredients = new ArrayList<>();

        String[] parenthesis = extractParenthesis(webString);
        String line = parenthesis[0];
        if (parenthesis[1] != null) {
            insideParenthesis = " (" + parenthesis[1] + ")";
        } else {
            insideParenthesis = "";
        }

        //Handle ":"
        if (line.contains(":")) {
            String[] split = line.toLowerCase().split(":");
            line = split[1];
            title = split[0];
            System.out.println("AFTER COLON: " + split[1]);
            System.out.println("BEFORE COLON: " + split[0]);
        }

        line = handleEdgeCases(line);

        try {
            JsonNode jsonNode = retrieveWordInfo(line);
            taggedWords = JsonHelper.getTaggedWords(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Ingredient ingredient = findIngredient(line);
            ingredients.add(ingredient);
        } catch (IngredientNotFoundException e) {
            Logger.error("No match \"" + webString + "\"");
            return null;
        }

        if (!afterOch.isEmpty()) {
            for (String och : afterOch) {
                if (hasAmount(och)) {
                    try {
                        ingredients.add(findIngredient(och));
                    } catch (IngredientNotFoundException e) {
                        Logger.error("No match \"" + webString + "\"");
                    }
                } else {
                    Amount amount = ingredients.get(0).getAmount();
                    try {
                        ingredients.add(findIngredient(och, amount));
                    } catch (IngredientNotFoundException e) {
                        Logger.error("No match \"" + webString + "\"");
                    }
                }
            }
        }

        return ingredients;
    }

    private Ingredient findIngredient(String line) throws IngredientNotFoundException {
        return findIngredient(line, findAmount());
    }

    private Ingredient findIngredient(String line, Amount amount) throws IngredientNotFoundException {
        FoodGeneral foodGeneral = findFoodGeneral();
        Food food = findFood(foodGeneral, line);

        if (food != null) {
            if (!title.equals("")) {
                if (!leftover.isEmpty() && !leftover.matches("[ -.,:]*")) {
                    String comment = leftover.replaceAll("\\s+(?=[),])|\\s{2,}", "");
                    comment += insideParenthesis;
                    Logger.trace("Added " + comment.trim() + " as comment");
                    Logger.info(
                        "Ingredient { " + amount.getAmount() + ", " + amount.getUnit() + ", "
                            + food.name + ", \"" + comment.trim() + "\" }");
                    return new Ingredient(food, amount, comment.trim(), title);
                } else {
                    return new Ingredient(food, amount, null, title);
                }
            }
            if (!leftover.isEmpty() && !leftover.matches("[ -.,:]*")) {
                String comment = leftover.replaceAll("\\s+(?=[),])|\\s{2,}", "");
                comment += insideParenthesis;
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
            hasAmount = true;
            return new Amount(numeric, unit);
        } else {
            hasAmount = false;
            return new Amount(0.0, Amount.Unit.EMPTY);
        }
    }

    private FoodGeneral findFoodGeneral() {
        StringBuilder builder = new StringBuilder(" ");
        for (TaggedWord taggedWord : taggedWords) {
            builder.append(taggedWord.getWord()).append(" ");
        }

        String line = builder.toString();
        String matchingTag = "";
        int matchingTagLength = 0;
        FoodGeneral foodGeneral = null;
        List<FoodGeneral> items = FoodGeneral.find.select("searchTags").findList();

        for (FoodGeneral general : items) {
            List<String> tags = general.searchTags;
            tags.add(general.name.toLowerCase());
            for (String tag : tags) {
                if (line.contains(" " + tag + " ") || line.contains(" " + tag + ",") || line
                    .contains(" " + tag + ".")) {
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

        if (foodGeneral == null) {
            System.out.println("FOOD GENERAL NULL");
            FoodParser parser = new FoodParser();
            System.out.println("LINE: " + line);
            String[] listLine = line.trim().split("\\s++");
            for (String word : listLine) {
                if (parser.autoCorrect(word) != null) {
                    foodGeneral = parser.autoCorrect(word);
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

    private Food findFood(FoodGeneral foodGeneral, String webString) {
        List<String> tags;
        Food food;
        String currentTag = null;
        Boolean hasTag = false;
        int tagAmount = Integer.MAX_VALUE;
        int currentTagAmount;
        int matchingTagAmount = 0;
        int currentMatchingTagAmount = 0;

        if (foodGeneral == null) {
            food = null;
        } else {
            food = foodGeneral.defaultFood;
            for (Food f : foodGeneral.foods) {
                tags = f.tags;
                for (String tag : tags) {
                    currentTagAmount = tags.size();
                    for (TaggedWord tagged : taggedWords) {
                        if (webString.contains(tag) || tagged.getLemma().equals(tag) || tagged.getWord().equals(tag)) {
                            currentMatchingTagAmount++;
                            if (currentTagAmount < tagAmount && currentMatchingTagAmount >= matchingTagAmount) {
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
        }

        if (hasTag) {
            leftover = leftover.replace(currentTag, "");
        }

        return food;
    }

    private String handleEdgeCases(String webString) {
        //Handle "eller"
        if (webString.toLowerCase().contains(" " + "eller" + " ") || webString.toLowerCase()
            .contains(" " + "eller" + ",") || webString.toLowerCase()
            .contains(" " + "eller" + ".")) {
            String[] split = webString.toLowerCase().split(" eller");
            webString = split[0];
            for (int i = 1; i < split.length; i++) {
                afterEller.add(split[i]);
                System.out.println("AFTER ELLER: " + split[i]);
            }
            System.out.println("BEFORE ELLER: " + split[0]);
        }

        //Handle "och"
        if (webString.toLowerCase().contains(" " + "och" + " ") || webString.toLowerCase()
            .contains(" " + "och" + ",") || webString.toLowerCase().contains(" " + "och" + ".")) {
            String[] split = webString.toLowerCase().split(" och");
            webString = split[0];
            for (int i = 1; i < split.length; i++) {
                afterOch.add(split[i]);
                System.out.println("AFTER OCH: " + split[i]);
            }
            System.out.println("BEFORE OCH: " + split[0]);
        }

        System.out.println("WEBSTRING: " + webString);
        return webString;
    }

    private String[] extractParenthesis(String input) {
        String[] split = new String[2];
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(input);
        while (m.find()) {
            if (m.group(1) != null) {
                split[1] = m.group(1);
            }
        }
        split[0] = input.replaceAll("\\(([^)]+)\\)", "");
        return split;
    }

    public boolean hasAmount(String webString) {
        findAmount();
        return hasAmount;
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

    private class IngredientNotFoundException extends Throwable {
    }
}
