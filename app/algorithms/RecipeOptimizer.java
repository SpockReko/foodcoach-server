package algorithms;

import models.recipe.Ingredient;
import models.recipe.IngredientAmount;
import models.recipe.Recipe;
import models.user.User;

import java.util.List;
import java.util.Map;

/**
 * Created by stefa on 2017-03-20.
 */
public class RecipeOptimizer {

    // The map include the given ingredient and number of least amount of th ingredient.
    public Recipe generateNewRecipe(List<IngredientAmount> list, User user){
        RecipeSimplex recipeSimplex = new RecipeSimplex();
        for (IngredientAmount ingredientAmount: list) {
            Ingredient ingredient = ingredientAmount.getIngredient();
            int amount = ingredientAmount.getAmount();
//            recipeSimplex.add(ingredient,amount);
        }
//        return recipeSimplex.generateNewRecipe;
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
