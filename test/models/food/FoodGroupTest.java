package models.food;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FoodGroupTest extends FakeApplicationInMemoryDB {

    @Test
    public void testFoodItemLink() {
        FoodItem food = new FoodItem("Banana", 123);
        FoodGroup group = new FoodGroup("Candy", "A1234");
        group.foodItems.add(food);
        food.save();
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
}
