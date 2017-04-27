package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helpers.JsonHelper;
import models.food.Nutrient;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-24.
 */
public class RecipeController extends Controller {

    // GET /recipes
    public Result getAll() {
        List<Recipe> recipes = Recipe.find.all();
        ArrayNode jsonArray = Json.newArray();
        for (Recipe recipe : recipes) {
            jsonArray.add(JsonHelper.toJson(recipe));
        }

        return ok(jsonArray);
    }
}
