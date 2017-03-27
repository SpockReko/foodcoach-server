package parsers;

import models.food.FoodItem;

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

        FoodItem food = FoodItem.find.where()
            .contains("searchTags", ingredient)
            .or().eq("name", ingredient)
            .findUnique();

        if (food == null) {
            food = autoCorrect(ingredient);
            if (food != null) {
                return food;
            } else {
                // TODO Throw exception here instead
                return null;
                //int foodNumber = FoodItem.find.findCount() * ingredient.hashCode();
                //return new FoodItem(ingredient, foodNumber);
            }
        } else {
            return food;
        }
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
