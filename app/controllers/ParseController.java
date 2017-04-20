package controllers;

import models.recipe.Ingredient;
import parsers.IngredientStringParser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ParseController extends Controller {

    public Result parseFull(String input) {
        IngredientStringParser parser = new IngredientStringParser();
        List<Ingredient> ingredients = parser.parse(input);
        if (ingredients != null) {
            return ok(Json.toJson(ingredients));
        } else {
            return ok("Did not find ingredient");
        }
    }

    public Result parse(String input) {
        IngredientStringParser parser = new IngredientStringParser();
        parser.parse(input);
        return ok("Check prints!");
    }
}
