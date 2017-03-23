package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ParseController;
import models.food.FoodItem;
import models.recipe.Amount;

import java.util.*;

/**
 * Created by emmafahlen on 2017-03-23.
 */
public class IngredientSplitter {
    private static String ingredientLine;
    private static JsonNode node;
    private static ParseController parseController;

    IngredientParser parse = new IngredientParser();
    WordGetters getters = new WordGetters();
    Amount.Unit[] unitsID;

    private List<String> notOther = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private Map<String, Integer> ingredients = new HashMap<>();
    private Map<String, Integer> adjektiv = new HashMap<>();
    private Map<String, Integer> other = new HashMap<>();
    private Map<String, Integer> numerics = new HashMap<>();
    private Map<String, Integer> units = new HashMap<>();

    public IngredientSplitter(String ingredientLine, JsonNode node) {
        this.ingredientLine = ingredientLine;
        this.node = node;
    }

    public List divided() {
        System.out.println(ingredientLine);
        String[] div = ingredientLine.trim().split("\\s+");
        System.out.println(div);
        for (String str : div) {
            System.out.println(str);
            stringList.add(str);
        }
        return stringList;
    }

    public Map extractIngeridents() {
        int counter = 0;
        for (String str : stringList) {
            if (!str.matches(".*\\d+.*")) {
                if (parse.findMatch(str) != null) {
                    ingredients.put(str, counter);
                    notOther.add(str);
                }
            }
            counter++;
        }
        return ingredients;
    }

    public Map extractAdjektiv() {
        int counter = 0;
        for (String str : stringList) {
            if (getters.getSucPosTag(node, counter).equals("JJ")) {
                adjektiv.put(str, counter);
                notOther.add(str);
            }
            counter++;
        }
        return adjektiv;
    }

    public Map extractNumeric() {
        int counter = 0;
        for (String str : stringList) {
            if (getters.getSucFeatures(node, counter).equals("NOM")) {
                numerics.put(str, counter);
                notOther.add(str);
            }
            counter++;
        }
        return numerics;
    }

    public Map extractUnits() {
        int counter;
        unitsID = Amount.Unit.values();
        for (Amount.Unit unit : unitsID) {
            counter = 0;
            for (String str : stringList) {
                for (String identifier : unit.getIdentifiers()) {
                    if (identifier.equals(getters.getLemma(node, counter))) {
                        units.put(str, counter);
                        notOther.add(str);
                    }
                }
                counter++;
            }
        }
        return units;
    }

    public Map extractOther() {
        System.out.println("NOT OTHER = " + notOther);
        int counter = 0;
        for (String str : stringList) {
            if (!notOther.contains(str)) {
                other.put(str, counter);
            }
            counter++;
        }
        return other;
    }


}
