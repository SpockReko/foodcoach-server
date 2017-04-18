package algorithms;

import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
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
    private Recipe recipe;
    private List<Ingredient> ingredients;
    User user;

    public RecipeOptimizer(Recipe recipe, User user) {
        this.recipe = recipe;
        this.ingredients = recipe.ingredients;
        this.user = user;
    }

    public Recipe optimizeRecipe(){
        List<Double> leastAmountOfIngredients = leastAmountOfIngredients(ingredients);

        RecipeSimplex recipeSimplex = new RecipeSimplex();
        recipeSimplex.setLinearObjectiveFunction(ingredients);
        recipeSimplex.setConstraintsIngredients(leastAmountOfIngredients);
        HashMap<Nutrient,Double> nutritionNeed = NutritionAlgorithms.nutrientsNeedScaled(user.hmap,1);
        recipeSimplex.setConstraintsNutrition(ingredients, nutritionNeed);

        double[] optimalAmountOfIngredients = recipeSimplex.optimize();

        List<Ingredient> newIngredients = new ArrayList<>();
        // Changes amount of each ingredient in the recipe to the optimal amount
        for( int i=0; i<optimalAmountOfIngredients.length; i++ ){
            FoodItem foodItem = ingredients.get(i).getFoodItem();
            Amount amount = new Amount(optimalAmountOfIngredients[i], ingredients.get(i).getAmount().getUnit());
            Ingredient ingredient = new Ingredient(foodItem, amount);
            newIngredients.add(ingredient);
        }
        Recipe newRecipe = new Recipe(recipe.getTitle(), recipe.getPortions(), newIngredients);
        return newRecipe;
    }

    /*
    To set a lowest amount of each ingredient in the recipe
     */
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
