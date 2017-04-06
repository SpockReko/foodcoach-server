package algorithms;

import models.food.Fats;
import models.food.FoodItem;
import models.food.Minerals;
import models.food.Vitamins;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by stefa on 2017-04-04.
 */
public class QuicksortTest {

    private static FoodItem food1;
    private static FoodItem food2;
    private static FoodItem food3;
    private static Fats fat;
    private static Vitamins vit;
    private static Minerals min;
    private static List<FoodItem> foodList = new ArrayList<>();
    private static List<FoodItem> sortedFoodList = new ArrayList<>();

    @BeforeClass
    public static void init(){
        fat = new Fats(1f,0f,0f,0f,0f,0f,0f,
                0f,0f,0f,0f,0f,0f,0f,0f,0f,
                0f,0f);
        vit = new Vitamins(1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F,
                1F,1F, 1F, 1F);
        min = new Minerals(1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F,
                1F,1F);
        food1 = new FoodItem("food1", "h", 1, "GH", 100F,
                1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F,
                1F, null, fat, vit, min);

        food2 = new FoodItem("food2", "h", 1, "GH", 200F,
                2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F,
                2F, null,fat,vit,min);

        food3 = new FoodItem("food3", "h", 3, "GH", 300F,
                3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F,
                3F, null,fat,vit,min);

        foodList.add(food1);
        foodList.add(food2);
        foodList.add(food3);
        sortedFoodList=QuicksortFoodItem.sort(foodList, food2);

    }

    @Before
    public void beforeTest(){

    }

    @Test
    public void notNull(){
        assertNotNull(sortedFoodList);
    }

    @Test
    public void sizeNotChangeTest() {
        assertTrue(sortedFoodList.size()==foodList.size());
    }

    @Test
    public void allFoodsStillThere() {
        assertTrue(sortedFoodList.contains(food1)
                && sortedFoodList.contains(food2)
                && sortedFoodList.contains(food3));
    }


    @Test
    public void isItTrue(){
        assertTrue(true);
        assertFalse(false);
        assertEquals(1,1);
        assertNull(null);
        assertNotNull(1);
    }

}
