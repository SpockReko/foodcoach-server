package algorithms;

import models.food.Nutrient;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.*;
import models.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Created by stefa on 2017-02-28.
 */
public class MenuAlgorithms {

    boolean noPrint = true;

    public final static Double LARGE_DISTANCE = 999999999.9999;

    private Double optimalMenuNutrition;
    private Menu optimalMenu = new Menu(new ArrayList<>());
    private Menu[] menus;
    private int nrOfRecipes;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Menu> menuList = new ArrayList<>();
    private User user = new User();
    private List<Ingredient> ingredientsToUse;
    private List<Ingredient> notTheseIngredients = new ArrayList<>();
    private List<Recipe> notTheseRecipes;
    private static int algoCount = 0;

    /**
     * Constructor that creates a MenuAlgorithm class that holds recipes and can create
     * optimal menus by different criteria
     *
     * @param
     * @param recipeList The list of recipes you want to chose between
     */
    public MenuAlgorithms(List<Recipe> recipeList, List<Recipe> notThisRecipes, int nrOfRecipes) {
        this.allRecipes = recipeList;
        notTheseRecipes = notThisRecipes;
        this.nrOfRecipes = nrOfRecipes;
    }

    /**
     * Recursiv method with 2 base cases and a minimization of the value.
     * In the case we get a size that is good then we can
     *
     * @param indexOfRecipes where in the recurtion we ar at
     * @param currentList    the list we have at each recurtion
     * @param optimize       The method we want to evaluate the menu on
     * @return
     */
    public double returnAllMenus(int indexOfRecipes, List<Recipe> currentList,
                                 Function<Menu, Double> optimize) {
        algoCount += 1;
        if (currentList.size() == nrOfRecipes) {
            Menu menu = new Menu(currentList);
            double value = optimize.apply(menu);

            if (value <= optimalMenuNutrition) {
                optimalMenuNutrition = value;
                optimalMenu = menu;
            }
            return value;
        } else if (indexOfRecipes < 0) {
            return optimalMenuNutrition + 1.0;
        } else {
            List<Recipe> newList = new ArrayList<>(currentList);
            currentList.add(allRecipes.get(indexOfRecipes));
            return Math.min(returnAllMenus(indexOfRecipes - 1, currentList, optimize),
                    returnAllMenus(indexOfRecipes - 1, newList, optimize));
        }
    }

    public void returnMenuGreedy(Function<Menu, Double> optimize) {

        List<Recipe> copyRecipes = new ArrayList<>(allRecipes);
        List<Recipe> optimalList = new ArrayList<>();
        List<Recipe> resultList = new ArrayList<>();
        while (optimalList.size() < nrOfRecipes) {
            for (Recipe recipe : copyRecipes) {
                algoCount += 1;
                List<Recipe> testRecipe = new ArrayList<>(optimalList);
                testRecipe.add(recipe);
                Menu menu = new Menu(testRecipe);
                double value = optimize.apply(menu);
                if (value < optimalMenuNutrition && value > 2) {
                    System.out.println(value);
                    optimalMenuNutrition = value;
                    resultList = menu.getRecipeList();
                    //if(value == 0.0){ //If the value is 0.0 then is the optimal right?
                    //    optimalMenu = menu;
                    //    return;
                    //}
                }
            }
            optimalMenuNutrition = LARGE_DISTANCE;
            copyRecipes.removeAll(resultList);
            optimalList = resultList;
        }
        Menu menu = new Menu(optimalList);
        optimize.apply(menu);
        optimalMenu = menu;
    }

    /**
     * @param user
     * @return
     */

    public Menu calculateMenuNutrition(User user) {
        this.user = user;
        reset();

        if (allRecipes.size() < 10) {
            returnAllMenus(40, new ArrayList<>(), this::nutritionValueCalculation);
        } else {
            returnMenuGreedy(this::nutritionValueCalculation);
        }

        return optimalMenu;
    }


    /**
     * Calculates a week menu using user specified ingredients.
     *
     * @param
     * @return the menu that generates the shortest shopping list.
     */
    public Menu calculateWeekMenu(List<Ingredient> ingredientList) {
        reset();
        this.ingredientsToUse = ingredientList;
        if (allRecipes.size() < 10) {
            returnAllMenus(allRecipes.size() - 1, new ArrayList<>(), this::getLeftoversSize);
        } else {
            returnMenuGreedy(this::getLeftoversSize);
        }
        return optimalMenu;
    }

    /**
     * @param ingredientList
     * @return
     */
    public Menu calculateWeekMenuMinimalCO2(List<Ingredient> ingredientList) {
        reset();
        this.ingredientsToUse = ingredientList;
        if (allRecipes.size() < 30) {
            returnAllMenus(allRecipes.size() - 1, new ArrayList<>(), this::getCO2);
        } else {
            returnMenuGreedy(this::getCO2);
        }
        return optimalMenu;
    }

    /**
     * @param ingredientList
     * @return
     */
    public Menu calculateWeekMenuMinimalShoppingList(List<Ingredient> ingredientList) {
        reset();
        this.ingredientsToUse = ingredientList;
        if (allRecipes.size() < 30) {
            returnAllMenus(allRecipes.size() - 1, new ArrayList<>(), this::getShoppinglistSize);
        } else {
            returnMenuGreedy(this::getShoppinglistSize);
        }
        return optimalMenu;
    }


