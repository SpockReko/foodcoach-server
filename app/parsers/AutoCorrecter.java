package parsers;

import java.util.List;

import info.debatty.java.stringsimilarity.*;
import models.food.FoodGroup;
import play.Logger;

/**
 * Created by emmafahlen on 2017-02-14.
 */
class AutoCorrecter {

    private int maxDistance = 1;

    private final Levenshtein levenshtein = new Levenshtein();
    private FoodGroup matchingFood = null;
    private double shortestDistance = Double.MAX_VALUE;

    FoodGroup autoCorrect(String ingredient) {
        List<FoodGroup> items = FoodGroup.find.select("searchTags").findList();
        for (FoodGroup food : items) {
            List<String> tags = food.searchTags;
            tags.add(food.name.toLowerCase());
            for (String tag : tags) {
                checkDistance(ingredient, tag, food);
            }
        }

        return matchingFood;
    }

    private void checkDistance(String input, String tag, FoodGroup food) {
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
