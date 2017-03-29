package algorithms;

import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.Nutrient;
import models.user.User;
import org.jetbrains.annotations.NotNull;


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
    private List<Recipe> notThisRecipes = new ArrayList<>(); //TODO: Dummy list!

    public WeekMenu(User user, List<Recipe> recipeList){
        this.user = user;
        this.allRecipes = recipeList;
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
        filterRecepies(user.allergier,notThisRecipes);
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

    private void filterRecepies(List<String> stringList, List<Recipe> recipeList){

        List<Ingredient> ingredientList = getIngredientsFromString(stringList);
        List<Recipe> filteredRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes){
            boolean badRecipe = false;
            for (Ingredient ingredient :ingredientList) {
                if(recipe.ingredients.contains(ingredient)) badRecipe = true;
            }
            for (Recipe r: recipeList) {
                if(recipe.equals(r)) badRecipe = true;
            }
            if(!badRecipe) filteredRecipes.add(recipe);
        }
        allRecipes = filteredRecipes;
    }

    @NotNull
    private List<Ingredient> getIngredientsFromString(List<String> stringList) {
        List<Ingredient> ingredientList = new ArrayList<>();

        for (String name: stringList) {
            List<FoodItem> foods = FoodItem.find.where().contains("name", name).findList();
            if( foods != null){
                for (FoodItem fi: foods) {
                    ingredientList.add(new Ingredient(fi,new Amount(100.0, Amount.Unit.GRAM)));
                }
            }

        } return ingredientList;
    }

    public int getNrOfRecipes(){ return nrOfRecipes;}

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

}