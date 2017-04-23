package controllers;

import algorithms.NutritionAlgorithms;
import algorithms.RecipeOptimizer;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.ShoppingList;
import models.user.User;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

/**
 * Created by louiserost on 2017-04-05.
 */
public class RecipeOptimizationController extends Controller {

    // GET /recipe/optimize/:number
    public Result optimizeByNumber(long recipeNumber) {
        Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User();
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();
        Menu menu = recipeOptimizerInstant.getMenu();

        return ok(recipeOptimizerInstant.toString());
    }

    // GET /recipe/optimize/title/:title
    public Result optimizeByTitle(String title) {
        Recipe recipe = Recipe.find.where().eq("title", title).findUnique();
        User user = new User();
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.toString());
    }

    public Result optimizeByNumberBengt(long recipeNumber) {
        Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User("Bengt");
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.toString());
    }

    public Result optimizeByNumberAnna(long recipeNumber) {
        Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User("Anna");
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.toString());
    }
}
