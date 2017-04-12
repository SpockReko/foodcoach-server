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

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by stefa on 2017-02-28.
 */
public class MenuAlgorithms {

    private final Double LARGE_DISTANCE = 999999999.9999;
    private Double optimalMenuNutrition;
    private int optimalValue;
    private Menu optimalMenu = new Menu(new ArrayList<>());
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


    // Building Menus with recursion!

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

    public Menu calculateWeekMenu(List<Recipe> notTheeseRecipes) {
        reset();
        filterRecipes(notTheeseIngredients,notTheeseRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        optimalMenuNutrition = nutritionValueCalculation(weekMenuList.get(0));
        for(Menu menu : weekMenuList){
            double value = nutritionValueCalculation(menu);
            System.out.println("Nutrients for " + menu.recipeListToString(menu) + "\n... has the value: " + value);
            if(value <= optimalMenuNutrition){
                optimalMenuNutrition = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }


    public Menu weekMenuFromFoodItemList(List<Recipe> notThisRecipes)  {
        reset();
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        for(Menu menu : weekMenuList){
            int value = nbrOfFoodsUsed(menu);
            if(value > optimalValue){
                optimalValue = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }

    public Menu weekMenuFromIngredientList(List<Recipe> notThisRecipes)  {
        reset();
        double optVal=100000;
        filterRecipes(notTheeseIngredients,notThisRecipes);
        returnAllWeekMenus(allRecipes.size()-1,new ArrayList<>());
        for(Menu menu : weekMenuList){
            double value = amountOfFoodNotUsed(menu); //The idea was to use menu.size() method
            if(value < optVal){
                optVal = value;
                optimalMenu = menu;
            }
        }
        return optimalMenu;
    }


    public Double nutritionValueCalculation(Menu chosenMenu){
        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = NutritionAlgorithms.nutrientsContent(chosenMenu);
        return NutritionAlgorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,chosenMenu);
    }

    private int nbrOfFoodsUsed(Menu menu){
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

    private double amountOfFoodNotUsed(Menu menu){
        List<FoodItem> foodItemsLeft=foodItemList;
        List<Amount> amountLeft=amountList;
        double usedFood=0;
        for( Recipe recipe : menu.getRecipeList() ) {

            //Snabbara att sätta ihop menyn till en ingredienslista?
            //Avbryta om listor tomma?

            for(int i=0; i<recipe.ingredients.size(); i++){
                Ingredient ingredient = recipe.ingredients.get(i);
                FoodItem food=ingredient.getFoodItem();
                Amount amount=ingredient.getAmount();

                //Hur gör man med enheter??

                    int index = foodItemsLeft.indexOf(food);
                    if(index!=-1){
                        double a=amountLeft.get(index).getAmount()-amount.getAmount();
                        if(a>0){
                            amountLeft.set(index,new Amount(amountLeft.get(index).getAmount()-amount.getAmount(), GRAM));
                        }
                        else{
                            foodItemsLeft.remove(index);
                            amountLeft.remove(index);
                        }
                    }
            }
        }
        double sum=0;
        for(int i=0; i<amountLeft.size(); i++){
            sum=sum+amountLeft.get(i).getAmount();
        }
        return sum;
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