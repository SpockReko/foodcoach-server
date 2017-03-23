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

    private Double optimalMenuNutions = 1.0;
    private List optimalMenu = new ArrayList<Recipe>();
    private Double limitFromOptimalResult = 0.0;
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<List<Recipe>> weekmenuList = new ArrayList<>();
    private User user = new User();

    public int returnAllWeekMenus(int indexOfRecipes, List<Recipe> currentList){
        if (currentList.size() == 2){
            weekmenuList.add(currentList);
            return 1;
        }else if(indexOfRecipes < 0){
            return 0;
        }else{
            List<Recipe> newList = new ArrayList<>(currentList);
            currentList.add(allRecipes.get(indexOfRecipes));
            return returnAllWeekMenus(indexOfRecipes-1, currentList) +
                    returnAllWeekMenus(indexOfRecipes-1, newList);
        }

    }

    public Double calculateWeekMenu() {
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        for(List<Recipe> lr : weekmenuList){
            double value = nutritionValueCalculation(lr);
            if(value < optimalMenuNutions){
                optimalMenuNutions = value;
                optimalMenu = lr;
            }
        }
        return optimalMenuNutions;
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