package algorithms;

import models.food.FoodItem;
import models.recipe.Recipe;
import models.user.RDI;
import models.user.User;


import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;

/**
 * Created by stefa on 2017-02-28.
 */
public class WeekMenu {


    private Double optimalMenuNutrition = 1.0;
    private List optimalMenu = new ArrayList<Recipe>();
    private Double limitFromOptimalResult = 0.5;
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();

    private User user = new User();

    public Double calculateWeekMenu(int indexOfRecipeList, List<Recipe> chosenRecipes){
        Double firstValue = 1.0;
        Double secondValue = 1.0;
        System.out.println("calculateWeekMenu is called " + indexOfRecipeList);
        //If you are in the limit of the optimal value.
        if(optimalMenuNutrition > limitFromOptimalResult ){
            List<Recipe> newChosenRecipes = chosenRecipes;

            if(indexOfRecipeList >= 0){
                if(newChosenRecipes.size() < nrOfRecipes){
                    newChosenRecipes.add(allRecipes.get(indexOfRecipeList));
                    firstValue = calculateWeekMenu(indexOfRecipeList-1, newChosenRecipes);
                    secondValue = calculateWeekMenu(indexOfRecipeList-1, chosenRecipes);
                }
            }

            Double menuNutrition;
            //System.out.println("!!!!!!!!!!! " + firstValue + " ????? "+ secondValue);
            if(Math.max(firstValue, secondValue ) == firstValue){
                menuNutrition = nutritionValueCalculation(newChosenRecipes);
                chosenRecipes = newChosenRecipes;
            }else{
                menuNutrition =  nutritionValueCalculation(chosenRecipes);
                newChosenRecipes = chosenRecipes;
            }

            if(optimalMenuNutrition > menuNutrition){
                optimalMenuNutrition = menuNutrition;
                optimalMenu = chosenRecipes;
                System.out.println("Uppdaterar optimalmenu");
            }
        }

        return optimalMenuNutrition;
    }

    public Double nutritionValueCalculation(List<Recipe> chosenRecipes){
        Random r = new Random();
        return (0.01*r.nextInt(100));
        /*HashMap<RDI,Double> nutrientsNeed = user.hmap;
        HashMap<RDI,Double> nutrientsContent = Algorithms.nutrientsContent(chosenRecipes);
        return Algorithms.L2Norm(nutrientsNeed,nutrientsContent);
        */
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
        this.limitFromOptimalResult = desiredValue;
    }

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

}