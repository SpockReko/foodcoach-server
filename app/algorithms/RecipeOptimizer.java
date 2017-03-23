package algorithms;

import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.User;

import java.util.List;

/**
 * Created by stefa on 2017-03-20.
 */
public class RecipeOptimizer {

    public Recipe generateNewRecipe(List<Ingredient> list, User user){
        // Optimize the recipe with help of simplex method.
        // We need to define a limit for every ingrediense so it not becomes 0% of anything.
        return null;
    }

    public Recipe OptimizeARecipe(Recipe recipe, User user){
        // Optimize a recepie to satisfie Users need!
        // suggested way is:
        // * To see if the recipe is to much calories or to little for the user.
        // * Then we need to increase och decrease the volym of ingredients in a smart way.
        return null;
    }
}
