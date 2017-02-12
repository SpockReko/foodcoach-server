package controllers;

import models.food.FoodItem;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-12.
 */
public class FoodController extends Controller {

	public Result get(int lmvNumber) {
		FoodItem food =
			FoodItem.find.where().ilike("lmv_food_number", String.valueOf(lmvNumber)).findUnique();
		return ok(Json.toJson(food));
	}

	public Result search(String attribute, String value) {
		List<FoodItem> foods;
		try {
			foods = FoodItem.find.where().contains(attribute, value).findList();
		} catch (PersistenceException e) {
			return badRequest("No attribute called '" + attribute + "' in table FoodItems");
		}
		return ok(Json.toJson(foods));
	}
}
