package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.food.Food;
import models.food.FoodGroup;
import play.libs.Json;

/**
 * Converts various models to a Json representation.
 * @author Fredrik Kindstrom
 */
public class JsonHelper {

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
        output.put("co2", food.getCO2());
        return output;
    }

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
}
