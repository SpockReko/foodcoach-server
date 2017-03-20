package tools;

import models.food.FoodItem;

import java.util.List;

import info.debatty.java.stringsimilarity.*;

import javax.naming.ldap.PagedResultsControl;

/**
 * Created by emmafahlen on 2017-02-14.
 */
public class IngredientToFood {

    private static final int MAX_DISTANCE = 3;
    private static double shortestDistance = Double.MAX_VALUE;
    private static Levenshtein l = new Levenshtein();
    private static FoodItem matchingFood = null;

    public static FoodItem ingToFood(String ingredient) {
        try {
            FoodItem food = FoodItem.find.where().contains("searchTags", ingredient).findUnique();

            if (food == null) {
                food = FoodItem.find.where().eq("name", ingredient).findUnique();
            }

            System.out.println("food = " + food);

            if (food == null) {
                String corrected = autoCorrect(ingredient.toLowerCase());
                if (!corrected.equals(ingredient.toLowerCase())) {
                    food = FoodItem.find.where().eq("screenName", corrected).findUnique();
                    if (food == null) {
                        food = FoodItem.find.where().eq("name", corrected).findUnique();
                    }
                    if (food != null) {
                        return food;
                    }
                    return new FoodItem(ingredient, FoodItem.find.findCount() * ingredient.hashCode());
                } else {
                    return new FoodItem(ingredient, FoodItem.find.findCount() * ingredient.hashCode());
                }
            } else {
                System.out.println("before return on else " + food);
                return food;
            }

        } catch (Exception ex) {
            System.out.println("Catchade exception!");
        }
        return null;
    }

    private static String autoCorrect(String ingredient) {
        System.out.println(l.distance("rödlök", ingredient));

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
        if (matchingFood != null) {
            return matchingFood.screenName;
        }
        return ingredient;
    }

    private static void checkDistance(String input, String tag, FoodItem food) {
        double tagDistance = l.distance(tag.toLowerCase(), input.toLowerCase());

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
