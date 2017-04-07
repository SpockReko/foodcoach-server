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

    // GET /recipe/:id
    public Result optimizeByNumber(long recipeNumber) {
        Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User();
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.recipeToString(optimizedRecipe));
    }

    // GET /recipe/title/:title
    public Result optimizeByTitle(String title) {
        Recipe recipe = Recipe.find.where().eq("title", title).findUnique();
        User user = new User();
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.recipeToString(optimizedRecipe));
    }
}