package controllers;

import models.food.FoodItem;
import parsers.IngredientParser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ParseController extends Controller {

    public Result parseIngredient(String str) {
        FoodItem item = IngredientParser.findMatch(str);
        if (item.example != null) {
            return ok("<font size=\"4\" color=\"blue\">"
                + "#" + item.getLmvFoodNumber() + " - "
                + item.screenName + " (exempelvis "
                + item.example + ")</font>")
                .as("text/html");
        } else if (item.screenName != null) {
            return ok("<font size=\"4\" color=\"green\">"
                + "#" + item.getLmvFoodNumber() + " - "
                + item.screenName + "</font>")
                .as("text/html");
        } else {
            return ok("<font size=\"4\" color=\"red\">"
                + item.getName() + "</font>")
                .as("text/html");
        }
    }
}
