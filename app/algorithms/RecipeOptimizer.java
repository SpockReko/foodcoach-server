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
    private List<Ingredient> ingredients;
    User user;

    public RecipeOptimizer(Recipe recipe, User user) {
        ingredients = recipe.ingredients;
        this.user = user;
    }

    public Recipe generateNewRecipe(){
        List<Double> leastAmountOfIngredients = leastAmountOfIngredients(ingredients);

        RecipeSimplex recipeSimplex = new RecipeSimplex();
        recipeSimplex.addLinearObjectiveFunction(ingredients);
        recipeSimplex.addConstraint(leastAmountOfIngredients);
        double[] optimalAmountOfIngredients = recipeSimplex.optimize();

        return null;
    }


    private List<Double> leastAmountOfIngredients(List<Ingredient> ingredients) {
        List<Double> leastAmountOfIngredients = new ArrayList<>();
        for( int i=0; i<ingredients.size(); i++ ) {
            Ingredient ingredient = ingredients.get(i);
            if( lowestPercentageOfIngredient != null ) {
                leastAmountOfIngredients.add(i, ingredient.getAmount().getAmount() * lowestPercentageOfIngredient);
            }
        }
        return leastAmountOfIngredients;
    }

    public void setLowestPercentageOfIngredient(Double lowestPercentageOfIngredient) {
        this.lowestPercentageOfIngredient = lowestPercentageOfIngredient;
    }

    public Double getLowestPercentageOfIngredient(){
        return lowestPercentageOfIngredient;
    }

}
