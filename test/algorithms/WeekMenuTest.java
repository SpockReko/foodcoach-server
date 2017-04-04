package algorithms;

import models.food.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Menu;
import models.recipe.Recipe;
import models.user.Nutrient;
import models.user.User;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

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
        user.firstName = "User";
        User stefan = new User(1);
        stefan.firstName = "Stefan";

        userRecipe = createOptimalRecipeForSpecificUser(user);
        Recipe stefanRecipe = createOptimalRecipeForSpecificUser(stefan);


        recipes.add(userRecipe);
        recipes.add(stefanRecipe);

        WeekMenu weekMenu = new WeekMenu(user,recipes);
        weekMenu.setNrOfRecipes(1);
        resultingMenu = weekMenu.calculateWeekMenu(new ArrayList<>());

        HashMap<Nutrient,Double> nutrientsNeed = user.hmap;
        HashMap<Nutrient,Double> nutrientsOverdose = user.overdoseValues;
        HashMap<Nutrient,Double> nutrientsContent = Algorithms.nutrientsContent(resultingMenu);
        result = Algorithms.L2Norm(nutrientsNeed,nutrientsContent,nutrientsOverdose,resultingMenu);

        Ingredient usersPerfectIngrediense = userRecipe.ingredients.get(0);
        weekMenu.addAllergies(usersPerfectIngrediense);
        resultingMenuFilterIngrediense = weekMenu.calculateWeekMenu(new ArrayList<>());

        List<Recipe> filterList = new ArrayList<>();
        filterList.add(userRecipe);
        resultingMenuFilterRecipe = weekMenu.calculateWeekMenu(filterList);

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
        assertTrue(!resultingMenuFilterRecipe.getRecipeList().contains(userRecipe));
    }

    @NotNull
    private static Recipe createOptimalRecipeForSpecificUser(User user) {
        List<Ingredient> ingredients = new ArrayList<>();
        float convertToOnePortion = 0.3f;

        Sugars sugars = new Sugars(0F, 0F, 0F, 0F);
        Fats fats =
            new Fats(Float.parseFloat(user.hmap.get(Nutrient.Fat) + "") * convertToOnePortion, 0F, 0F,
                0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F);
        Vitamins vitamins = new Vitamins(0F, 0F,
            Float.parseFloat(user.hmap.get(Nutrient.VitaminAUG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.VitaminB6MG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.VitaminB12UG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.VitaminCMG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.VitaminDUG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.VitaminEMG) + "") * convertToOnePortion, 0F,
            Float.parseFloat(user.hmap.get(Nutrient.ThiamineMG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.RiboflavinMG) + "") * convertToOnePortion,
            Float.parseFloat(user.hmap.get(Nutrient.NiacinMG) + "") * convertToOnePortion, 0F);
        Minerals minerals =
            new Minerals(Float.parseFloat(user.hmap.get(Nutrient.FolateUG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.PhosphorusMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.IodineUG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.IronMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.CalciumMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.PotassiumMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.MagnesiumMG) + "") * convertToOnePortion, 0F, 0F,
                Float.parseFloat(user.hmap.get(Nutrient.SeleniumUG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.ZinkMG) + "") * convertToOnePortion);
        Amount amount = new Amount(100, Amount.Unit.GRAM);

        Ingredient perfectIngredient = new Ingredient(
                new FoodItem("perfectFoodFor" + user.firstName, "perfektus foodus", 9999, "perfekt",
                        Float.parseFloat(user.hmap.get(Nutrient.CaloriKcal) + "") * convertToOnePortion, 0F,
                        Float.parseFloat(user.hmap.get(Nutrient.Carbohydrates) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.Protein) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.Fibre) + "")* convertToOnePortion, 0F, 0F,
                        0F, 0F, 0F, 0F, sugars, fats, vitamins, minerals), amount);
        ingredients.add(perfectIngredient);

        return new Recipe(user.firstName + "Recipe", 1, ingredients);
    }
}
