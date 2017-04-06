package algorithms;

import models.food.FoodItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa on 2017-03-29.
 */

public class QuicksortFoodItem {
    private static List<FoodItem> foodItems;
    private static int number;
    private static FoodItem foodItemWeCompare;

    public static List<FoodItem> sort(List<FoodItem> values, FoodItem food) {
        // check for empty or null array
        if (values == null || values.size() == 0){
            return new ArrayList<>();
        }
        foodItemWeCompare = food;
        foodItems = values;
        number = values.size();
        quicksort(0, number - 1);
        return foodItems;
    }

    private static void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        FoodItem pivot = foodItems.get(low + (high-low)/2);

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (diff(foodItems.get(i)) > diff(pivot)) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (diff(foodItems.get(j)) < diff(pivot)) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }

    private static void exchange(int i, int j) {
        FoodItem temp = foodItems.get(i);
        foodItems.add(i,foodItems.get(j));
        foodItems.add(j,temp);
    }


    private static Float diff(FoodItem foodItem1) {

        float sum = 0f;

        sum += Math.abs(foodItem1.getEnergyKcal() - foodItemWeCompare.getEnergyKcal());
        sum += Math.abs(foodItem1.getCarbohydrates() - foodItemWeCompare.getCarbohydrates())*20;
        sum += Math.abs(foodItem1.getFats().getFat() - foodItemWeCompare.getFats().getFat())*20;
        sum += Math.abs(foodItem1.getProtein() - foodItemWeCompare.getProtein())*20;
        sum += Math.abs(foodItem1.getFibre() - foodItemWeCompare.getFibre());

        sum += Math.abs(foodItem1.getVitamins().getVitaminA() - foodItemWeCompare.getVitamins().getVitaminA());
        sum += Math.abs(foodItem1.getVitamins().getVitaminD() - foodItemWeCompare.getVitamins().getVitaminD());
        sum += Math.abs(foodItem1.getVitamins().getVitaminE() - foodItemWeCompare.getVitamins().getVitaminE());
        sum += Math.abs(foodItem1.getVitamins().getVitaminB6() - foodItemWeCompare.getVitamins().getVitaminB6());
        sum += Math.abs(foodItem1.getVitamins().getVitaminB12() - foodItemWeCompare.getVitamins().getVitaminB12());
        sum += Math.abs(foodItem1.getVitamins().getVitaminC() - foodItemWeCompare.getVitamins().getVitaminC());
        sum += Math.abs(foodItem1.getVitamins().getVitaminK() - foodItemWeCompare.getVitamins().getVitaminK());

        sum += Math.abs(foodItem1.getMinerals().getCalcium() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getFolate() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getIodine() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getIron() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getMagnesium() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getPhosphorus() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getPotassium() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getSalt() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getSelenium() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getSodium() - foodItemWeCompare.getMinerals().getCalcium());
        sum += Math.abs(foodItem1.getMinerals().getZink() - foodItemWeCompare.getMinerals().getCalcium());
        //sum += Math.abs(foodItem1.getSugars().getSugars() - foodItemWeCompare.getSugars().getSugars());

        return sum;
    }
}

