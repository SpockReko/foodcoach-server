package algorithms;

import models.food.FoodItem;
import models.recipe.*;
import models.user.Nutrient;
import models.user.User;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.function.Function;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by stefa on 2017-02-28.
 */
public class MenuAlgorithms {

    private final Double LARGE_DISTANCE = 999999999.9999;
    private final int DEFAULT_VALUE_MENUS = 3;
    private Double optimalMenuNutrition;
    private int optimalValue;
    private Menu optimalMenu = new Menu(new ArrayList<>());
    private Menu[] menus;
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Menu> weekMenuList = new ArrayList<>();
    private User user = new User();
    private List<FoodItem> foodItemList;
    private List<Amount> amountList;
    private List<Ingredient> notTheeseIngredients = new ArrayList<>();

    public MenuAlgorithms(User user, List<Recipe> recipeList){
        this.user = user;
        this.allRecipes = recipeList;
        menus = new Menu[DEFAULT_VALUE_MENUS];
    }

    public MenuAlgorithms(User user, List<Recipe> recipeList, int numberOfMenus){
        this.user = user;
        this.allRecipes = recipeList;
        menus = new Menu[numberOfMenus];
    }
    public MenuAlgorithms(List<FoodItem> foodItemList, List<Recipe> recipeList){
        this.foodItemList=foodItemList;
        this.allRecipes=recipeList;
    }

    //Listorna måste vara lika stora
    public MenuAlgorithms(List<FoodItem> foodItemList, List<Amount> amountList, List<Recipe> recipeList){
        this.foodItemList=new ArrayList<>(foodItemList);
        this.amountList=new ArrayList<>(amountList);
        this.allRecipes=recipeList;
    }

    private void reset(){
        optimalMenuNutrition = LARGE_DISTANCE;
        optimalMenu = new Menu(new ArrayList<>());
        weekMenuList = new ArrayList<>();
        optimalValue=0;
    }

    public void addAllergies(Ingredient ingredient) {
        notTheeseIngredients.add(ingredient);
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
            int optimizeMenuIndex = getOptimalMenuIndex(optimize);
            Menu optimizeMenu = menus[optimizeMenuIndex] == null ? menu : menus[optimizeMenuIndex];
            if(value <= optimize.apply(optimizeMenu)) {

                //saves a list of menus
                menus[optimizeMenuIndex] = menu;

                // To get one the optimal menu
                if(value <= optimalMenuNutrition) {
                    optimalMenuNutrition = value;
                    optimalMenu = menu;
                }
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
     * Returns the least optimized value in menus
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

    public Menu MenuFrom(List<Recipe> notThisRecipes) {
        reset();
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1, new ArrayList<>(), this::nutritionValueCalculation);
        return optimalMenu;
    }




/*    public Menu weekMenuFromFoodItemList(List<Recipe> notThisRecipes)  {
        reset();
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>(),null);
        for(Menu menu : weekMenuList){
            int value = nbrOfFoodsUsed(menu);
            if(value > optimalValue){
                optimalValue = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }
  */


    public Menu weekMenuFromFoodItemList(List<Recipe> notThisRecipes)  {
        reset();
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>(),this::nbrOfFoodsUsed);
        return optimalMenu;
    }

/*    public Menu weekMenuFromIngredientList(List<Recipe> notThisRecipes)  {
        reset();
        double optVal=100000;
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>(),null);
        for(Menu menu : weekMenuList){
            double value = lengthOfShoppingList(menu); //The idea was to use menu.size() method
            if(value < optVal){
                optVal = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }*/



    public Menu weekMenuFromIngredientList(List<Recipe> notThisRecipes)  {
        reset();
        double optVal=100000;
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>(),this::lengthOfShoppingList);
        return optimalMenu;
    }


    public Double nutritionValueCalculation(Menu chosenMenu){
        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = NutritionAlgorithms.nutrientsContent(chosenMenu);
        return NutritionAlgorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,chosenMenu);
    }

    private double nbrOfFoodsUsed(Menu menu){
        List<FoodItem> foodItemsLeft=foodItemList;
        List<FoodItem> usedFood=new ArrayList<FoodItem>();
        for( Recipe recipe : menu.getRecipeList() ) {
            for(int i=0; i<recipe.ingredients.size(); i++){
                FoodItem food=recipe.ingredients.get(i).getFoodItem();
                if(foodItemsLeft.contains(food)){
                    foodItemsLeft.remove(food);
                    usedFood.add(food);
                }
            }
        }
        return usedFood.size();
    }

    private Double lengthOfShoppingList(Menu menu){
        ShoppingList shoppingList=new ShoppingList(menu);

        for(int i=0; i<foodItemList.size(); i++){
            shoppingList.removeAmountToIngredient(new Ingredient(foodItemList.get(i), amountList.get(i)),
                    amountList.get(i).getAmount());
        }
        return shoppingList.size()+0.0;
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

    private void filterRecipes(List<Ingredient> ingredientList, List<Recipe> recipeList){

        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes){
            boolean badRecipe = false;
            for (Ingredient ingredient :ingredientList) {
                if(recipe.ingredients.contains(ingredient)){
                    badRecipe = true;
                }
            }
            for (Recipe r: recipeList) {
                if(recipe.equals(r)){
                    badRecipe = true;
                }
            }
            if(!badRecipe) filteredRecipes.add(recipe);
        }
        for (Recipe r: filteredRecipes) {
        }
        allRecipes = filteredRecipes;
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
                    ingredientList.add(new Ingredient(fi,new Amount(100.0, GRAM)));
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