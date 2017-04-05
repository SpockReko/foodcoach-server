package algorithms;

import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.Nutrient;
import models.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stefa on 2017-03-20.
 */
public class RecipeOptimizer {

    private Double lowestPercentageOfIngredient;

    // The map include the given ingredient and number of least amount of th ingredient.
    public Recipe generateNewRecipe(List<Ingredient> ingredients, User user){
        HashMap<Integer,Ingredient> indexOfIngredient;
        List<Double> leastAmountOfIngredient = leastAmountOfIngredient(ingredients);

        RecipeSimplex recipeSimplex = new RecipeSimplex();
        recipeSimplex.addConstraint(leastAmountOfIngredient);
        /*
        for (IngredientAmount ingredientAmount: list) {
            Ingredient ingredient = ingredientAmount.getIngredient();
            int amount = ingredientAmount.getAmount();
            recipeSimplex.add(ingredient,amount);
        }
//        return recipeSimplex.generateNewRecipe;*/
        return null;
    }

    public Recipe OptimizeARecipe(Recipe recipe, User user){
        List<Recipe> r = new ArrayList<>();
        r.add(recipe);
        HashMap<Nutrient,Double> nutrients = NutritionAlgorithms.nutrientsContent(new Menu(r));
        HashMap<Nutrient,Double> userNeeds = user.hmap;



        // Optimize a recepie to satisfie Users need!
        // suggested way is:
        // * To see if the recipe is to much calories or to little for the user.
        //   * If it to little: Sort fooditems in highest consetration of missing nutrious values to the least and
        //   * If it to much sort ingredience with % and highest to lowest
        return null;
    }

    private List<Double> leastAmountOfIngredient(List<Ingredient> ingredients) {
        List<Double> leastAmountOfIngredient = new ArrayList<>();
        for( int i=0; i<ingredients.size(); i++ ) {
            Ingredient ingredient = ingredients.get(i);
            leastAmountOfIngredient.set(i,ingredient.getAmount().getAmount()*lowestPercentageOfIngredient);
        }
        return leastAmountOfIngredient;
    }

    public void setLowestPercentageOfIngredient(Double lowestPercentageOfIngredient) {
        this.lowestPercentageOfIngredient = lowestPercentageOfIngredient;
    }

    public Double getLowestPercentageOfIngredient(){
        return lowestPercentageOfIngredient;
    }

}