    /**
     * @param chosenMenu
     * @return
     */
    private Double nutritionValueCalculation(Menu chosenMenu) {
        if (chosenMenu.getCommentList().isEmpty()) {
            HashMap<Nutrient, Double> nutrientsNeed = user.hmap;
            HashMap<Nutrient, Double> nutrientsOverdose = user.overdoseValues;
            HashMap<Nutrient, Double> nutrientsContent =
                    NutritionAlgorithms.nutrientsContent(chosenMenu);
            double value = NutritionAlgorithms.L2Norm(nutrientsNeed, nutrientsContent, nutrientsOverdose, chosenMenu);
            chosenMenu.setValue(value);
            //TODO: To test uncomment test clause in menuByName in MenuAlgorithmsController
            //System.out.println("\n chosenMenu: \n "+ chosenMenu.recipeListToString(new ShoppingList(chosenMenu)) +"\n value " + value);
            return value;
        } else {
            return chosenMenu.getValue();
        }
    }


    /**
     * @param menu
     * @return
     */
    private Double getShoppinglistSize(Menu menu) {
        ShoppingList shoppingList = new ShoppingList(menu);
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            shoppingList.removeAmountOfIngredient(ingredientsToUse.get(i), ingredientsToUse.get(i).getAmount().getQuantity());
        }
        nutritionValueCalculation(menu);
        return shoppingList.size() + 0.0;
    }


    /**
     * @param menu
     * @return
     */
    private Double getLeftoversSize(Menu menu) {
        ShoppingList shoppingList = new ShoppingList(menu);
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            shoppingList.removeAmountOfIngredient(ingredientsToUse.get(i), ingredientsToUse.get(i).getAmount().getQuantity());
        }
        nutritionValueCalculation(menu);
        return shoppingList.getLeftoverSize();
    }

    /**
     * @param menu
     * @return
     */
    private Double getCO2(Menu menu) {
        ShoppingList shoppingList = new ShoppingList(menu);
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            shoppingList.removeAmountOfIngredient(ingredientsToUse.get(i), ingredientsToUse.get(i).getAmount().getQuantity());
        }
        nutritionValueCalculation(menu);
        return shoppingList.getCO2();
    }

    /**Used in test MenyAlgorithm2Test
     * @param menu
     * @return
     */
    public String recipeListToString(Menu menu) {
        String text = "";

        for (Recipe r : menu.getRecipeList()) {
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\n";
        if (menu.getCommentList().isEmpty()) {
            nutritionValueCalculation(menu);
        }
        for (String comment : menu.getCommentList()) {
            text = text + comment + "\n";
        }
        return text;
    }

    /** ///////////////////////////////
     *  Setters and getters
     */////////////////////////////////

    /**
     * Adds allergies to the calculations
     *
     * @param ingredient
     */
    public void addAllergies(Ingredient ingredient) {
        notTheseIngredients.add(ingredient);
    }

    public void setNoPrint(boolean noPrint) {
        this.noPrint = noPrint;
    }


    /**
     * @return
     */
    public int getNrOfRecipes() {
        return nrOfRecipes;
    }

    /**
     * @param nrOfRecipes
     */
    public void setNrOfRecipes(int nrOfRecipes) {
        this.nrOfRecipes = nrOfRecipes;
    }

    /**
     * @return
     */
    public Menu[] getMenus() {
        return menus.clone();
    }

    public int getAllRecepie() {
        return allRecipes.size();
    }
//############### Help-classes ################

    /**
     * Every time we want to calculate values we need clean containers for menus
     */
    private void reset() {
        algoCount = 0;
        convertAllToOnePortion();
        optimalMenuNutrition = LARGE_DISTANCE;
        optimalMenu = new Menu(new ArrayList<>());
        menuList = new ArrayList<>();
        filterRecipes(notTheseIngredients, notTheseRecipes);
    }


    private void convertAllToOnePortion() {
        convertAllRecipesToOneProtion();
        convertNotTheseRecipesToOneProtion();
    }

    private void convertAllRecipesToOneProtion() {
        List<Recipe> oneRecepiesList = new ArrayList<>();
        for (Recipe r : allRecipes) {
            oneRecepiesList.add(r.getOnePortionRecipe());
        }
        allRecipes = oneRecepiesList;
    }

    private void convertNotTheseRecipesToOneProtion() {
        List<Recipe> oneRecepiesList = new ArrayList<>();
        for (Recipe r : notTheseRecipes) {
            oneRecepiesList.add(r.getOnePortionRecipe());
        }
        notTheseRecipes = oneRecepiesList;
    }


    /**
     * @param ingredientList
     * @param recipeList
     */
    private void filterRecipes(List<Ingredient> ingredientList, List<Recipe> recipeList) {

        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            boolean badRecipe = false;
            for (Ingredient ingredient : ingredientList) {
                if (recipe.ingredients.contains(ingredient)) {
                    badRecipe = true;
                }
            }
            for (Recipe r : recipeList) {
                if (recipe.getTitle().equals(r.getTitle())) {
                    badRecipe = true;
                }
            }
            List<Recipe> testList = new ArrayList<>();
            testList.add(recipe);
            Menu menu = new Menu(testList);
            //if(this.nutritionValueCalculation(menu) < 0.8 && recipe.ingredients.size() != 5){
            //    badRecipe = true;
            //}
            if (!badRecipe)
                filteredRecipes.add(recipe);
        }

        allRecipes = filteredRecipes;
    }

    /**
     * Returns the least optimized value in menus
     *
     * @param optimize
     * @return menu;
     */
    private int getOptimalMenuIndex(Function<Menu, Double> optimize) {
        Menu menu = menus[0];
        int returnIndex = 0;
        if (menu == null)
            return 0;

        for (int i = 0; i < menus.length; i++) {
            if (menus[i] == null)
                return i;

            double mValue = optimize.apply(menus[i]);
            double menuValue = optimize.apply(menu);

            if (mValue > menuValue) { //The worse value is better!
                returnIndex = i;
            }
        }
        return returnIndex;
    }
}
