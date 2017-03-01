package controllers;

import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-24.
 */
public class RecipeController extends Controller {

    // GET /recipe/:id
    public Result get(long id) {
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(new Ingredient(FoodItem.find.byId(1862L), new Amount(340, Amount.Unit.GRAM)));
        Recipe recipe = new Recipe("Linsgryta", "En gryta.", 4, ingredients);
        return ok(Json.toJson(recipe));
    }
}
