package algorithms;

import models.food.*;
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
        //TODO: Add perfekt food item that covers everything that the user need!
        /*ingredients.add(new Ingredient(new FoodItem("perfectFood", "perfektus foodus", 9999, "perfekt",
                user.hmap.get()Float energyKcal, Float energyKj, Float carbohydrates, Float protein, Float fibre,
                Float wholeGrain, Float cholesterol, Float water, Float alcohol, Float ash, Float waste,
                Sugars sugars, Fats fats, Vitamins vitamins, Minerals minerals)));
        */
        Recipe recipe = new Recipe("perfectRecipe",1,ingredients);
        WeekMenu weekMenu = new WeekMenu();
        List<Recipe> allrecepies = Recipe.find.all();
        allrecepies.add(recipe);
        weekMenu.setAllRecepie(allrecepies);
        weekMenu.setDesiredValue(1.0);
        weekMenu.setNrOfRecept(1);
        List<Recipe> emptylist = new ArrayList<>();
        weekMenu.calculateWeekMenu(allrecepies.size(),emptylist);
        assertTrue(weekMenu.getOptimalMenu().contains(recipe)
                && weekMenu.getOptimalMenu().size() == 1);
    }


}
