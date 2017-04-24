package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.food.Food;
import models.food.FoodGroup;
import models.food.Nutrient;
import models.recipe.Ingredient;
import play.libs.Json;

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
        output.put("scientificName", food.scientificName);
        output.put("exampleBrands", food.exampleBrands);
        output.put("pieceWeightGrams", food.pieceWeightGrams);
        output.put("densityConstant", food.densityConstant);
        output.put("processing", food.processing == null ? null : food.processing.name());
        output.put("category", food.category == null ? null : food.category.name());
        ArrayNode diets = output.putArray("diets");
        food.diets.forEach(d -> diets.add(d.type.name()));
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
        output.put("defaultFood", toJson(foodGroup.defaultFood));
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
        output.put("food", toJson(ingredient.getFood()));
        output.put("amount", Json.toJson(ingredient.getAmount()));
        output.put("comment", ingredient.comment);
        output.put("kcal", ingredient.getEnergyKcal());
        return output;
    }
}
