package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.JsonWordHelper;
import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
import play.Logger;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Created by emmafahlen on 2017-03-23.
 */
public class IngredientParser {

    private List<String> notOther = new ArrayList<>();
    private List<String> ingredients = new ArrayList<>();
    private List<String> adjectives = new ArrayList<>();
    private List<String> other = new ArrayList<>();

    private FoodItemParser foodItemParser = new FoodItemParser();
    private JsonNode wordInfo;
    private String workString;

    public Ingredient parse(String webString) {
        workString = webString;

        try {
            wordInfo = retrieveWordInfo(webString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (workString.contains("eller")) {
            String[] split = workString.split("eller");
        }

        if (workString.contains(":")) {
            String[] split = workString.split(":");
        }

        Ingredient ingredient;
        try {
            ingredient = findIngredient();
        } catch (AmountNotFoundException e) {
            Logger.warn("No amount found for '" + webString + "'");
            return null;
        }

        return ingredient;
    }

    private Ingredient findIngredient() throws AmountNotFoundException {
        Amount amount = findAmount(workString);
        FoodItem food = findFood(workString);

        return new Ingredient(food, amount);
    }

    private Amount findAmount(String str) throws AmountNotFoundException {
        String[] words = str.trim().split("\\s+");
        Double numeric = null;
        Amount.Unit unit = null;
        List<Integer> wordsToRemove = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            for (Amount.Unit u : Amount.Unit.values()) {
                for (String identifier : u.getIdentifiers()) {
                    if (identifier.equals(JsonWordHelper.getLemma(wordInfo, i))) {
                        if (unit == null) {
                            unit = u;
                            System.out.println("Added " + word + " as unit");
                            wordsToRemove.add(i);
                            notOther.add(word);
                        }
                    }
                }
            }
            if (JsonWordHelper.getSucFeatures(wordInfo, i).equals("NOM")) {
                if (numeric == null) {
                    word = word.replace(',','.');
                    numeric = Double.parseDouble(word);
                    System.out.println("Added " + word + " as numeric");
                    wordsToRemove.add(i);
                    notOther.add(word);
                }
            }
        }

        if (numeric != null && unit != null) {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                if (wordsToRemove.contains(i)) {
                    continue;
                }
                strBuilder.append(words[i]).append(" ");
            }
            workString = strBuilder.toString();
            return new Amount(numeric, unit);
        } else {
            throw new AmountNotFoundException();
        }
    }

    private FoodItem findFood(String str) {
        int counter = 0;

        String[] words = str.trim().split("\\s+");
        Map<String, Boolean> map = new HashMap<>();

        for (String word : words) {
            map.put(word, true);
        }

        for (String word : words) {
            if (!word.matches(".*\\d+.*")) {
                if (foodItemParser.findMatch(" " + word + " ") != null) {
                    if (map.get(word)) {
                        ingredients.add(word);
                        notOther.add(word);
                        System.out.println("Added " + word + " as ingredient");
                        map.replace(word, false);
                    }
                }
            }
            if (JsonWordHelper.getSucPosTag(wordInfo, counter).equals("JJ")) {
                if (map.get(word)) {
                    adjectives.add(word);
                    notOther.add(word);
                    System.out.println("Added " + word + " as adjective");
                    map.replace(word, false);
                }
            }
            counter++;
        }
        for (String word : words) {
            if (!notOther.contains(word)) {
                other.add(word);
            }
        }

        if (ingredients.isEmpty()) {
            return null;
        }

        return foodItemParser.findMatch(ingredients.get(0));
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

    public class AmountNotFoundException extends Exception {}
    public class FoodNotFoundException extends Exception {}
}
