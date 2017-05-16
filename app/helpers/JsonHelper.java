package helpers;

import algorithms.RecipeOptimizer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.food.Food;
import models.food.FoodGroup;
import models.food.Nutrient;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.ShoppingList;
import models.user.User;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts various models to a Json representation.
 *
 * @author Fredrik Kindstrom
 */
public class JsonHelper {

    /**
     * Converts a {@link Food} to Json.
     * @param food The food to convert.
     * @return The food represented as Json.
     */
    public static JsonNode toJson(Food food) {
        ObjectNode output = Json.newObject();
        output.put("id", food.getId());
        output.put("dataSource", food.getDataSource().name());
        output.put("dataSourceId", food.getDataSourceId());
        output.put("name", food.name);
        output.put("group", food.group.name);
        ArrayNode tags = output.putArray("tags");
        food.tags.forEach(tags::add);
        output.put("processing", food.processing == null ? null : food.processing.name());
        output.put("category", food.category == null ? null : food.category.name());
        return output;
    }

    /**
     * Converts a {@link User} to Json.
     * @param user The user to convert.
     * @return The user represented as Json.
     */
    public static JsonNode toJson(User user) {
        ObjectNode output = Json.newObject();
        output.put("id", user.id);
        output.put("name", user.firstName);
        output.put("sex", String.valueOf(user.sex));
        output.put("weight", user.weight);
        output.put("height", user.height);
        output.put("age", user.age);
        output.put("activityLevel", user.activityLevel);
        output.put("goal", String.valueOf(user.goal));
        ArrayNode array = output.putArray("nutrients");
        for (Map.Entry<Nutrient, Double> entry : user.hmap.entrySet() ) {
            Nutrient nutrient = entry.getKey();
            Double amount = entry.getValue();
            array.add(JsonHelper.toJson(nutrient, amount));
        }
        return output;
    }

    /**
     * Converts a {@link User} to Json.
     * @param menu The menu to convert.
     * @return The user represented as Json.
     */
    public static JsonNode toJson(Menu menu, ShoppingList shoppingList) {
        ObjectNode output = Json.newObject();
        ArrayNode array = output.putArray("recipes");
        for ( Recipe recipe : menu.getRecipeList()) {
            array.add(JsonHelper.toJson(recipe));
        }
        ArrayNode array2 = output.putArray("ingredients");
        for ( Ingredient ingredient : shoppingList.getIngredients()){
            array2.add(JsonHelper.toJson(ingredient));
        }
        return output;
    }

    /**
     * Converts a {@link User} to Json.
     * @param shoppingList The shoppinglist to convert.
     * @return The user represented as Json.
     */
    public static JsonNode toJson( ShoppingList shoppingList) {
        ObjectNode output = Json.newObject();
        ArrayNode array2 = output.putArray("ingredients");
        for ( Ingredient ingredient : shoppingList.getIngredients()){
            array2.add(JsonHelper.toJson(ingredient));
        }
        return output;
    }

    /**
     * Converts an {@link FoodGroup} to Json.
     * @param foodGroup The food group to convert.
     * @return The food group represented as Json.
     */
    public static JsonNode toJson(FoodGroup foodGroup) {
        ObjectNode output = Json.newObject();
        output.put("id", foodGroup.getId());
        output.put("name", foodGroup.name);
        ArrayNode searchTags = output.putArray("searchTags");
        foodGroup.searchTags.forEach(searchTags::add);
        output.set("defaultFood", toJson(foodGroup.defaultFood));
        if (!foodGroup.foods.isEmpty()) {
            ArrayNode foods = output.putArray("foods");
            for (Food food : foodGroup.foods) {
                foods.add(toJson(food));
            }
        }
        return output;
    }

    /**
     * Converts an {@link Ingredient} to Json.
     * @param ingredient The ingredient to convert.
     * @return The ingredient represented as Json.
     */
    public static JsonNode toJson(Ingredient ingredient) {
        ObjectNode output = Json.newObject();
        output.set("amount", Json.toJson(ingredient.getAmount()));
        output.put("comment", ingredient.comment);
        output.put("original", ingredient.original);
        output.put("kcal", ingredient.getNutrient(Nutrient.ENERGY_KCAL));
        output.put("co2", ingredient.getCO2());
        output.set("food", toJson(ingredient.getFood()));
        if (!ingredient.alternatives.isEmpty()) {
            ArrayNode alternatives = output.putArray("alternatives");
            for (Ingredient alt : ingredient.alternatives) {
                alternatives.add(toJson(alt));
            }
        }
        return output;
    }

    /**
     * Converts a {@link Recipe} to Json.
     * @param recipe The recipe to convert.
     * @return The recipe represented as Json.
     */
    public static JsonNode toJson(Recipe recipe) {
        ObjectNode output = Json.newObject();
        output.put("title", recipe.getTitle());
        output.put("portions", recipe.getPortions());
        output.put("energyKcalPerPortion", Math.round(recipe.getNutrientPerPortion(Nutrient.ENERGY_KCAL)));
        output.put("energyKcal", Math.round(recipe.getNutrient(Nutrient.ENERGY_KCAL)));
        output.put("co2PerPortion", Math.round(recipe.getCO2PerPortion() * 100));
        output.put("carbohydrates", Math.round(recipe.getNutrient(Nutrient.CARBOHYDRATES)));
        output.put("protein", Math.round(recipe.getNutrient(Nutrient.PROTEIN)));
        output.put("fat", Math.round(recipe.getNutrient(Nutrient.FAT)));
        output.put("vitaminA", Math.round(recipe.getNutrient(Nutrient.VITAMIN_A)));
        output.put("url", recipe.sourceUrl);
        ArrayNode array = output.putArray("ingredients");
        for (Ingredient ingredient : recipe.ingredients) {
            array.add(JsonHelper.toJson(ingredient));
        }

        ArrayNode array2 = output.putArray("nutrients");
        for (Map.Entry<Nutrient, Double> entry : recipe.getNutrientsContent().entrySet() ) {
            Nutrient nutrient = entry.getKey();
            Double amount = entry.getValue();
            array2.add(JsonHelper.toJson(nutrient, amount));
        }
        return output;
    }

    /**
     * Converts a {@link Nutrient} to Json.
     * @param nutrient The nutrient to convert.
     * @return The user represented as Json.
     */
    public static JsonNode toJson(Nutrient nutrient, double amount) {
        ObjectNode output = Json.newObject();
        output.put("name", nutrient.getName());
        output.put("type", String.valueOf(nutrient.getType()));
        output.put("unit", String.valueOf(nutrient.getUnit()));
        output.put("amount", amount);
        return output;
    }
}
