package algorithms;

import models.GlobalDummyModels;
import models.food.Food;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by stefa on 2017-04-04.
 */
public class QuicksortTest {

    private static Food food1;
    private static Food food2;
    private static Food food3;
    private static Food food4;
    private static Food food5;
    private static Food food6;
    private static List<Food> foodList = new ArrayList<>();
    private static List<Food> sortedFoodList = new ArrayList<>();
    private static int foodSize = 0;
    @BeforeClass
    public static void init(){
        System.out.print("Quicksort");
        food1 = GlobalDummyModels.getFoodDummyCarrot();
        food2 = GlobalDummyModels.getFoodDummyParsnip();
        food3 = GlobalDummyModels.getFoodDummyChicken();
        food4 = GlobalDummyModels.getFoodDummySteak();
        food5 = GlobalDummyModels.getFoodDummyRedLenses();
        food6 = GlobalDummyModels.getFoodDummyBean();
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
    public void testOrderOfOrginalTheList(){
        Food f0 = sortedFoodList.get(0);
        Food f1 = sortedFoodList.get(1);
        Food f2 = sortedFoodList.get(2);
        Food f3 = sortedFoodList.get(3);
        Food f4 = sortedFoodList.get(4);
        assertTrue(QuicksortFoodItem.diff(f1,f0) <= QuicksortFoodItem.diff(f2,f0) &&
                QuicksortFoodItem.diff(f2,f0) <= QuicksortFoodItem.diff(f3,f0) &&
                QuicksortFoodItem.diff(f3,f0) <= QuicksortFoodItem.diff(f4,f0));
    }

    private static void printList(List<Food> list) {
        List<Food> listCopy = new ArrayList<>(list);
        Iterator ite = listCopy.iterator();
        System.out.println("New Foodlist! ");
        while(ite.hasNext()){
            Food f = (Food) ite.next();
            System.out.println(".\t\t: " + f.name + " with diff " +  QuicksortFoodItem.diff(f,food2));
            ite.remove();
        }
        System.out.println("Foodlist ended\n");

    }

}
