package parsers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.ParseController;
import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;

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
    private List<String> ingredients = new ArrayList<>();
    private List<Amount.Unit> units = new ArrayList<>();
    private Map<Integer, String> adjektiv = new HashMap<>();
    private Map<Integer, String> other = new HashMap<>();
    private Map<Integer, String> numerics = new HashMap<>();

    public IngredientSplitter(String ingredientLine, JsonNode node) {
        this.ingredientLine = ingredientLine;
        this.node = node;
    }

    public Ingredient createIngredient() {
        divide();
        extractIngeridents();
        extractUnits();
        extractNumeric();
        extractAdjektiv();
        extractOther();

        System.out.println(ingredients.get(0));
        FoodItem foodItem = parse.findMatch(ingredients.get(0));
        Ingredient ingredient = new Ingredient(foodItem, new Amount(Double.parseDouble(numerics.get(0)), units.get(0)));
        return ingredient;
    }

    public List divide() {
        System.out.println(ingredientLine);
        String[] div = ingredientLine.trim().split("\\s+");
        System.out.println(div);
        for (String str : div) {
            System.out.println(str);
            stringList.add(str);
        }
        return stringList;
    }

    public List extractIngeridents() {
        int counter = 0;
        for (String str : stringList) {
            if (!str.matches(".*\\d+.*")) {
                if (parse.findMatch(str) != null) {
                    ingredients.add(str);
                    notOther.add(str);
                }
            }
            counter++;
        }
        System.out.println(ingredients);
        return ingredients;
    }

    public Map extractAdjektiv() {
        int counter = 0;
        for (String str : stringList) {
            if (getters.getSucPosTag(node, counter).equals("JJ")) {
                adjektiv.put(counter, str);
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
                numerics.put(counter, str);
                notOther.add(str);
            }
            counter++;
        }
        return numerics;
    }

    public List extractUnits() {
        int counter;
        unitsID = Amount.Unit.values();
        for (Amount.Unit unit : unitsID) {
            counter = 0;
            for (String str : stringList) {
                for (String identifier : unit.getIdentifiers()) {
                    if (identifier.equals(getters.getLemma(node, counter))) {
                        units.add(unit);
                        notOther.add(str);
                    }
                }
                System.out.println("UNIT = " + units);
                counter++;
            }
        }
        System.out.println("UNIT = " + units);
        return units;
    }

    public Map extractOther() {
        System.out.println("NOT OTHER = " + notOther);
        int counter = 0;
        for (String str : stringList) {
            if (!notOther.contains(str)) {
                other.put(counter, str);
            }
            counter++;
        }
        return other;
    }


}
