package models.food;

import org.junit.Test;

import javax.persistence.PersistenceException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class FoodGroupTest extends FakeApplicationInMemoryDB {

    @Test
    public void testFoodItemLink() {
        FoodItem food = new FoodItem("Banana", 123);
        FoodGroup group = new FoodGroup("Candy", "A1234");
        group.foodItems.add(food);
        group.save();

        FoodItem dbFood = FoodItem.find.byId(food.id);
        FoodGroup dbGroup = FoodGroup.find.byId(group.id);

        assertTrue(dbGroup.foodItems.contains(dbFood));
        assertTrue(dbFood.groups.contains(dbGroup));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectLangualCode() {
        FoodGroup group = new FoodGroup("Candy", "34");
        group.save();
    }

    @Test(expected = PersistenceException.class)
    public void testDuplicateLangualCode() {
        FoodGroup group1 = new FoodGroup("Candy", "A1234");
        FoodGroup group2 = new FoodGroup("Banana", "A1234");
        group1.save();
        group2.save();
    }

    @Test(expected = PersistenceException.class)
    public void testNullName() {
        FoodGroup group = new FoodGroup(null, "X9999");
        group.save();
    }

    @Test
    public void testParentLink() {
        FoodGroup group = new FoodGroup("Candy", "A0001");
        FoodGroup parent = new FoodGroup("OldCandy", "A0002");
        FoodGroup grandParent = new FoodGroup("AncientCandy", "A0003");
        group.parent = parent;
        parent.parent = grandParent;
        group.save();

        FoodGroup dbGroup = FoodGroup.find.byId(group.id);
        assertThat(dbGroup.parent.parent.getLangualCode(), is(grandParent.getLangualCode()));
    }

    @Test
    public void testNotCascadeDelete() {
        FoodGroup group = new FoodGroup("Candy", "A1001");
        FoodGroup parent = new FoodGroup("OldCandy", "A1002");
        group.parent = parent;
        group.save();
        group.delete();

        FoodGroup dbGroup = FoodGroup.find.byId(parent.id);
        assertThat(dbGroup, notNullValue());
    }
}
