package algorithms;

import models.food.fineli.Food;
import models.food.fineli.Nutrient;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by stefa on 2017-02-28.
 */
public class MenuAlgorithms {

    boolean noPrint = false;

    public void setNoPrint(boolean noPrint) {
        this.noPrint = noPrint;
    }

    private final Double LARGE_DISTANCE = 999999999.9999;
    private Double optimalMenuNutrition;
    private Menu optimalMenu = new Menu(new ArrayList<>());
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Menu> weekMenuList = new ArrayList<>();
    private User user = new User();

    private List<Ingredient> notThisIngredients = new ArrayList<>();

    public MenuAlgorithms(User user, List<Recipe> recipeList){
        this.user = user;
        this.allRecipes = recipeList;
    }

    private void reset(){
        optimalMenuNutrition = LARGE_DISTANCE;
        optimalMenu = new Menu(new ArrayList<>());
        weekMenuList = new ArrayList<>();
    }

    public void addAllergies(Ingredient ingredient) {
        notThisIngredients.add(ingredient);
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


    public Menu calculateWeekMenu(List<Recipe> notThisRecipes) {
        reset();
        filterRecipes(notThisIngredients,notThisRecipes);
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


    public Double nutritionValueCalculation(Menu chosenMenu){
        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = NutritionAlgorithms.nutrientsContent(chosenMenu);
        return NutritionAlgorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,chosenMenu);
    }

    private void filterRecipes(List<Ingredient> ingredientList, List<Recipe> recipeList){
                                                                    // help testPrint methods!
                                                                    test1(ingredientList);
        List<Recipe> filteredRecipes = new ArrayList<>();           test2(recipeList);
        for (Recipe recipe : allRecipes){                           test3(recipe);
            boolean badRecipe = false;
            for (Ingredient ingredient :ingredientList) {           test4(recipe, ingredient);
                if(recipe.ingredients.contains(ingredient)){
                    badRecipe = true;                               test5(recipe, ingredient);
                }
            }
            for (Recipe r: recipeList) {
                if(recipe.equals(r)){
                    badRecipe = true;
                                                                    test6(recipe);
                }
            }
            if(!badRecipe) filteredRecipes.add(recipe);
                                                                    test7(filteredRecipes);
        }
                                                                    test8(filteredRecipes);
                                                                    test9(filteredRecipes);
        allRecipes = filteredRecipes;
                                                                    testEnd();
    }

    private List<Ingredient> getIngredientsFromString(List<String> stringList) {
        List<Ingredient> ingredientList = new ArrayList<>();

        for (String name: stringList) {
            //TODO: Fix the error of the MYSQL call!
            List<Food> foods;
            try {
                foods = Food.find.where().contains("name", name).findList();
            } catch(Exception e) {
                foods = null;
            }
            if (foods != null) {
                for (Food food: foods) {
                    ingredientList.add(new Ingredient(food, new Amount(100.0, Amount.Unit.GRAM)));
                }
            }
        }
        return ingredientList;
    }

    public int getNrOfRecipes(){ return nrOfRecipes;}

    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

    ///////////////////////////////////////////////////////////////////////
    //////// Test print help functions! ///////////////////////////////////
    ///////////////////////////////////////////////////////////////////////

    private void testEnd() {
        if(!noPrint)
            System.out.println("************************************************************slut");
    }

    private void test9(List<Recipe> filteredRecipes) {
        if (!noPrint) {
            for (Recipe r : filteredRecipes) {
                System.out.println("recipe: " + r.getTitle());
            }
        }
    }

    private void test8(List<Recipe> filteredRecipes) {
        if(!noPrint)
            System.out.println("Now has all loops ended! the filterdList has size: " + filteredRecipes.size());
    }

    private void test7(List<Recipe> filteredRecipes) {
        if(!noPrint)
            System.out.println("Now is the size of the filteredRecipes: " + filteredRecipes.size());
    }

    private void test6(Recipe recipe) {
        if(!noPrint)
            System.out.println("\t" + recipe.getTitle() + "is a bad recipe! You don't want this recepie!");
    }

    private void test5(Recipe recipe, Ingredient ingredient) {
        if(!noPrint)
            System.out.println("\t" + recipe.getTitle() + "is a bad recipe! Ingredient " + ingredient + "exist in allergies!!!**!!!");
    }

    private void test4(Recipe recipe, Ingredient ingredient) {
        if(!noPrint)
            System.out.println("\t"+recipe.getTitle() + " see if if it have ingredient: " + ingredient);
    }

    private void test3(Recipe recipe) {
        if(!noPrint)
            System.out.println("\nLoop for recipe " + recipe.getTitle());
    }

    private void test2(List<Recipe> recipeList) {
        if (!noPrint) {
            System.out.println("#recipesList in weekmenu at parameter has size: " + recipeList.size());
            System.out.println("For loop starts! nr of Recepies: " + allRecipes.size());
        }
    }

    private void test1(List<Ingredient> ingredientList) {
        if (!noPrint) {
            System.out.println("optimalMenuNutrition: " + optimalMenuNutrition +
                    "\noptimalMenu size: " + optimalMenu.getRecipeList().size() +
                    "\nnrOfRecipe: " + nrOfRecipes +
                    "\nallRecipes size: " + allRecipes.size() +
                    "\nweekMenuList size: " + weekMenuList.size() +
                    "\nUser: " + user.firstName);
            System.out.println("*********************************************************start");

            System.out.println("#IngredientList in weekmenu at row 88 has size: " + ingredientList.size());
        }
    }
}
