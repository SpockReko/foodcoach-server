package models.recipe;

import models.food.FoodItem;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by fredrikkindstrom on 2017-03-01.
 */
public class RecipeTest {

    private static final double delta = 1e-2;

    private static Recipe recipe;

    @Before
    public void setUp() throws Exception {
        FoodItem item1 = new FoodItem(null, null, 100, null,
            10F, 10F, 10F, 10F, 10F,
            10F, 10F, 10F, 10F, 10F, 10F,
            null, null, null, null);
        item1.densityConstant = 0.8;
        Ingredient ing1 = new Ingredient(item1, new Amount(100, Amount.Unit.GRAM));
        FoodItem item2 = new FoodItem(null, null, 101, null,
            15F, 15F, 15F, 15F, 15F,
            15F, 15F, 15F, 15F, 15F, 15F,
            null, null, null, null);
        item2.densityConstant = 2.0;
        Ingredient ing2 = new Ingredient(item2, new Amount(200, Amount.Unit.GRAM));
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ing1);
        ingredients.add(ing2);
        recipe = new Recipe("Paj", null, 4, ingredients);
    }

    @Test
    public void getEnergyKcal() {
        assertEquals(40, recipe.getEnergyKcal(), delta);
    }

}
