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

    public Result ingToFood(String str) {
        FoodItem item = IngredientToFood.findMatch(str);
        if (item.example != null) {
            return ok("<font size=\"4\" color=\"blue\">"
                + item.screenName + " (exempelvis "
                + item.example + ")</font>")
                .as("text/html");
        } else if (item.screenName != null) {
            return ok("<font size=\"4\" color=\"green\">"
                + item.screenName + "</font>")
                .as("text/html");
        } else {
            return ok("<font size=\"4\" color=\"red\">"
                + item.getName() + "</font>")
                .as("text/html");
        }
    }
}
