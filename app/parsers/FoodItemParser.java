package parsers;

import models.food.FoodItem;

import java.util.ArrayList;
import java.util.List;

import info.debatty.java.stringsimilarity.*;
import models.food.fineli.FoodGeneral;
import play.Logger;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class FoodItemParser {

    private int maxDistance = 1;

    private Levenshtein levenshtein = new Levenshtein();
    private FoodGeneral matchingFood = null;
    private double shortestDistance = Double.MAX_VALUE;

    public FoodGeneral findMatch(String input) {
        String ingredient = " " + input + " ";
        matchingFood = null;
        shortestDistance = Double.MAX_VALUE;
        int matchingTagLength = 0;
        FoodGeneral food = null;
        List<FoodItem> items = FoodItem.find.select("searchTags").findList();

        for (FoodItem item : items) {
            List<String> tags = item.searchTags;
            for (String tag : tags) {
                if (ingredient.contains(" " + tag + " ") ||
                    ingredient.contains(" " + tag + ",") ||
                    ingredient.contains(" " + tag + ".")) {
                    if (tag.length() > matchingTagLength) {
                        Logger.debug("Found \"" + item.getName() + "\" for string '" + input + "'");
                        //food = item;
                        matchingTagLength = tag.length();
                    }
                }
            }
        }
        return food;
    }

    public FoodGeneral autoCorrect(String ingredient) {
        List<FoodGeneral> items = FoodGeneral.find.select("searchTags").findList();
        for (FoodGeneral food : items) {
            List<String> tags = food.searchTags;
            tags.add(food.name.toLowerCase());
            for (String tag : tags) {
                checkDistance(ingredient, tag, food);
            }
        }

        return matchingFood;
    }

    private void checkDistance(String input, String tag, FoodGeneral food) {
        if (input.length() <= 1) {
            maxDistance = 0;
        }
        if (input.length() <= 3) {
            maxDistance = 1;
        }
        double tagDistance = levenshtein.distance(tag.toLowerCase(), input.toLowerCase());

        if (tagDistance <= maxDistance) {
            if (tagDistance < shortestDistance) {
                matchingFood = food;
                shortestDistance = tagDistance;
                Logger.debug("Found distance: " + shortestDistance);
                Logger.debug("Matching food: " + matchingFood.name);
            }
        }
    }
}
