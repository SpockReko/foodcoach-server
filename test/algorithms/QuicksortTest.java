package algorithms;

import models.GlobalDummyModels;
import models.food.*;
import models.user.User;
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
    private static FoodItem food4;
    private static FoodItem food5;
    private static Fats fat;
    private static Vitamins vit;
    private static Minerals min;
    private static Sugars sug;
    private static List<FoodItem> foodList = new ArrayList<>();
    private static List<FoodItem> sortedFoodList = new ArrayList<>();
    private static int foodSize = 0;
    @BeforeClass
    public static void init(){
        fat = new Fats(1f,0f,0f,0f,0f,0f,0f,
                0f,0f,0f,0f,0f,0f,0f,0f,0f,
                0f,0f);
        vit = new Vitamins(1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F,
                1F,1F, 1F, 1F);
        min = new Minerals(1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F,
                1F,1F);
        sug = new Sugars(1f,1f,1f,1f);

        food1 = new FoodItem("food1", "h", 1, "GH", 100F,
                1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F,
                1F, sug, fat, vit, min);

        food2 = new FoodItem("food2", "h", 1, "GH", 200F,
                2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F,
                2F, sug,fat,vit,min);

        food3 = new FoodItem("food3", "h", 3, "GH", 300F,
                3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F,
                3F, sug,fat,vit,min);
        food4 =GlobalDummyModels.createOptimalRecipeForSpecificUser(new User()).ingredients.get(0).getFoodItem();
        food5 =GlobalDummyModels.createOptimalRecipeForSpecificUser(new User(1)).ingredients.get(0).getFoodItem();

        foodList.add(food1);
        foodList.add(food2);
        foodList.add(food3);
        foodList.add(food4);
        foodList.add(food5);
        foodSize = foodList.size();
        sortedFoodList=QuicksortFoodItem.sort(new ArrayList<>(foodList), food2);

        printList(foodList);
        printList(sortedFoodList);

    }

    @Test
    public void notNull(){
        assertNotNull(sortedFoodList);
    }

    @Test
    public void sizeNotChangeTest() {
        assertTrue(sortedFoodList.size()==foodSize);
    }

    @Test
    public void allFoodsStillThere() {
        assertTrue(sortedFoodList.contains(food1)
                && sortedFoodList.contains(food2)
                && sortedFoodList.contains(food3)
                && sortedFoodList.contains(food4)
                && sortedFoodList.contains(food5));
    }

    @Test
    public void testOrderOfTheList(){
        FoodItem f0 = sortedFoodList.get(0);
        FoodItem f1 = sortedFoodList.get(1);
        FoodItem f2 = sortedFoodList.get(2);
        FoodItem f3 = sortedFoodList.get(3);
        FoodItem f4 = sortedFoodList.get(4);
        assertTrue(QuicksortFoodItem.diff(f1,f0) <= QuicksortFoodItem.diff(f2,f0) &&
                QuicksortFoodItem.diff(f2,f0) <= QuicksortFoodItem.diff(f3,f0) &&
                QuicksortFoodItem.diff(f3,f0) <= QuicksortFoodItem.diff(f4,f0));
    }

    private static void printList(List<FoodItem> list) {
        List<FoodItem> listCopy = new ArrayList<>(list);
        Iterator ite = listCopy.iterator();
        System.out.println("New Foodlist! ");
        while(ite.hasNext()){
            FoodItem f = (FoodItem) ite.next();
            System.out.println(".\t\t: " + f.getName() + " with diff " +  QuicksortFoodItem.diff(f,food2));
            ite.remove();
        }
        System.out.println("Foodlist ended\n");

    }

}
