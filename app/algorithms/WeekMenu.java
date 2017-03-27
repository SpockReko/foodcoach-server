package algorithms;

import models.food.FoodItem;
import models.recipe.Menu;
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
    private Menu optimalMenu = new Menu(new ArrayList<Recipe>());
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Menu> weekMenuList = new ArrayList<>();
    private User user = new User();

    public WeekMenu(User user){
        this.user = user;
    }

    public int returnAllWeekMenus(int indexOfRecipes, List<Recipe> currentList){
        if (currentList.size() == nrOfRecipes){
            weekMenuList.add(new Menu(currentList));
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

    public Menu calculateWeekMenu() {
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        optimalMenuNutrition = nutritionValueCalculation(weekMenuList.get(0));
        for(Menu menu : weekMenuList){
            double value = nutritionValueCalculation(menu);
            System.out.println("Näringsvärdet för " + recipeListToString(menu) + "\n... är värdet: " + value);
            if(value <= optimalMenuNutrition){
                optimalMenuNutrition = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }

    public Double nutritionValueCalculation(Menu chosenRecipes){
        //Random r = new Random();
        //return (0.01*r.nextInt(100));
        HashMap<RDI,Double> nutrientsNeed = user.hmap;
        HashMap<RDI,Double> nutrientsContent = Algorithms.nutrientsContent(chosenRecipes);
        return Algorithms.L2Norm(nutrientsNeed,nutrientsContent,chosenRecipes);

    }

    public String recipeListToString(Menu menu){
        String text = "";
        for(Recipe r : menu.getRecipeList()){
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