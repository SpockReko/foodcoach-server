package algorithms;

import models.food.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.RDI;
import models.user.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Admin on 2017-03-23.
 */
public class WeekMenuTest {

    //TODO: Add a user and recepie ith identical recepie and se if week menu chose this.

    @Test
    public void RecepieWithOptimalResultGetPickedTest(){
        User user = new User();
        List<Ingredient> ingredients = new ArrayList<>();
        Sugars sugers = new Sugars(
                0F,
                0F,
                0F,
                0F);
        Fats fats = new Fats(
                Float.parseFloat(user.hmap.get("fat")+""),
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
                Float.parseFloat(user.hmap.get("vitaminANeedug")+""),
                Float.parseFloat(user.hmap.get("vitaminB6Needmg")+""),
                Float.parseFloat(user.hmap.get("vitaminB12Needug")+""),
                Float.parseFloat(user.hmap.get("vitaminCNeedmg")+""),
                0F,
                0F,//TODO: HAr kommit hit från att mata in användarens värden!
                0F,
                Float.parseFloat(user.hmap.get("tiaminNeedmg")+""),
                0F,
                0F,
                0F);
        Minerals minerals = new Minerals(
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
                0F);
        Amount amount = new Amount(100, Amount.Unit.GRAM);
        //TODO: Add perfekt food item that covers everything that the user need!
        ingredients.add(
                new Ingredient(
                        new FoodItem(
                                "perfectFood",
                                "perfektus foodus",
                                9999,
                                "perfekt",
                                Float.parseFloat(user.hmap.get("bmr")+""),//energyKcal
                                0F,//EnergiKJ
                                Float.parseFloat( user.hmap.get("carbohydrates")+""),//Carbohydrates
                                Float.parseFloat(user.hmap.get("protein")+""),//protein,
                                0F,// fibre,
                                0F,//wholeGrain,
                                0F,//Float cholesterol,
                                0F,//Float water,
                                0F,//Float alcohol,
                                0F,//Float ash,
                                0F,//Float waste,
                                sugers,//Sugars sugars,
                                fats,
                                vitamins,
                                minerals
                        )
                        ,amount
                ));

        Recipe recipe = new Recipe("perfectRecipe",1,ingredients);
        WeekMenu weekMenu = new WeekMenu(new User());
        List<Recipe> allrecepies = Recipe.find.all();
        allrecepies.add(recipe);
        weekMenu.setAllRecipes(allrecepies);
        weekMenu.setNrOfRecipes(1);
        List<Recipe> resultingList = weekMenu.calculateWeekMenu();
        assertTrue(resultingList.contains(recipe)
                && resultingList.size() == 1);
    }
}
