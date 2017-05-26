package controllers;
import algorithms.RecipeOptimizer;
import models.food.Nutrient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

/**
 * Created by macbookl on 25/05/17.
 */
public class RecipeOptimizerTestController extends Controller {

        public Result test() {
            return ok(test1()+"\n"+test2());
        }
        private String test1(){
            int nbrOfRecipes=200;
            double differenceCO2=0;
            double optiNut=0;
            double originalNut=0;
            User user=new User();
            //User user = new User(User.Sex.FEMALE, 1.5, 50, 160, 21, User.Goal.STAY, new ArrayList<String>());
            for(int i=1; i<=nbrOfRecipes; i++){
                long recipeNumber=i;
                Recipe recipe = Recipe.find.byId(recipeNumber);
                RecipeOptimizer recipeOptimizer = new RecipeOptimizer(recipe, user);
                recipeOptimizer.setLowestPercentageOfIngredient(0.75D);
                recipeOptimizer.optimizeRecipe();
                differenceCO2 += recipeOptimizer.getDifferenceCO2();
                optiNut += recipeOptimizer.getOptiNutrient();
                originalNut += recipeOptimizer.getOriginalNutrient();
                System.out.println(recipe.getTitle()+" "+i+" "+recipeOptimizer.getDifferenceCO2());
            }
            double resultCO2=differenceCO2/nbrOfRecipes;
            double resultNutritionOpti=optiNut/nbrOfRecipes;
            double resultNutritionOriginal=originalNut/nbrOfRecipes;

            return "Avarage difference CO2: "+resultCO2+"\nAvarage difference nutrition optimal: "+resultNutritionOpti
                +"\nAvarage difference nutrition original: "+ resultNutritionOriginal+"\n"+user.firstName;
        }
    private String test2(){
        int nbrOfRecipes=200;
        int nbrOfRecipesPassed=0;
        double differenceCO2=0;
        double optiNut=0;
        double originalNut=0;
        User user=new User();
        //User user = new User(User.Sex.FEMALE, 1.5, 50, 160, 21, User.Goal.STAY, new ArrayList<String>());
        for(int i=1; i<=nbrOfRecipes; i++){
            long recipeNumber=i;
            Recipe recipe = Recipe.find.byId(recipeNumber);
            if(recipe.getRecipeInGram().getOnePortionRecipe().getNutrient(Nutrient.CARBOHYDRATES)>user.hmap.get(Nutrient.CARBOHYDRATES)*0.2
                    && recipe.getRecipeInGram().getOnePortionRecipe().getNutrient(Nutrient.PROTEIN)>user.hmap.get(Nutrient.PROTEIN)*0.2
                    && recipe.getRecipeInGram().getOnePortionRecipe().getNutrient(Nutrient.FAT)>user.hmap.get(Nutrient.FAT)*0.2) {
                nbrOfRecipesPassed++;
                RecipeOptimizer recipeOptimizer = new RecipeOptimizer(recipe, user);
                recipeOptimizer.setLowestPercentageOfIngredient(0.75D);
                recipeOptimizer.optimizeRecipe();
                differenceCO2 += recipeOptimizer.getDifferenceCO2();
                optiNut += recipeOptimizer.getOptiNutrient();
                originalNut += recipeOptimizer.getOriginalNutrient();
                System.out.println(recipe.getTitle() + " " + i + " " + recipeOptimizer.getDifferenceCO2());
            }
        }
        double resultCO2=differenceCO2/nbrOfRecipes;
        double resultNutritionOpti=optiNut/nbrOfRecipes;
        double resultNutritionOriginal=originalNut/nbrOfRecipes;

        return "Avarage difference CO2: "+resultCO2+"\nAvarage difference nutrition optimal: "+resultNutritionOpti
                +"\nAvarage difference nutrition original: "+ resultNutritionOriginal+"\nNumber of recipes passed: "+nbrOfRecipesPassed;
    }

}
