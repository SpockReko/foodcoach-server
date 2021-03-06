package models;

import models.food.DataSource;
import models.food.Food;
import models.food.Nutrient;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa on 2017-04-06.
 */
public class GlobalDummyModels {

    @NotNull
    public static Recipe createOptimalRecipeForSpecificUser(User user) {

        List<Ingredient> ingredients = new ArrayList<>();

        float convertToOnePortion = 0.3f;

        Amount amount = new Amount(100, Amount.Unit.GRAM);

        Food perfectFood = new Food("perfectFoodFor", 9999, DataSource.FINELI,
            user.hmap.get(Nutrient.ENERGY_KJ) * convertToOnePortion,
            user.hmap.get(Nutrient.CARBOHYDRATES) * convertToOnePortion,
            user.hmap.get(Nutrient.PROTEIN) * convertToOnePortion,
            user.hmap.get(Nutrient.FAT) * convertToOnePortion,
            user.hmap.get(Nutrient.FIBRE) * convertToOnePortion, 0.0, 0.0,
            //user.hmap.get(Nutrient.ALCOHOL) * convertToOnePortion, //exist not in user RDI
            //user.hmap.get(Nutrient.SALT) * convertToOnePortion, //exist not in user RDI
            user.hmap.get(Nutrient.VITAMIN_A) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_B6) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_B12) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_C) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_D) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_E) * convertToOnePortion, 0.0,
            //user.hmap.get(Nutrient.VITAMIN_K) * convertToOnePortion, //exist not in user RDI
            user.hmap.get(Nutrient.THIAMINE) * convertToOnePortion,
            user.hmap.get(Nutrient.RIBOFLAVIN) * convertToOnePortion,
            user.hmap.get(Nutrient.NIACIN) * convertToOnePortion, 0.0,
            //user.hmap.get(Nutrient.NIACIN_EQ) * convertToOnePortion, //exist not in user RDI
            user.hmap.get(Nutrient.FOLATE) * convertToOnePortion,
            user.hmap.get(Nutrient.PHOSPHORUS) * convertToOnePortion,
            user.hmap.get(Nutrient.IODINE) * convertToOnePortion,
            user.hmap.get(Nutrient.IRON) * convertToOnePortion,
            user.hmap.get(Nutrient.CALCIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.POTASSIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.MAGNESIUM) * convertToOnePortion, 0.0,
            //user.hmap.get(Nutrient.SODIUM) * convertToOnePortion, //exist not in user RDI
            user.hmap.get(Nutrient.SELENIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.ZINC) * convertToOnePortion);

        Ingredient perfectIngredient = new Ingredient(perfectFood, amount);
        ingredients.add(perfectIngredient);

        return new Recipe("Recipe", 1, ingredients);
    }


    // Nutrient taken from the finish food database
    public static Food getFoodDummyCarrot() {
        return new Food("Carrot", 300, DataSource.FINELI, 317.0, 8.1, 0.6, 0.2, 2.5, 0.0, 0.0,
            774.12, 0.03, 0.0, 6.5, 0.0, 0.36, 19.0, 0.07, 0.07, 0.8, 0.9, 16.4, 40.0, 1.0, 0.5,
            29.0, 390.0, 14.0, 25.0, 0.2, 0.4);
    }

    public static Food getFoodDummyParsnip() {
        return new Food("Parsnips", 309, DataSource.FINELI, 266.0, 11.8, 0.5, 0.4, 4.5, 0.0, 2.0,
            0.3, 0.03, 0.0, 6.5, 0.0, 0.4, 19.0, 0.07, 0.07, 0.8, 0.9, 16.4, 40.0, 1.0, 0.5, 29.0,
            390.0, 14.0, 25.0, 0.2, 0.4);
    }

    public static Food getFoodDummyChicken() {
        return new Food("Chicken", 11565, DataSource.FINELI, 456.0, 0.0, 22.4, 2.0, 0.0, 0.0, 86.6,
            11.0, 0.51, 1.0, 0.0, 0.1, 0.7, 15.0, 0.14, 0.18, 7.8, 11.8, 15.6, 160.0, 6.0, 0.4, 6.0,
            380.0, 26.0, 34.0, 24.0, 0.7);
    }

    public static Food getFoodDummyBean() {
        return new Food("Beans", 373, DataSource.FINELI, 464.7, 14.0, 8.3, 0.8, 7.0, 0.0, 12.74,
            39.960, 0.18, 0.0, 0.0, 0.0, 0.129, 27.0, 0.12, 0.05, 0.7, 2.223, 167.58, 147.9, 0.0,
            1.886, 81.66, 364.97, 48.910, 5.00, 0.390, 0.81);
    }

    public static Food getFoodDummyRedLenses() {
        return new Food("Red Lenses", 31225, DataSource.FINELI, 424.4, 15.6, 7.6, 0.4, 1.9, 0.0,
            30.58, 20.0, 0.11, 0.0, 0.0, 0.0, 0.11, 1.7, 0.11, 0.04, 0.4, 1.402, 5.00, 100.0, 0.0,
            2.400, 16.00, 220.00, 220.00, 12.00, 2.00, 1.00);
    }

    public static Food getFoodDummySteak() {
        return new Food("Beef, ", 31145, DataSource.FINELI, 795.43, 1.4, 22.19, 14.375, 0.0, 0.0,
            1054.51, 3.163, 0.11, 0.91, 0.0, 0.13, 2.555, 21.396, 0.0715, 0.11, 3.7, 7.179, 0.76,
            137.3, 2.05, 2.020, 7.50, 260.42, 260.42, 413.86, 10.415, 2.471);
    }
}
