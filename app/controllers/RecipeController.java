package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.recipe.Ingredient;
import models.recipe.NotLinkedRecipe;
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
            jsonArray.add(getJson(recipe));
        }

        return ok(jsonArray);
    }

    // GET /notlinked_recipes
    public Result getAllNotLinked() {
        List<NotLinkedRecipe> recipes = NotLinkedRecipe.find.all();
        return ok(Json.toJson(recipes));
    }

    private ObjectNode getJson(Recipe recipe) {
        ObjectNode json = Json.newObject();
        json.put("title", recipe.getTitle());
        json.put("portions", recipe.getPortions());
        json.put("energyKcalPerPortion", Math.round(recipe.getEnergyKcal()/recipe.getPortions()));
        json.put("energyKcal", Math.round(recipe.getEnergyKcal()));
        json.put("energyKj", Math.round(recipe.getEnergyKj()));
        json.put("carbohydrates", Math.round(recipe.getCarbohydrates()));
        json.put("protein", Math.round(recipe.getProtein()));
        json.put("fibre", Math.round(recipe.getFibre()));
        ArrayNode array = json.putArray("ingredients");
        for (Ingredient i : recipe.ingredients) {
            ObjectNode node = Json.newObject();
            node.put("name", i.getFood().name);
            node.put("amount", i.getAmount().getAmount() + " " + i.getAmount().getUnit().name());
            node.put("energyKcal", Math.round(i.getEnergyKcal()));
            node.put("energyKj", Math.round(i.getEnergyKj()));
            node.put("carbohydrates", Math.round(i.getCarbohydrates()));
            node.put("protein", Math.round(i.getProtein()));
            node.put("fibre", Math.round(i.getFibre()));
            array.add(node);
        }
        return json;
    }
}
