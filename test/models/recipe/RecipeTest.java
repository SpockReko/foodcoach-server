package models.recipe;

import models.food.FoodItem;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

/**
 * Created by fredrikkindstrom on 2017-03-01.
 */
public class RecipeTest {

    private static final double delta = 1e-2;

    private static FoodItem item1;
    private static FoodItem item2;
    private static Recipe recipe;

    @Before
    public void setUp() throws Exception {
        item1 = new FoodItem("Mat1", null, 100, null,
            10F, 10F, 10F, 10F, 10F,
            10F, 10F, 10F, 10F, 10F, 10F,
            null, null, null, null);
        item1.densityConstant = 0.8;
        Ingredient ing1 = new Ingredient(item1, new Amount(100, Amount.Unit.GRAM));
        item2 = new FoodItem("Mat2", null, 101, null,
            15F, 15F, 15F, 15F, 15F,
            15F, 15F, 15F, 15F, 15F, 15F,
            null, null, null, null);
        item2.densityConstant = 2.0;
        Ingredient ing2 = new Ingredient(item2, new Amount(200, Amount.Unit.GRAM));
        List<Ingredient> ingredients = new LinkedList<>();
        ingredients.add(ing1);
        ingredients.add(ing2);
        recipe = new Recipe("Paj", null, 4, ingredients);
    }

    @Test
    public void persistanceTest() {
        running(fakeApplication(inMemoryDatabase()), () -> {
            item1.save();
            item2.save();
            recipe.save();
            Recipe dbRecipe = Recipe.find.byId(1L);
            assertThat(dbRecipe.getTitle(), is(recipe.getTitle()));
            assertThat(dbRecipe.getPortions(), is(recipe.getPortions()));
            assertThat(dbRecipe.getEnergyKcal(), is(recipe.getEnergyKcal()));
            List<Ingredient> dbIngredients = dbRecipe.ingredients;
            List<Ingredient> ingredients = recipe.ingredients;
            for (int i = 0; i < dbIngredients.size(); i++) {
                assertThat(dbIngredients.get(i), is(ingredients.get(i)));
            }
        });
    }

    @Test
    public void nutrientValuesTest() {
        assertEquals(40, recipe.getEnergyKcal(), delta);
        assertEquals(40, recipe.getEnergyKj(), delta);
        assertEquals(40, recipe.getCarbohydrates(), delta);
        assertEquals(40, recipe.getProtein(), delta);
        assertEquals(40, recipe.getFibre(), delta);
        assertEquals(40, recipe.getWholeGrain(), delta);
        assertEquals(40, recipe.getCholesterol(), delta);
        assertEquals(40, recipe.getWater(), delta);
        assertEquals(40, recipe.getAlcohol(), delta);
        assertEquals(40, recipe.getAsh(), delta);
        assertEquals(40, recipe.getWaste(), delta);
    }

}
