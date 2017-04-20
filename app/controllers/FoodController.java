package controllers;

import algorithms.QuicksortFoodItem;
import models.food.FoodGroup;
import models.food.FoodItem;
import models.food.fineli.Food;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * HTTP controller handling all requests that have to do with
 * food data and their respective components.
 *
 * @author Fredrik Kindstrom
 */
public class FoodController extends Controller {

    // GET /food/:id
    public Result get(int id) {
        Food food;
        try {
            food = Food.find.where().eq("dataSourceId", id).findUnique();
        } catch (PersistenceException e) {
            return badRequest("No food with food number '" + id + "' in table Foods");
        }
        return ok(Json.toJson(food));
    }

    // GET /food/name/:name
    public Result getByName(String name) {
        List<Food> foods;
        try {
            foods = Food.find.where().contains("name", name).findList();
        } catch (PersistenceException e) {
            return badRequest("No food called '" + name + "' in table Foods");
        }
        return ok(Json.toJson(foods));
    }

    // GET /food/sort/:id
    public Result sortById(int id){
        Food food;
        List<Food> allFoodItems;
        try {
            food = Food.find.where().eq("dataSourceId", id).findUnique();
            allFoodItems = Food.find.all();
        } catch (PersistenceException e) {
            return badRequest("No food with food number '" + id + "' in table FoodItems");
        }

        List<Food> sortedFood = QuicksortFoodItem.sort(allFoodItems, food);
        // To get a good outprint on the webpage!
        String foodlist = food.getDataSourceId() + " " + food.name + "\n\n";
        for (Food f: sortedFood) {
            foodlist = foodlist + f.getDataSourceId() + " " +f.name +
                    " with diff: " + QuicksortFoodItem.diff(f,food) + "\n";
        }
        return ok(foodlist, "UTF-8");
    }
}
