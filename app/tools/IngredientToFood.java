package tools;

import models.food.FoodItem;

import java.util.List;

import info.debatty.java.stringsimilarity.*;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class IngredientToFood {

    private static final int MAX_DISTANCE = 3;

    private static Levenshtein levenshtein = new Levenshtein();
    private static FoodItem matchingFood = null;
    private static double shortestDistance = Double.MAX_VALUE;

    public static FoodItem findMatch(String ingredient) {
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
                int foodNumber = FoodItem.find.findCount() * ingredient.hashCode();
                return new FoodItem(ingredient, foodNumber);
            }
        } else {
            return food;
        }
    }

    private static FoodItem autoCorrect(String ingredient) {
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

    private static void checkDistance(String input, String tag, FoodItem food) {
        double tagDistance = levenshtein.distance(tag.toLowerCase(), input.toLowerCase());

        if (tagDistance <= MAX_DISTANCE) {
            if (tagDistance < shortestDistance) {
                matchingFood = food;
                System.out.println("tag: dist = " + shortestDistance);
                System.out.println("tag: matchingFood = " + matchingFood.screenName);
                shortestDistance = tagDistance;
            }
        }
    }
}
