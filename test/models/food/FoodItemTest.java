package models.food;

import helpers.FakeApplicationInMemoryDB;
import org.junit.Test;

import javax.persistence.PersistenceException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class FoodItemTest extends FakeApplicationInMemoryDB {

    @Test
    public void testBasicFields() {
        String name = "Banana";
        int lmvFoodNumber = 123;
        FoodItem food = new FoodItem(name, lmvFoodNumber);

        food.save();

        FoodItem savedFood = FoodItem.find.byId(food.getId());

        assertThat(savedFood, notNullValue());
        assertThat(savedFood.getName(), is(name));
        assertThat(savedFood.getLmvFoodNumber(), is(lmvFoodNumber));
    }

    @Test(expected = PersistenceException.class)
    public void testNullName() {
        FoodItem food = new FoodItem(null, 123);
        food.save();
    }

    @Test(expected = PersistenceException.class)
    public void testDuplicateLmvFoodNumbers() {
        FoodItem food1 = new FoodItem("Banana", 123);
        FoodItem food2 = new FoodItem("Banana", 123);
        food1.save();
        food2.save();
    }

    @Test
    public void testCascadePersist() {
        FoodItem food = new FoodItem("Banana", 100);
        FoodGroup group = new FoodGroup("Candy", "A1002");
        FoodSource source = new FoodSource("Tree", "A1003");
        food.groups.add(group);
        food.source = source;
        food.save();

        FoodGroup dbGroup = FoodGroup.find.where().eq("foodItems", food).findUnique();
        FoodSource dbSource = FoodSource.find.where().eq("foodItems", food).findUnique();
        assertThat(dbGroup, notNullValue());
        assertThat(dbSource, notNullValue());
    }

    @Test
    public void testNotCascadeDelete() {
        FoodItem food = new FoodItem("Banana", 30);
        FoodGroup group = new FoodGroup("Fruit", "A4444");
        FoodSource source = new FoodSource("Tree", "A5555");
        food.groups.add(group);
        food.source = source;
        food.save();
        food.delete();

        FoodGroup dbGroup = FoodGroup.find.byId(group.getId());
        FoodSource dbSource = FoodSource.find.byId(source.getId());
        assertThat(dbGroup, notNullValue());
        assertThat(dbSource, notNullValue());
        assertThat(dbGroup.getName(), is("Fruit"));
        assertThat(dbGroup.getLangualCode(), is("A4444"));
        assertThat(dbSource.getName(), is("Tree"));
        assertThat(dbSource.getLangualCode(), is("A5555"));
        assertTrue(dbGroup.foodItems.isEmpty());
        assertTrue(dbSource.foodItems.isEmpty());
    }
}
