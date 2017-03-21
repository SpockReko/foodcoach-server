package controllers;

import models.food.FoodItem;
import play.mvc.Controller;
import play.mvc.Result;
import parsers.IngredientParser;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ApplicationController extends Controller {

    public Result index() {

        return ok("It works!");
    }
}
