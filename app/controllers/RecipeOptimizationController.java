package controllers;

import algorithms.NutritionAlgorithms;
import algorithms.RecipeOptimizer;
import helpers.JsonHelper;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.ShoppingList;
import models.user.User;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import play.api.libs.json.Json;
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
        User user = new User("Olof");
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();
        Menu menu = recipeOptimizerInstant.getMenu();

        return ok(recipeOptimizerInstant.toString());
    }

    // GET /recipe/optimize/title/:title
    public Result optimizeByTitle(String title) {
        Recipe recipe = Recipe.find.where().eq("title", title).findUnique();
        User user = new User("Olof");
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.toString());
    }

    /* public Result optimizeByNumberBob(long recipeNumber) {
        Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User("Bob");
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.toString());
    }

    public Result optimizeByNumberAlice(long recipeNumber) {
        Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User("Alice");
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        return ok(recipeOptimizerInstant.toString());
    } */

    // GET   /recipe/optimize/user/:number/:name
    public Result optimizeByNumberUserName(String title, String userName) {
        Recipe recipe = Recipe.find.where().eq("title", title).findUnique();
        // Recipe recipe = Recipe.find.byId(recipeNumber);
        User user = new User(userName);
        //User user = User.find.where().eq("firstName", userName).findUnique();
        //User user = User.getUserByName2(userName);
        RecipeOptimizer recipeOptimizerInstant = new RecipeOptimizer(recipe, user);
        recipeOptimizerInstant.setLowestPercentageOfIngredient(0.75D);
        Recipe optimizedRecipe = recipeOptimizerInstant.optimizeRecipe();

        //return ok(recipeOptimizerInstant.toString());
        return ok(JsonHelper.toJson(optimizedRecipe));
    }
}
