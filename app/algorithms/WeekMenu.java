package algorithms;

import models.food.FoodItem;
import models.recipe.Recipe;
import models.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;


/**
 * Created by stefa on 2017-02-28.
 */
public class WeekMenu {

    private List<Recipe> optimalMenu = new ArrayList<Recipe>();
    private List<Recipe> allRecipes = new ArrayList<Recipe>();

    private Double optimalMenuNutrition = 0.0;
    private Double desiredValue = 0.0;
    private int nrOfRecipes;

    private User user = new User();

    public Double calculateWeekMenu(int indexOfRecipeList, List<Recipe> chosenRecipes){

        Double firstValue = 1.0;
        Double secondValue = 1.0;

        if(optimalMenuNutrition <= desiredValue ){
            List<Recipe> newChosenRecipes = chosenRecipes;

            if(indexOfRecipeList >= 0){
                if(newChosenRecipes.size() < nrOfRecipes){
                    newChosenRecipes.add(allRecipes.get(indexOfRecipeList));
                    firstValue = calculateWeekMenu(indexOfRecipeList-1, newChosenRecipes);
                    secondValue = calculateWeekMenu(indexOfRecipeList-1, chosenRecipes);
                }
            }
            Double menuNutrition;
            if(Math.max(firstValue, secondValue ) == firstValue){
                menuNutrition = nutritionValueCalculation(newChosenRecipes);
                chosenRecipes = newChosenRecipes;
            }else{
                menuNutrition =  nutritionValueCalculation(chosenRecipes);
            }
            if(optimalMenuNutrition <= menuNutrition){
                optimalMenuNutrition = menuNutrition;
                optimalMenu = chosenRecipes;
            }
        }
        return optimalMenuNutrition;
    }

    public Double nutritionValueCalculation(List<Recipe> chosenRecipes){
        /*Random r = new Random();
        return (0.01*r.nextInt(100));*/
        HashMap<String,Double> nutrientsNeed = user.hmap;
        HashMap<String,Double> nutrientsContent = Algorithms.nutrientsContent(chosenRecipes);
        return Algorithms.L2Norm(nutrientsNeed,nutrientsContent);
    }


    public int getNrOfRecipes() {
        return nrOfRecipes;
    }

    public List<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public List getOptimalMenu() {
        return optimalMenu;
    }

    // Exemple of how to get values..
    private void exemple() {
        // N är värdet av av aska i första råvaran!
        FoodItem food = FoodItem.find.where().eq("lmv_food_number", 1).findUnique();
        float n = food.getAsh();
    }


    public void setDesiredValue(Double desiredValue) {
        this.desiredValue = desiredValue;
    }

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

}
