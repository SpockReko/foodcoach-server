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

    private User user = new User();

    public Double calculateWeekMenu(int indexOfRecipeList, List<Recipe> chosenRecipes) {
        Double firstValue = 1.0;
        Double secondValue = 1.0;
        System.out.println("calculateWeekMenu is called " + indexOfRecipeList);
        //If you are in the limit of the optimal value.
        if (optimalMenuNutions > limitFromOptimalResult) {
            if (indexOfRecipeList >= 0) { // Then there is recipes left
                if (chosenRecipes.size() < nrOfRecipes) {
                    //New list to add a recipe to!
                    List<Recipe> newChoicenRecipes = chosenRecipes;
                    //Add a recipe
                    newChoicenRecipes.add(allRecipes.get(indexOfRecipeList));

                    if(nutritionValueCalculation(chosenRecipes) < optimalMenuNutions
                            && chosenRecipes.size() == nrOfRecipes) {
                        optimalMenu = chosenRecipes;
                        optimalMenuNutions = nutritionValueCalculation(chosenRecipes);
                    }
                    if(nutritionValueCalculation(newChoicenRecipes) < optimalMenuNutions
                            && chosenRecipes.size() == nrOfRecipes){
                        optimalMenu = chosenRecipes;
                        optimalMenuNutions = nutritionValueCalculation(newChoicenRecipes);
                    }

                    // Solve the same problem with the current recipe added
                    return Math.min(calculateWeekMenu(indexOfRecipeList - 1, newChoicenRecipes),
                    // Solve the same problem without the current recipe added
                    calculateWeekMenu(indexOfRecipeList - 1, chosenRecipes));

                } else {
                    return nutritionValueCalculation(chosenRecipes);
                }
            } else if (chosenRecipes.size() != nrOfRecipes) { //Then list is empty
                return 10.0;
            } else{
                return nutritionValueCalculation(chosenRecipes);
            }
        }else{
            return 10.0;
        }
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