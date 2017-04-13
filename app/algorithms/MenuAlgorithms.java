package algorithms;

import models.food.FoodItem;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.Nutrient;
import models.user.User;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by stefa on 2017-02-28.
 */
public class MenuAlgorithms {

    private final Double LARGE_DISTANCE = 999999999.9999;
    private Double optimalMenuNutrition;
    private Menu optimalMenu = new Menu(new ArrayList<>());
    private Menu[] menus;
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Menu> weekMenuList = new ArrayList<>();
    private User user = new User();

    private List<Ingredient> notThisIngredients = new ArrayList<>();

    public MenuAlgorithms(User user, List<Recipe> recipeList){
        this.user = user;
        this.allRecipes = recipeList;
        menus = new Menu[1];
    }

    public MenuAlgorithms(User user, List<Recipe> recipeList, int numberOfMenus){
        this.user = user;
        this.allRecipes = recipeList;
        menus = new Menu[numberOfMenus];  //An extenstion to have more than one menu.
        for (Menu m: menus) {
            m = null;
        }

    }

    private void reset(){
        optimalMenuNutrition = LARGE_DISTANCE;
        optimalMenu = new Menu(new ArrayList<>());
        weekMenuList = new ArrayList<>();
    }

    public void addAllergies(Ingredient ingredient) {
        notThisIngredients.add(ingredient);
    }

    /**
     * Recursiv method with 2 base cases and a minimization of the value.
     * In the case we get a size that is good then we can
     * @param indexOfRecipes where in the recurtion we ar at
     * @param currentList the list we have at each recurtion
     * @param optimize The method we want to evaluate the menu on
     * @return
     */
    public double returnAllWeekMenus(int indexOfRecipes, List<Recipe> currentList, Function<Menu,Double> optimize){

        if (currentList.size() == nrOfRecipes){
            Menu menu = new Menu(currentList);
            double value = optimize.apply(menu);
            //int optimizeMenuIndex = getOptimalMenuIndex(optimize);
            //Menu optimizeMenu = menus[optimizeMenuIndex] == null ? menu : menus[optimizeMenuIndex];
            //if(value <= optimize.apply(optimizeMenu)) {
            if(value <= optimalMenuNutrition){
                optimalMenuNutrition = value;
                optimalMenu = menu;
            }
            return value;
        }else if(indexOfRecipes < 0){
            return optimalMenuNutrition+1.0;
        }else{
            List<Recipe> newList = new ArrayList<>(currentList);
            currentList.add(allRecipes.get(indexOfRecipes));
            return Math.min(returnAllWeekMenus(indexOfRecipes-1, currentList, optimize),
                    returnAllWeekMenus(indexOfRecipes-1, newList, optimize));
        }

    }

    /**
     * returns the least optimized value in menus
     * @param optimize
     * @return menu;
     */
    private int getOptimalMenuIndex(Function<Menu, Double> optimize) {
        Menu menu = menus[0];
        int returnIndex = 0;
        if( menu == null) return 0;

        for (int i = 0; i < menus.length; i++) {
            if(    menus[i] == null) return i;

            double mValue = optimize.apply(menus[i]);
            double menuValue = optimize.apply(menu);

            if(mValue>menuValue){ //The worse value is better!
                returnIndex = i;
            }
        }
        return returnIndex;
    }

    public Menu calculateWeekMenu(List<Recipe> notThisRecipes) {
        reset();
        filterRecipes(notThisIngredients,notThisRecipes);
        returnAllWeekMenus(
                allRecipes.size()-1,
                new ArrayList<>(),
                this::nutritionValueCalculation);
        return optimalMenu;
    }


    public Double nutritionValueCalculation(Menu chosenMenu){
        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = NutritionAlgorithms.nutrientsContent(chosenMenu);
        return NutritionAlgorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,chosenMenu);
    }

    private void filterRecipes(List<Ingredient> ingredientList, List<Recipe> recipeList){

        System.out.println("optimalMenuNutrition: " + optimalMenuNutrition +
                "\noptimalMenu size: " + optimalMenu.getRecipeList().size() +
                "\nnrOfRecipe: " + nrOfRecipes +
                "\nallRecipes size: " + allRecipes.size() +
                "\nweekMenuList size: " + weekMenuList.size() +
                "\nUser: " + user.firstName);
        System.out.println("*********************************************************start");

        System.out.println("#IngredientList in weekmenu at row 88 has size: " + ingredientList.size());
        List<Recipe> filteredRecipes = new ArrayList<>();
        System.out.println("#recipesList in weekmenu at parameter has size: " + recipeList.size());
        System.out.println("For loop starts! nr of Recepies: " + allRecipes.size());
        for (Recipe recipe : allRecipes){
            System.out.println("\nLoop for recipe " + recipe.getTitle());
            boolean badRecipe = false;
            for (Ingredient ingredient :ingredientList) {
                System.out.println("\t"+recipe.getTitle() + " see if if it have ingredient: " + ingredient);
                if(recipe.ingredients.contains(ingredient)){
                    badRecipe = true;
                    System.out.println("\t" + recipe.getTitle() + "is a bad recipe! Ingredient " + ingredient + "exist in allergies!!!**!!!");
                }
            }
            for (Recipe r: recipeList) {
                if(recipe.equals(r)){
                    badRecipe = true;
                    System.out.println("\t" + recipe.getTitle() + "is a bad recipe! You don't want this recepie!");
                }
            }
            if(!badRecipe) filteredRecipes.add(recipe);
            System.out.println("Now is the size of the filteredRecipes: " + filteredRecipes.size());
        }
        System.out.println("Now has all loops ended! the filterdList has size: " + filteredRecipes.size());
        for (Recipe r: filteredRecipes) {
            System.out.println("recipe: " + r.getTitle());
        }
        allRecipes = filteredRecipes;

        System.out.println("************************************************************slut");
    }

    private List<Ingredient> getIngredientsFromString(List<String> stringList) {
        List<Ingredient> ingredientList = new ArrayList<>();

        for (String name: stringList) {
            //TODO: Fix the error of the MYSQL call!
            List<FoodItem> foods;
            try {
                foods = FoodItem.find.where().contains("name", name).findList();
            }catch(Exception e){
                foods = null;
            }
            if( foods != null){
                for (FoodItem fi: foods) {
                    ingredientList.add(new Ingredient(fi,new Amount(100.0, Amount.Unit.GRAM)));
                }
            }
        }
        return ingredientList;
    }

    public int getNrOfRecipes(){ return nrOfRecipes;}

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }
}