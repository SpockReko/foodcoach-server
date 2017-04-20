package algorithms;

import models.GlobalDummyModels;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.food.Nutrient;
import models.user.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Admin on 2017-03-23.
 */
public class WeekMenuTest {

    //TODO: Add a user and recepie ith identical recepie and se if week menu chose this.

    private static Double result;
    private static Menu resultingMenu;
    private static Menu resultingMenuFilterIngrediense;
    private static Menu resultingMenuFilterRecipe;
    private static Recipe userRecipe;
    private static List<Recipe> recipes = new ArrayList<>();

    @BeforeClass
    public static void init() {

        User user = new User();
        User stefan = new User(1);

        userRecipe = GlobalDummyModels.createOptimalRecipeForSpecificUser(user);
        Recipe stefanRecipe = GlobalDummyModels.createOptimalRecipeForSpecificUser(stefan);


        recipes.add(userRecipe);
        recipes.add(stefanRecipe);

        MenuAlgorithms weekMenu = new MenuAlgorithms(recipes, new ArrayList<>(), 1);

        resultingMenu = weekMenu.calculateWeekMenu(user);

        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = NutritionAlgorithms.nutrientsContent(resultingMenu);
        result = NutritionAlgorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,resultingMenu);

        Ingredient usersPerfectIngrediense = userRecipe.ingredients.get(0);
        weekMenu.addAllergies(usersPerfectIngrediense);

        resultingMenuFilterIngrediense = weekMenu.calculateWeekMenu(user);

        List<Recipe> filterList = new ArrayList<>();
        filterList.add(userRecipe);
        weekMenu = new MenuAlgorithms(recipes, filterList, 1);
        resultingMenuFilterRecipe = weekMenu.calculateWeekMenu(user);

    }

    @Test
    public void userRecipeExistInTheResultTest() {
        assertTrue(resultingMenu.getRecipeList().contains(userRecipe));
    }

    @Test
    public void onlyOneRecipeExistInTheList() {
        assertTrue(resultingMenu.getRecipeList().size() == 1);
    }

    @Test
    public void theDistanceFromOptimalIsZero() {
        assertTrue(result == 0);
    }

//    @Test
//    public void filterIngredientTest(){
//        assertTrue(!resultingMenuFilterIngrediense.getRecipeList().contains(userRecipe));
//    }

    @Test
    public void filterRecipeTest(){
        System.out.println("Listan innehåller: " );
        for (Recipe i : resultingMenuFilterRecipe.getRecipeList()) {

            System.out.println("inlkuderar: " +i.getTitle());

        }
        assertFalse(resultingMenuFilterRecipe.getRecipeList().contains(userRecipe));
    }
}
