package controllers;

import algorithms.QuicksortFoodItem;
import com.fasterxml.jackson.databind.node.ArrayNode;
import helpers.JsonHelper;
import models.food.Food;
import models.food.FoodGroup;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * HTTP controller handling all requests that have to do with
 * food data and their respective components.
 *
 * @author Fredrik Kindstrom
 */
public class FoodController extends Controller {

    // GET /food/:id
    public Result getFoodById(int id) {
        Food food = Food.find.where().eq("dataSourceId", id).findUnique();
        if (food != null) {
            return ok(JsonHelper.toJson(food));
        } else {
            return badRequest("Food \"" + id + "\" does not exist");
        }
    }

    // GET /food/name/:name
    public Result getFoodByName(String name) {
        List<Food> foods = Food.find.where().contains("name", name).findList();
        if (!foods.isEmpty()) {
            ArrayNode array = Json.newArray();
            foods.forEach(food -> array.add(JsonHelper.toJson(food)));
            return ok(array);
        } else {
            return badRequest("No food with name \"" + name + "\" found");
        }
    }

    // GET /foodgroup/name/:name
    public Result getFoodGroupByName(String name) {
        List<FoodGroup> foodGroups = FoodGroup.find.where().contains("name", name).findList();
        if (!foodGroups.isEmpty()) {
            ArrayNode array = Json.newArray();
            foodGroups.forEach(group -> array.add(JsonHelper.toJson(group)));
            return ok(array);
        } else {
            return badRequest("No group with name \"" + name + "\" found");
        }
    }


    // GET /food/sort/:id
    public Result sortById(int id){
        Food food = Food.find.where().eq("dataSourceId", id).findUnique();
        List<Food> allFoodItems = Food.find.all();
        if (food == null) {
            return badRequest("Food \"" + id + "\" does not exist");
        }

        List<Food> sortedFood = QuicksortFoodItem.sort(allFoodItems, food);
        // To get a good outprint on the webpage!
        String foodList = food.getDataSourceId() + " " + food.name + "\n\n";
        for (Food f: sortedFood) {
            foodList = foodList + f.getDataSourceId() + " " +f.name +
                    " with diff: " + QuicksortFoodItem.diff(f,food) + "\n";
        }
        return ok(foodList, "UTF-8");
    }
}
