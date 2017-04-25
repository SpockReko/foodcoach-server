package algorithms;

import models.food.Food;
import models.food.Nutrient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa on 2017-03-29.
 */

public class QuicksortFoodItem {
    private static List<Food> foods = new ArrayList<>();
    private static int number = 0;
    private static Food foodWeCompare;

    public static List<Food> sort(List<Food> values, Food food) {
        // check for empty or null array
        if (values == null || values.size() == 0){
            return new ArrayList<>();
        }
        foodWeCompare = food;
        foods = new ArrayList<>(values);
        number = values.size();
        quicksort(0, number - 1);
        return foods;
    }

    private static void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        Food pivot = foods.get(low + (high-low)/2);

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (diff(foods.get(i)) < diff(pivot)) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (diff(foods.get(j)) > diff(pivot)) {
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

        foods.set(i, foods.set(j, foods.get(i)));

    }


    private static Double diff(Food food1) {

        double sum = 0d;

        sum +=
            Math.abs(food1.getNutrient(Nutrient.ENERGY_KJ) - foodWeCompare.getNutrient(Nutrient.ENERGY_KJ)) * 20;
        sum += Math.abs(food1.getNutrient(Nutrient.CARBOHYDRATES) - foodWeCompare.getNutrient(Nutrient.CARBOHYDRATES))
            * 20;
        sum += Math.abs(food1.getNutrient(Nutrient.FAT) - foodWeCompare.getNutrient(Nutrient.FAT))
            * 20;
        sum += Math.abs(food1.getNutrient(Nutrient.PROTEIN) - foodWeCompare.getNutrient(Nutrient.PROTEIN))
            * 20;
        sum += Math.abs(food1.getNutrient(Nutrient.FIBRE) - foodWeCompare.getNutrient(Nutrient.FIBRE));

        //sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_A) - foodWeCompare.getNutrient(Nutrient.VITAMIN_A));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_D) - foodWeCompare.getNutrient(Nutrient.VITAMIN_D));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_E) - foodWeCompare.getNutrient(Nutrient.VITAMIN_E));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_B6) - foodWeCompare.getNutrient(Nutrient.VITAMIN_B6));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_B12) - foodWeCompare.getNutrient(Nutrient.VITAMIN_B12));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_C) - foodWeCompare.getNutrient(Nutrient.VITAMIN_C));
        //sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_K) - foodWeCompare.getNutrient(Nutrient.VITAMIN_K));

        sum += Math.abs(food1.getNutrient(Nutrient.CALCIUM) - foodWeCompare.getNutrient(Nutrient.CALCIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.FOLATE) - foodWeCompare.getNutrient(Nutrient.FOLATE));
        //sum += Math.abs(food1.getNutrient(Nutrient.IODINE) - foodWeCompare.getNutrient(Nutrient.IODINE));
        sum += Math.abs(food1.getNutrient(Nutrient.IRON) - foodWeCompare.getNutrient(Nutrient.IRON));
        sum += Math.abs(food1.getNutrient(Nutrient.MAGNESIUM) - foodWeCompare.getNutrient(Nutrient.MAGNESIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.PHOSPHORUS) - foodWeCompare.getNutrient(Nutrient.PHOSPHORUS));
        sum += Math.abs(food1.getNutrient(Nutrient.POTASSIUM) - foodWeCompare.getNutrient(Nutrient.POTASSIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.SALT) - foodWeCompare.getNutrient(Nutrient.SALT));
        sum += Math.abs(food1.getNutrient(Nutrient.SELENIUM) - foodWeCompare.getNutrient(Nutrient.SELENIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.SODIUM) - foodWeCompare.getNutrient(Nutrient.SODIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.ZINC) - foodWeCompare.getNutrient(Nutrient.ZINC));

        return sum;
    }


    public static Double diff(Food food1, Food foodWeCompare) {

        double sum = 0f;

        sum += Math.abs(food1.getNutrient(Nutrient.ENERGY_KCAL) - foodWeCompare.getNutrient(Nutrient.ENERGY_KCAL))*20;
        sum += Math.abs(food1.getNutrient(Nutrient.CARBOHYDRATES) - foodWeCompare.getNutrient(Nutrient.CARBOHYDRATES))*20;
        sum += Math.abs(food1.getNutrient(Nutrient.FAT) - foodWeCompare.getNutrient(Nutrient.FAT))*20;
        sum += Math.abs(food1.getNutrient(Nutrient.PROTEIN) - foodWeCompare.getNutrient(Nutrient.PROTEIN))*20;
        sum += Math.abs(food1.getNutrient(Nutrient.FIBRE) - foodWeCompare.getNutrient(Nutrient.FIBRE))*20;

        //sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_A) - foodWeCompare.getNutrient(Nutrient.VITAMIN_A));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_D) - foodWeCompare.getNutrient(Nutrient.VITAMIN_D));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_E) - foodWeCompare.getNutrient(Nutrient.VITAMIN_E));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_B6) - foodWeCompare.getNutrient(Nutrient.VITAMIN_B6));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_B12) - foodWeCompare.getNutrient(Nutrient.VITAMIN_B12));
        sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_C) - foodWeCompare.getNutrient(Nutrient.VITAMIN_C));
        //sum += Math.abs(food1.getNutrient(Nutrient.VITAMIN_K) - foodWeCompare.getNutrient(Nutrient.VITAMIN_K));

        sum += Math.abs(food1.getNutrient(Nutrient.CALCIUM) - foodWeCompare.getNutrient(Nutrient.CALCIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.FOLATE) - foodWeCompare.getNutrient(Nutrient.FOLATE));
        //sum += Math.abs(food1.getNutrient(Nutrient.IODINE) - foodWeCompare.getNutrient(Nutrient.IODINE));
        sum += Math.abs(food1.getNutrient(Nutrient.IRON) - foodWeCompare.getNutrient(Nutrient.IRON));
        sum += Math.abs(food1.getNutrient(Nutrient.MAGNESIUM) - foodWeCompare.getNutrient(Nutrient.MAGNESIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.PHOSPHORUS) - foodWeCompare.getNutrient(Nutrient.PHOSPHORUS));
        sum += Math.abs(food1.getNutrient(Nutrient.POTASSIUM) - foodWeCompare.getNutrient(Nutrient.POTASSIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.SALT) - foodWeCompare.getNutrient(Nutrient.SALT));
        sum += Math.abs(food1.getNutrient(Nutrient.SELENIUM) - foodWeCompare.getNutrient(Nutrient.SELENIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.SODIUM) - foodWeCompare.getNutrient(Nutrient.SODIUM));
        sum += Math.abs(food1.getNutrient(Nutrient.ZINC) - foodWeCompare.getNutrient(Nutrient.ZINC));
        //sum += Math.abs(food1.getSugars().getSugars() - foodWeCompare.getSugars().getSugars());
        return sum;
    }

}

