package algorithms;

import models.food.FoodItem;
import models.recipe.Recipe;
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

    private Double optimalMenuNutions = 1.0;
    private List optimalMenu = new ArrayList<Recipe>();
    private Double limitFromOptimalResult = 0.0;
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();

    private User user = new User();

    public Double calculateWeekMenu(int indexOfRecepieList, List<Recipe> chosenRecipes){
        Collections.shuffle(allRecipes);
        Double firstValue = 1.0;
        Double secondValue = 1.0;

        //If you are in the limit of the optimal value.
        if(optimalMenuNutions < limitFromOptimalResult ){
            List<Recipe> newChoicenRecipes = chosenRecipes;

            if(indexOfRecepieList > 0){
                if(newChoicenRecipes.size() < nrOfRecipes){
                    newChoicenRecipes.add(allRecipes.get(indexOfRecepieList));
                    firstValue = calculateWeekMenu(indexOfRecepieList-1, newChoicenRecipes);
                    secondValue = calculateWeekMenu(indexOfRecepieList-1, chosenRecipes);
                }
            }

            Double menuNutions = 0.0;

            Double menuNutrition;
            if(Math.max(firstValue, secondValue ) == firstValue){
                menuNutrition = nutritionValueCalculation(newChoicenRecipes);
                chosenRecipes = newChoicenRecipes;
            }else{
                menuNutrition =  nutritionValueCalculation(chosenRecipes);
            }
            if(optimalMenuNutions <= menuNutrition){
                optimalMenuNutions = menuNutrition;
                optimalMenu = chosenRecipes;
            }
        }
        return optimalMenuNutions;
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
        this.limitFromOptimalResult = desiredValue;
    }

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

}