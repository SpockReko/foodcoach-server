package models.recipe;

import models.food.DataSource;
import models.food.Food;
import models.food.FoodGroup;
import models.food.Nutrient;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

/**
 * Created by fredrikkindstrom on 2017-03-01.
 */
public class RecipeTest {

    private static final double delta = 1e-2;

    private static Food food1;
    private static Food food2;
    private static Recipe recipe;

   @Before
   public void setUp() throws Exception {
       FoodGroup group = new FoodGroup("Test Group");
       food1 = new Food("test1", 100, DataSource.SLV, 10D, 20D, 10D, 10D, 10D, 0D, 10D, 10D, 10D, 10D,
           10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
           10D);
       food1.densityConstant = 0.8;
       food1.group = group;
       Ingredient ing1 = new Ingredient(food1, new Amount(100, Amount.Unit.GRAM));
       food2 = new Food("test2", 100, DataSource.SLV, 10D, 20D, 10D, 10D, 10D, 0D, 10D, 10D, 10D, 10D,
           10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
           10D);
       food2.densityConstant = 2.0;
       food2.group = group;
       Ingredient ing2 = new Ingredient(food2, new Amount(200, Amount.Unit.GRAM));
       List<Ingredient> ingredients = new LinkedList<>();
       ingredients.add(ing1);
       ingredients.add(ing2);
       recipe = new Recipe("Paj", 3, ingredients);
   }

   @Test
   public void persistanceTest() {
       running(fakeApplication(inMemoryDatabase()), () -> {
           food1.save();
           food2.save();
           recipe.save();
           Recipe dbRecipe = Recipe.find.byId(1L);
           assertThat(dbRecipe.getTitle(), is(recipe.getTitle()));
           assertThat(dbRecipe.getPortions(), is(recipe.getPortions()));
           assertThat(dbRecipe.getNutrient(Nutrient.ENERGY_KCAL), is(recipe.getNutrient(Nutrient.ENERGY_KCAL)));
           List<Ingredient> dbIngredients = dbRecipe.ingredients;
           List<Ingredient> ingredients = recipe.ingredients;
           for (int i = 0; i < dbIngredients.size(); i++) {
               assertThat(dbIngredients.get(i), is(ingredients.get(i)));
           }
       });
   }

   @Test
   public void nutrientValuesTest() {
       assertEquals(30, recipe.getNutrient(Nutrient.ENERGY_KJ), delta);
       assertEquals(60, recipe.getNutrient(Nutrient.CARBOHYDRATES), delta);
       assertEquals(30, recipe.getNutrient(Nutrient.PROTEIN), delta);
       assertEquals(30, recipe.getNutrient(Nutrient.FIBRE), delta);
       assertEquals(0, recipe.getNutrient(Nutrient.ALCOHOL), delta);
       assertEquals(10, recipe.getNutrientPerPortion(Nutrient.ENERGY_KJ), delta);
       assertEquals(20, recipe.getNutrientPerPortion(Nutrient.CARBOHYDRATES), delta);
       assertEquals(10, recipe.getNutrientPerPortion(Nutrient.PROTEIN), delta);
       assertEquals(10, recipe.getNutrientPerPortion(Nutrient.FIBRE), delta);
       assertEquals(0, recipe.getNutrientPerPortion(Nutrient.ALCOHOL), delta);
   }

}
