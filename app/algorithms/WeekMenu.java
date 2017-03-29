package algorithms;

import models.food.FoodItem;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.Nutrient;
import models.user.User;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Created by stefa on 2017-02-28.
 */
public class WeekMenu {

    private final Double LARGE_DISTANCE = 999999999.9999;
    private Double optimalMenuNutrition = LARGE_DISTANCE;
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
            System.out.println("Nutrients for " + recipeListToString(menu) + "\n... has the value: " + value);
            if(value <= optimalMenuNutrition){
                optimalMenuNutrition = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }


    public Double nutritionValueCalculation(Menu chosenMenu){
        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = Algorithms.nutrientsContent(chosenMenu);
        return Algorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,chosenMenu);
    }


    public String recipeListToString(Menu menu){
        String text = "";
        for(Recipe r : menu.getRecipeList()){
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\n";
        for(String comment : menu.getCommentList()){
            text = text + comment + "\n";
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