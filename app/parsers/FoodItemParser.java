package parsers;

import models.food.FoodItem;

import java.util.ArrayList;
import java.util.List;

import info.debatty.java.stringsimilarity.*;
import play.Logger;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class FoodItemParser {

    private int maxDistance = 2;

    private Levenshtein levenshtein = new Levenshtein();
    private FoodItem matchingFood = null;
    private double shortestDistance = Double.MAX_VALUE;

    public FoodItem findMatch(String ingredient) {
        matchingFood = null;
        shortestDistance = Double.MAX_VALUE;
        int matchingTagLength = 0;
        FoodItem food = null;
        List<FoodItem> items = FoodItem.find.select("search_tags").findList();

        for (FoodItem item : items) {
            List<String> tags = item.searchTags;
            for (String tag : tags) {
                if (ingredient.contains(" " + tag + " ")){
                    if (tag.length() > matchingTagLength){
                        food = item;
                    }
                }
            }
        }
        return food;
    }

    private FoodItem autoCorrect(String ingredient) {
        List<FoodItem> allFoods = FoodItem.find.all();

        for (FoodItem food : allFoods) {
            if (food.searchTags != null && !food.searchTags.isEmpty()) {
                for (String tag : food.searchTags) {
                    checkDistance(ingredient, tag, food);
                }
            }
            String tag = food.getName().toLowerCase();
            checkDistance(ingredient, tag, food);
        }
        return matchingFood;
    }

    private void checkDistance(String input, String tag, FoodItem food) {
        if (input.length() <=1){
            maxDistance = 0;
        }
        if (input.length() <= 3){
            maxDistance = 1;
        }
        double tagDistance = levenshtein.distance(tag.toLowerCase(), input.toLowerCase());

        if (tagDistance <= maxDistance) {
            if (tagDistance < shortestDistance) {
                matchingFood = food;
                shortestDistance = tagDistance;
                Logger.debug("Found distance: " + shortestDistance);
                Logger.debug("Matching food: " + matchingFood.screenName);
            }
        }
    }
}
