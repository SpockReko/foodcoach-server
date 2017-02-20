package models.food;

import org.junit.Test;

import javax.persistence.PersistenceException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FoodItemTest extends FakeApplicationInMemoryDB {

    @Test
    public void testBasicFields() {
        String name = "Banana";
        int lmvFoodNumber = 123;
        FoodItem food = new FoodItem(name, lmvFoodNumber);
        String scientificName = "Bananus krokus";
        String lmvProject = "Bananprojektet 2000";

        food.name = name;
        food.scientificName = scientificName;
        food.lmvFoodNumber = lmvFoodNumber;
        food.lmvProject = lmvProject;

        food.save();

        FoodItem savedFood = FoodItem.find.byId(food.id);

        assertThat(savedFood, notNullValue());
        assertThat(savedFood.name, is(name));
        assertThat(savedFood.scientificName, is(scientificName));
        assertThat(savedFood.lmvFoodNumber, is(lmvFoodNumber));
        assertThat(savedFood.lmvProject, is(lmvProject));
    }

	@Test(expected = PersistenceException.class)
	public void testDuplicateLmvFoodNumbers() {
        FoodItem food1 = new FoodItem("Banana", 123);
        FoodItem food2 = new FoodItem("Banana", 123);
        food1.save();
        food2.save();
	}
}
