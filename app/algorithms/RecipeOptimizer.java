package algorithms;

import models.food.Food;
import models.food.Nutrient;
import models.recipe.*;
import models.user.User;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.*;

/**
 * Created by stefa on 2017-03-20.
 */
public class RecipeOptimizer {

    private Double lowestPercentageOfIngredient;
    private Recipe recipe;
    private List<Ingredient> ingredients;
    private Recipe optimizedRecipe;
    private RecipeSimplex recipeSimplex;
    private Recipe originalRecipe;
    User user;

    public RecipeOptimizer(Recipe recipe, User user) {
        originalRecipe = recipe.getRecipeInGram().getOnePortionRecipe();
        //this.recipe = recipe.getOnePortionRecipe();
        //this.recipe = recipe.getRecipeInGram();
        this.recipe = recipe.getRecipeInGram().getUserRecipe(user);
        this.ingredients = this.recipe.ingredients;
        this.user = user;
    }

    public Recipe optimizeRecipe(){
        System.out.println(recipe.getTitle());
        List<Double> leastAmountOfIngredients = leastAmountOfIngredients(ingredients);

        recipeSimplex = new RecipeSimplex();
        recipeSimplex.setLinearObjectiveFunction(ingredients);
        recipeSimplex.setConstraintsIngredients(leastAmountOfIngredients, 2);
        HashMap<Nutrient,Double> nutritionNeed = NutritionAlgorithms.nutrientsNeedScaled(user.hmap,1);
        recipeSimplex.setConstraintsNutrition(ingredients, nutritionNeed, 1.2);

        double[] optimalAmountOfIngredients = recipeSimplex.optimize();

        List<Ingredient> newIngredients = new ArrayList<>();
        // Changes amount of each ingredient in the recipe to the optimal amount
        for( int i=0; i<optimalAmountOfIngredients.length; i++ ){
            Food food = ingredients.get(i).getFood();
            Amount amount = new Amount(optimalAmountOfIngredients[i], ingredients.get(i).getAmount().getUnit());
            Ingredient ingredient = new Ingredient(food, amount);
            newIngredients.add(ingredient);
        }
        Recipe newRecipe = new Recipe(recipe.getTitle(), recipe.getPortions(), newIngredients);
        optimizedRecipe=newRecipe;
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
                leastAmountOfIngredients.add(i, ingredient.getAmount().getQuantity() * lowestPercentageOfIngredient);
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

    public double getDifferenceCO2(){
        return originalRecipe.getCO2()-optimizedRecipe.getCO2();
    }

    public double getOriginalNutrient(){
        double nut = sqrt(pow(originalRecipe.getNutrient(Nutrient.CARBOHYDRATES)/user.hmap.get(Nutrient.CARBOHYDRATES)-1,2)
                +pow(originalRecipe.getNutrient(Nutrient.PROTEIN)/user.hmap.get(Nutrient.PROTEIN)-1, 2)
                +pow(originalRecipe.getNutrient(Nutrient.FAT)/user.hmap.get(Nutrient.FAT)-1,2));
        return nut;
    }

    public double getOptiNutrient(){
        double nut = sqrt(pow(optimizedRecipe.getNutrient(Nutrient.CARBOHYDRATES)/user.hmap.get(Nutrient.CARBOHYDRATES)-1,2)
                +pow(optimizedRecipe.getNutrient(Nutrient.PROTEIN)/user.hmap.get(Nutrient.PROTEIN)-1,2)
                +pow(optimizedRecipe.getNutrient(Nutrient.FAT)/user.hmap.get(Nutrient.FAT)-1,2));
        return nut;
    }


/*
    public double getDifferenceNutrient(){
        List recipeOriginal= new ArrayList<Recipe>();
        recipeOriginal.add(originalRecipe);
        Menu menuOriginal=new Menu(recipeOriginal);
        List recipeOpti= new ArrayList<Recipe>();
        recipeOpti.add(optimizedRecipe);
        Menu menuOpti=new Menu(recipeOpti);
        return menuOriginal.getValue()-menuOpti.getValue();
    }
*/
    public Menu getMenu(){
        ArrayList<Recipe> recipeList =new ArrayList<Recipe>();
        recipeList.add(optimizedRecipe);
        Menu menu=new Menu(recipeList);
        NutritionAlgorithms.L2Norm(user.hmap, NutritionAlgorithms.nutrientsContent(menu), user.overdoseValues, menu );
        return menu;
    }
    public String toString(){
        Menu menu=getMenu();
        String string="Optimalt recept, "+optimizedRecipe.recipeToString(optimizedRecipe) +"\nKoldioxidutsläpp: "
                + Precision.round(optimizedRecipe.getCO2(), 3) + " kg\n\n"+"Originalrecept, "
                +originalRecipe.recipeToString(originalRecipe.getOnePortionRecipe()) +"\nKoldioxidutsläpp: "
                + Precision.round(originalRecipe.getCO2(), 3) + " kg"+"\nEnergi: "+round(optimizedRecipe.getNutrient(Nutrient.ENERGY_KCAL))+" kcal\n";
        if(recipeSimplex.exceedsCalorie()){
            string=string+"\nMer än 120% av kaloribehov\n";
        }
        string=string+menu.nutritionToString();
        string=string+"\n"+optimizedRecipe.recipeToString(optimizedRecipe.getInSameUnit(originalRecipe));

        return string;
    }
}
