package controllers;

import models.recipe.Ingredient;
import parsers.IngredientParser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ParseController extends Controller {

    public Result parseFull(String input) {
        IngredientParser ingredientParser = new IngredientParser();
        List<Ingredient> ingredients = ingredientParser.parse(input);
        if (ingredients != null) {
            return ok(Json.toJson(ingredients));
        } else {
            return ok("Did not find ingredient");
        }
    }
}
