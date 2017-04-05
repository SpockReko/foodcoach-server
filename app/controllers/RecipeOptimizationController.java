package controllers;

import algorithms.RecipeOptimizer;
import models.recipe.Recipe;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by louiserost on 2017-04-05.
 */
public class RecipeOptimizationController extends Controller {

    public Result recipeOptimization() {
        Recipe recipe = Recipe.find.byId(1L);
        User user = new User();
        RecipeOptimizer recipeOptimizer = new RecipeOptimizer(recipe, user);
        recipeOptimizer.setLowestPercentageOfIngredient(0.75D);
        recipeOptimizer.generateNewRecipe();

        return ok();
    }
}
