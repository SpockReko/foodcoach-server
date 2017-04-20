package controllers;

import models.food.FoodGeneral;
import models.recipe.Ingredient;
import models.recipe.NotLinkedRecipe;
import parsers.FoodParser;
import parsers.IngredientParser;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * HTTP controller that handles all general requests to the server.
 */
public class ParseController extends Controller {

    @Inject
    static WSClient ws;

    public Result parseIngredient(String str) {
        FoodParser foodParser = new FoodParser();
        FoodGeneral item = foodParser.findMatch(str);
        /*if (item.example != null) {
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
        }*/
        return  ok(item.name);
    }

    public Result parseFull(String input) {
        IngredientParser ingredientParser = new IngredientParser();
        Ingredient ingredient = ingredientParser.parse(input);
        if (ingredient != null) {
            return ok(Json.toJson(ingredient));
        }
        else { return ok("Did not find ingredient");}
    }

    public Result recipeInfo(String recipe) {
        IngredientParser parser = new IngredientParser();
        Ingredient ing = parser.parse(recipe);

        return ok(Json.toJson(ing));
    }

    public Result runParse() {
        IngredientParser parser = new IngredientParser();
        List<NotLinkedRecipe> notLinkedRecipes = NotLinkedRecipe.find.all();

        for (NotLinkedRecipe notLinkedRecipe : notLinkedRecipes) {
            List<Ingredient> taggedIngredients = new ArrayList<>();
            for (String string : notLinkedRecipe.ingredients) {
                Ingredient ingredient = parser.parse(string);
                if (ingredient != null) {
                    taggedIngredients.add(ingredient);
                }
            }
        }
        return ok("Finished!");
    }
}
