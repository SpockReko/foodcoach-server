package algorithms;

import models.food.Food;
import models.food.Nutrient;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.recipe.*;
import models.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by stefa on 2017-02-28.
 */
public class MenuAlgorithms {

    boolean noPrint = true;

    private final Double LARGE_DISTANCE = 999999999.9999;

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

    /**
     *
     * @param user
     * @return
     */
    public Menu calculateMenuNutrition(User user) {
        this.user = user;
        reset();
        returnAllMenus(allRecipes.size() - 1, new ArrayList<>(), this::nutritionValueCalculation);
        return optimalMenu;
    }


    /**
     * Calculates a week menu using user specified ingredients.
     *
     * @param
     * @return the menu that generates the shortest shopping list.
     */
    public Menu calculateWeekMenu(List<Ingredient> ingredientList)  {
        reset();
        this.ingredientsToUse=ingredientList;
        returnAllMenus(allRecipes.size()-1,new ArrayList<>(),this::getLeftoversSize);
        return optimalMenu;
    }

    /**
     *
     * @param ingredientList
     * @return
     */
    public Menu CalculateWeekMenuMinimalWaste(List<Ingredient> ingredientList) {
        reset();
        this.ingredientsToUse=ingredientList;
        returnAllMenus(allRecipes.size() - 1, new ArrayList<>(), this::getCO2);
        return optimalMenu;
    }



    /**
     * @param chosenMenu
     * @return
     */
    private Double nutritionValueCalculation(Menu chosenMenu) {
        HashMap<Nutrient, Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient, Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient, Double> nutrientsContent =
                NutritionAlgorithms.nutrientsContent(chosenMenu);
        return NutritionAlgorithms
                .L2Norm(nutrientsNeed, nutrientsContent, nutrientsOverdose, chosenMenu);
    }


    /**
     * @param menu
     * @return
     */
    private Double getLeftoversSize(Menu menu) {
        ShoppingList shoppingList = new ShoppingList(menu);
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            shoppingList.removeAmountToIngredient(ingredientsToUse.get(i), ingredientsToUse.get(i).getAmount().getAmount());
        }
        //return shoppingList.size()+0.0;
        nutritionValueCalculation(menu);
        return shoppingList.getLeftovers().size() + 0.0;
    }

    /**
     * @param menu
     * @return
     */
    private Double getCO2(Menu menu) {
        ShoppingList shoppingList = new ShoppingList(menu);
        for (int i = 0; i < ingredientsToUse.size(); i++) {
            shoppingList.removeAmountToIngredient(ingredientsToUse.get(i), ingredientsToUse.get(i).getAmount().getAmount());
        }
        nutritionValueCalculation(menu);
        return shoppingList.getCO2();
    }

    /**
     * @param menu
     * @return
     */
    public String recipeListToString(Menu menu) {
        String text = "";
        for (Recipe r : menu.getRecipeList()) {
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\n";
        for (String comment : menu.getCommentList()) {
            text = text + comment + "\n";
        }
        return text;
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
                if (recipe.equals(r)) {
                    badRecipe = true;
                }
            }
            if (!badRecipe)
                filteredRecipes.add(recipe);
        }

        allRecipes = filteredRecipes;
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

//############### Help-classes ################

    /**
     * Every time we want to calculate values we need clean containers for menus
     */
    private void reset() {
        optimalMenuNutrition = LARGE_DISTANCE;
        optimalMenu = new Menu(new ArrayList<>());
        filterRecipes(notTheseIngredients, notTheseRecipes);
        menuList = new ArrayList<>();
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
