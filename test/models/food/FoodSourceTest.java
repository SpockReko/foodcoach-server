package models.food;

import org.junit.Test;

import javax.persistence.PersistenceException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class FoodSourceTest extends FakeApplicationInMemoryDB {

    @Test
    public void testFoodItemLink() {
        FoodItem food = new FoodItem("Banana", 123);
        FoodSource source = new FoodSource("Tree", "A1234");
        source.foodItems.add(food);
        source.save();

        FoodItem dbFood = FoodItem.find.byId(food.getId());
        FoodSource dbGroup = FoodSource.find.byId(source.id);

        assertTrue(dbGroup.foodItems.contains(dbFood));
        assertTrue(dbFood.sources.contains(dbGroup));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectLangualCode() {
        FoodSource group = new FoodSource("Tree", "34");
        group.save();
    }

    @Test(expected = PersistenceException.class)
    public void testDuplicateLangualCode() {
        FoodSource source1 = new FoodSource("Tree", "A1234");
        FoodSource source2 = new FoodSource("Dirt", "A1234");
        source1.save();
        source2.save();
    }

    @Test(expected = PersistenceException.class)
    public void testNullName() {
        FoodSource source = new FoodSource(null, "X9999");
        source.save();
    }

    @Test
    public void testParentLink() {
        FoodSource group = new FoodSource("Tree", "A0001");
        FoodSource parent = new FoodSource("OldTree", "A0002");
        FoodSource grandParent = new FoodSource("AncientTree", "A0003");
        group.parent = parent;
        parent.parent = grandParent;
        group.save();

        FoodSource dbSource = FoodSource.find.byId(group.id);
        assertThat(dbSource.parent.parent.getLangualCode(), is(grandParent.getLangualCode()));
    }

    @Test
    public void testNotCascadeDelete() {
        FoodSource source = new FoodSource("Tree", "A1001");
        FoodSource parent = new FoodSource("OldTree", "A1002");
        source.parent = parent;
        source.save();
        source.delete();

        FoodSource dbSource = FoodSource.find.byId(parent.id);
        assertThat(dbSource, notNullValue());
    }
}
