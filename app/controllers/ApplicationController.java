package controllers;

import models.food.FoodItem;
import play.mvc.Controller;
import play.mvc.Result;
import tools.IngredientToFood;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }

    public Result ingToFood (String str){
		FoodItem item = IngredientToFood.ingToFood(str);
		return ok(item.getName());
	}
}
