package models.recipe;

import com.avaje.ebean.Ebean;
import helpers.FakeApplicationInMemoryDB;
import models.food.FoodItem;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IngredientPersistTest extends FakeApplicationInMemoryDB {

    @Test
    public void testPersisting() {
        FoodItem item = new FoodItem("Banan", 200);
        item.save();

        Ingredient ingredient = new Ingredient(item, new Amount(30, Amount.Unit.DECILITER));
        assertThat(ingredient.getFood(), is(item));
        ingredient.save();

        Ingredient dbIngredient = Ebean.find(Ingredient.class).findUnique();

        assertThat(dbIngredient.getAmount().getAmount(), is(ingredient.getAmount().getAmount()));
        assertThat(dbIngredient.getAmount().getUnit(), is(ingredient.getAmount().getUnit()));
        assertThat(dbIngredient.getAmount().getUnit().getFraction(),
            is(ingredient.getAmount().getUnit().getFraction()));
        assertThat(dbIngredient.getAmount().getUnit().getType(),
            is(ingredient.getAmount().getUnit().getType()));
        
        String[] dbIdentifiers = dbIngredient.getAmount().getUnit().getIdentifiers();
        String[] identifiers = ingredient.getAmount().getUnit().getIdentifiers();
        for (int i = 0; i < dbIdentifiers.length; i++) {
            assertThat(dbIdentifiers[i], is(identifiers[i]));
        }
    }
}
