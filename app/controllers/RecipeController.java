package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by fredrikkindstrom on 2017-02-24.
 */
public class RecipeController extends Controller {

    // GET /recipe/:id
    public Result get(long id) {
        Recipe recipe = Recipe.find.byId(id);
        ObjectNode json = Json.newObject();
        json.put("title", recipe.getTitle());
        json.put("portions", recipe.getPortions());
        json.put("energyKcalPerPortion", Math.round(recipe.getEnergyKcal()/recipe.getPortions()));
        json.put("energyKcal", Math.round(recipe.getEnergyKcal()));
        json.put("energyKj", Math.round(recipe.getEnergyKj()));
        json.put("carbohydrates", Math.round(recipe.getCarbohydrates()));
        json.put("protein", Math.round(recipe.getProtein()));
        json.put("fibre", Math.round(recipe.getFibre()));
        json.put("wholeGrain", Math.round(recipe.getWholeGrain()));
        json.put("water", Math.round(recipe.getWater()));
        ArrayNode array = json.putArray("ingredients");
        for (Ingredient i : recipe.ingredients) {
            System.out.println(i.getFoodItem().getName());
            ObjectNode node = Json.newObject();
            node.put("name", i.getFoodItem().getName());
            node.put("amount", i.getAmount().getAmount() + " " + i.getAmount().getUnit().name());
            node.put("energyKcal", Math.round(i.getEnergyKcal()));
            node.put("energyKj", Math.round(i.getEnergyKj()));
            node.put("carbohydrates", Math.round(i.getCarbohydrates()));
            node.put("protein", Math.round(i.getProtein()));
            node.put("fibre", Math.round(i.getFibre()));
            node.put("wholeGrain", Math.round(i.getWholeGrain()));
            node.put("water", Math.round(i.getWater()));
            array.add(node);
        }

        return ok(json);
    }
}
