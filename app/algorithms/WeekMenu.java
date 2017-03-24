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
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<List<Recipe>> weekmenuList = new ArrayList<>();
    private User user = new User();

    public WeekMenu(User user){
        this.user = user;
    }

    public int returnAllWeekMenus(int indexOfRecipes, List<Recipe> currentList){
        if (currentList.size() == nrOfRecipes){
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

    public List<Recipe> calculateWeekMenu() {
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        optimalMenuNutrition = nutritionValueCalculation(weekmenuList.get(0));
        for(List<Recipe> lr : weekmenuList){
            double value = nutritionValueCalculation(lr);
            System.out.println("Näringsvärdet för " + recipeListToString(lr) + "\n... är värdet: " + value);
            if(value <= optimalMenuNutrition){
                optimalMenuNutrition = value;
                optimalMenu = lr;
            }
        }
        return optimalMenu;
    }

    public Double nutritionValueCalculation(List<Recipe> chosenRecipes){
        //Random r = new Random();
        //return (0.01*r.nextInt(100));
        HashMap<RDI,Double> nutrientsNeed = user.hmap;
        HashMap<RDI,Double> nutrientsContent = Algorithms.nutrientsContent(chosenRecipes);
        return Algorithms.L2Norm(nutrientsNeed,nutrientsContent,chosenRecipes);

    }

    public String recipeListToString(List<Recipe> list){
        String text = "";
        for(Recipe r : list){
            text = text + "\n " + r.getTitle() ;
        }
        return text;
    }

    public int getNrOfRecipes(){ return nrOfRecipes;}

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

    public void setAllRecipes(List<Recipe> allRecipes) {
        this.allRecipes = allRecipes;
    }

}