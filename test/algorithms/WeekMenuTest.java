package algorithms;

import models.food.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.RDI;
import models.user.User;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Admin on 2017-03-23.
 */
public class WeekMenuTest {

    //TODO: Add a user and recepie ith identical recepie and se if week menu chose this.

    private static Double result;
    private static List<Recipe> resultingList = new ArrayList<>();
    private static Recipe userRecipe;
    @BeforeClass
    public static void init(){

        User user = new User();
        user.firstName = "User";
        User stefan = new User(1);
        stefan.firstName = "Stefan";

        userRecipe = createOptimalRecipeForSpecificUser(user);
        Recipe stefanRecipe = createOptimalRecipeForSpecificUser(stefan);
        List<Recipe> recipes = new ArrayList<>();


        recipes.add(userRecipe);
        recipes.add(stefanRecipe);

        WeekMenu weekMenu = new WeekMenu(user);
        weekMenu.setAllRecipes(recipes);
        weekMenu.setNrOfRecipes(1);
        resultingList = weekMenu.calculateWeekMenu();

        HashMap<RDI,Double> nutrientsNeed = user.hmap;
        HashMap<RDI,Double> nutrientsContent = Algorithms.nutrientsContent(resultingList);
        result = Algorithms.L2Norm(nutrientsNeed,nutrientsContent,resultingList);
        System.out.println("The result is: " + result + "!!!!!!!!!!!!!!!!" );

    }

    @Test
    public void userRecipeExistInTheResultTest(){
        assertTrue(resultingList.contains(userRecipe));
    }

    @Test
    public void onlyOneRecipeExistInTheList(){
        assertTrue(resultingList.size() == 1);
    }

    @Test
    public void theDistanceFromOptimalIsZero(){
        assertTrue(result == 0);
    }


    @NotNull
    private static Recipe createOptimalRecipeForSpecificUser(User user) {
        List<Ingredient> ingredients = new ArrayList<>();
        float convertToOnePortion = 0.3f;
        Sugars sugers = new Sugars(
                0F,
                0F,
                0F,
                0F);
        Fats fats = new Fats(
                Float.parseFloat(user.hmap.get(RDI.Fat)+"") * convertToOnePortion,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F,
                0F
        );
        Vitamins vitamins = new Vitamins(
                0F,
                0F,
                Float.parseFloat(user.hmap.get(RDI.VitaminAUG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.VitaminB6MG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.VitaminB12UG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.VitaminCMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.VitaminDUG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.VitaminEMG)+"") * convertToOnePortion,
                0F,
                Float.parseFloat(user.hmap.get(RDI.ThiamineMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.RiboflavinMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.NiacinMG)+"") * convertToOnePortion,
                0F);
        Minerals minerals = new Minerals(
                Float.parseFloat(user.hmap.get(RDI.FolateUG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.PhosphorusMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.IodineUG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.IronMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.CalciumMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.PotassiumMG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.Magnesium)+"") * convertToOnePortion,
                0F,
                0F,
                Float.parseFloat(user.hmap.get(RDI.SeleniumUG)+"") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(RDI.ZinkMG)+"") * convertToOnePortion);
        Amount amount = new Amount(100, Amount.Unit.GRAM);

        ingredients.add(
                new Ingredient(
                        new FoodItem(
                                "perfectFood",
                                "perfektus foodus",
                                9999,
                                "perfekt",
                                Float.parseFloat(user.hmap.get(RDI.CaloriKcal)+"") * convertToOnePortion,
                                0F,
                                Float.parseFloat( user.hmap.get(RDI.Carbohydrates)+"") * convertToOnePortion,
                                Float.parseFloat(user.hmap.get(RDI.Protein)+"") * convertToOnePortion,
                                0F,
                                0F,
                                0F,
                                0F,
                                0F,
                                0F,
                                0F,
                                sugers,
                                fats,
                                vitamins,
                                minerals
                        )
                        ,amount
                ));

        return new Recipe(user.firstName + "Recipe",1,ingredients);
    }
}
