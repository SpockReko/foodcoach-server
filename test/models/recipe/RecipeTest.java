package models.recipe;

import models.food.DataSource;
import models.food.Food;
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

    private static Food item1;
    private static Food item2;
    private static Recipe recipe;

   @Before
   public void setUp() throws Exception {
       item1 = new Food("test1", 100, DataSource.SLV, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
           10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
           10D);
       item1.densityConstant = 0.8;
       Ingredient ing1 = new Ingredient(item1, new Amount(100, Amount.Unit.GRAM));
       item2 = new Food("test2", 100, DataSource.SLV, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
           10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
           10D);
       item2.densityConstant = 2.0;
       Ingredient ing2 = new Ingredient(item2, new Amount(200, Amount.Unit.GRAM));
       List<Ingredient> ingredients = new LinkedList<>();
       ingredients.add(ing1);
       ingredients.add(ing2);
       recipe = new Recipe("Paj", 4, ingredients);
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
       assertEquals(40, recipe.getAlcohol(), delta);
   }

}
