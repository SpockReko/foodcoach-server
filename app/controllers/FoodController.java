package controllers;

import algorithms.QuicksortFoodItem;
import models.food.FoodGroup;
import models.food.FoodItem;
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
    public Result get(int lmvNumber) {
        FoodItem food;
        try {
            food = FoodItem.find.where().eq("lmv_food_number", lmvNumber).findUnique();
        } catch (PersistenceException e) {
            return badRequest("No food with food number '" + lmvNumber + "' in table FoodItems");
        }
        return ok(Json.toJson(food));
    }

    // GET /food/name/:name
    public Result getByName(String name) {
        List<FoodItem> foods;
        try {
            foods = FoodItem.find.where().contains("name", name).findList();
        } catch (PersistenceException e) {
            return badRequest("No food called '" + name + "' in table FoodItems");
        }
        return ok(Json.toJson(foods));
    }

    // GET /food/group/:code
    public Result getByGroup(String code) {
        List<FoodGroup> groups;
        try {
            groups = FoodGroup.find.where().eq("langualCode", code).findList();
        } catch (PersistenceException e) {
            return badRequest("No food group with code '" + code + "' in table FoodGroups");
        }
        return ok(Json.toJson(groups));
    }

    // GET /food/sort/:id
    public Result sortById(int id){
        FoodItem food;
        List<FoodItem> allFoodItems;
        try {
            food = FoodItem.find.where().eq("lmv_food_number", id).findUnique();
            allFoodItems = FoodItem.find.all();
        } catch (PersistenceException e) {
            return badRequest("No food with food number '" + id + "' in table FoodItems");
        }

        List<FoodItem> sortedFood = QuicksortFoodItem.sort(allFoodItems, food);
        // To get a good outprint on the webpage!
        String foodlist = food.getLmvFoodNumber() + " " + food.getName() + "\n\n";
        for (FoodItem f: sortedFood) {
            foodlist = foodlist + f.getLmvFoodNumber() + " " +f.getName() +
                    " with diff: " + QuicksortFoodItem.diff(f,food) + "\n";
        }
        return ok(foodlist, "UTF-8");
    }
}
