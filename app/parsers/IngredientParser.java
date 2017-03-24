package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

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
    private List<String> numerics = new ArrayList<>();
    private List<Amount.Unit> units = new ArrayList<>();
    private List<String> other = new ArrayList<>();

    public Ingredient parse(String webString) {
        System.out.println(webString);
        int counter = 0;
        JsonNode wordInfo = getWordInfo(webString);
        String[] words = webString.trim().split("\\s+");
        Map<String, Boolean> map = new HashMap<>();

        for (String word : words){
            map.put(word, true);
        }

        for (String word : words) {
            for (Amount.Unit unit : Amount.Unit.values()) {
                for (String identifier : unit.getIdentifiers()) {
                    if (identifier.equals(getLemma(wordInfo, counter))) {
                        if (map.get(word)) {
                            units.add(unit);
                            notOther.add(word);
                            map.replace(word, false);
                            System.out.println("Added " + word + " as unit");
                        }
                    }
                }
            }
            if (getSucFeatures(wordInfo, counter).equals("NOM")) {
                if (map.get(word)) {
                    numerics.add(word);
                    notOther.add(word);
                    System.out.println("Added " + word + " as numeric");
                    map.replace(word, false);
                }
            }
            if (!word.matches(".*\\d+.*")) {
                if (FoodItemParser.findMatch(word) != null) {
                    if (map.get(word)) {
                        ingredients.add(word);
                        notOther.add(word);
                        System.out.println("Added " + word + " as ingredient");
                        map.replace(word, false);
                    }
                }
            }
            if (getSucPosTag(wordInfo, counter).equals("JJ")) {
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

        if (ingredients.isEmpty() || numerics.isEmpty() || units.isEmpty()) {
            return null;
        }

        Amount amount;
        try {
            amount = new Amount(Double.parseDouble(numerics.get(0).replace(',','.')), units.get(0));
        } catch (IllegalArgumentException e) {
            return null;
        }

        FoodItem foodItem = FoodItemParser.findMatch(ingredients.get(0));

        return new Ingredient(foodItem, amount);
    }

    private JsonNode getWordInfo(String webString) {
        WSClient ws = WS.newClient(9000);
        CompletionStage<WSResponse> request = ws.url("http://json-tagger.herokuapp.com/tag")
                .setContentType("application/x-www-form-urlencoded")
                .post(webString);
        CompletionStage<JsonNode> jsonPromise = request.thenApply(WSResponse::asJson);
        try {
            return jsonPromise.toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalArgumentException("Illegal webString");
        }
    }

    public String getWordForm(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("word_form").toString();
        return str.substring(1, str.length() - 1);
    }

    public String getLemma(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("lemma").toString();
        return str.substring(1, str.length() - 1);
    }

    public String getSucPosTag(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("suc_tags").get("pos_tag").toString();
        return str.substring(1, str.length() - 1);
    }

    public String getSucFeatures(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("suc_tags").get("features").toString();
        return str.substring(1, str.length() - 1);
    }

    public String getUdPosTag(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("ud_tags").get("pos_tag").toString();
        return str.substring(1, str.length() - 1);
    }

    public String getUdFeatures(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("ud_tags").get("features").toString();
        return str.substring(1, str.length() - 1);
    }

    public String getTokenID(JsonNode json, int index) {
        String str = json.get("sentences").get(0).get(index).get("token_id").toString();
        return str.substring(1, str.length() - 1);
    }

    public int getNumberOfWords(JsonNode json) {
        int counter = 0;
        while (true) {
            if (json.get("sentences").get(0).has(counter)) {
                counter++;
            } else {
                break;
            }
        }
        System.out.println("COUNT = " + counter);
        return counter;
    }
}
