package controllers;

import helpers.JsonHelper;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.ShoppingList;
import play.api.libs.json.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by stefa on 2017-04-06.
 */
public class ShoppingListController extends Controller {

    // Get
    /* public Result createShoppinglist(Menu menu) {

        ShoppingList shoppingList = new ShoppingList(menu);

        return ok(JsonHelper.toJson(shoppingList));

        List<Recipe> removeRecipeList = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();

        List<Recipe> allRecipes = Recipe.find.all();
        MenuAlgorithms menuAlgorithmsInstant = new MenuAlgorithms(allRecipes, removeRecipeList, nrOfRecipes);
        menuAlgorithmsInstant.setNrOfRecipes(nrOfRecipes);
        Menu resultingMenu = menuAlgorithmsInstant.calculateMenuNutrition(user);
        ShoppingList shoppingList = new ShoppingList(resultingMenu, ingredients);


        if (resultingMenu.getRecipeList().size() == menuAlgorithmsInstant.getNrOfRecipes())
            //return ok(user.firstName + " " + resultingMenu.recipeListToString(new ShoppingList(resultingMenu)));
            return ok(JsonHelper.toJson(resultingMenu, shoppingList));

            //return ok("nothing found!");
        else
            return null;


    } */

    // GET
    public Result removeIngredient(Ingredient ingredient, ShoppingList shoppingList){

    shoppingList.removeIngredient(ingredient);
    return
            ok(JsonHelper.toJson(shoppingList));
    }

}
