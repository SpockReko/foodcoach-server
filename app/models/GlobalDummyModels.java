package models;

import models.food.*;
import models.food.fineli.Food;
import models.food.fineli.Nutrient;
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

        Sugars sugars = new Sugars(0F, 0F, 0F, 0F);

        Fats fats =
                new Fats(Float.parseFloat(user.hmap.get(Nutrient.FAT) + "") * convertToOnePortion,
                        0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F);

        Vitamins vitamins = new Vitamins(0F, 0F,
                Float.parseFloat(user.hmap.get(Nutrient.VITAMIN_A) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VITAMIN_B6) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VITAMIN_B12) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VITAMIN_C) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VITAMIN_D) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VITAMIN_E) + "") * convertToOnePortion, 0F,
                Float.parseFloat(user.hmap.get(Nutrient.THIAMINE) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.RIBOFLAVIN) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.NIACIN) + "") * convertToOnePortion, 0F);

        Minerals minerals =
                new Minerals(Float.parseFloat(user.hmap.get(Nutrient.FOLATE) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.PHOSPHORUS) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.IODINE) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.IRON) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.CALCIUM) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.POTASSIUM) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.MAGNESIUM) + "") * convertToOnePortion, 0F, 0F,
                        Float.parseFloat(user.hmap.get(Nutrient.SELENIUM) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.ZINC) + "") * convertToOnePortion);

        Amount amount = new Amount(100, Amount.Unit.GRAM);

        Food perfectFood = new Food(
            "perfectFoodFor",
            9999,
            user.hmap.get(Nutrient.KJ) * convertToOnePortion,
            user.hmap.get(Nutrient.CARBOHYDRATES) * convertToOnePortion,
            user.hmap.get(Nutrient.PROTEIN) * convertToOnePortion,
            user.hmap.get(Nutrient.FAT) * convertToOnePortion,
            user.hmap.get(Nutrient.FIBRE) * convertToOnePortion,
            user.hmap.get(Nutrient.ALCOHOL) * convertToOnePortion,
            user.hmap.get(Nutrient.SALT) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_A) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_B6) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_B12) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_C) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_D) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_E) * convertToOnePortion,
            user.hmap.get(Nutrient.VITAMIN_K) * convertToOnePortion,
            user.hmap.get(Nutrient.THIAMINE) * convertToOnePortion,
            user.hmap.get(Nutrient.RIBOFLAVIN) * convertToOnePortion,
            user.hmap.get(Nutrient.NIACIN) * convertToOnePortion,
            user.hmap.get(Nutrient.NIACIN_EQ) * convertToOnePortion,
            user.hmap.get(Nutrient.FOLATE) * convertToOnePortion,
            user.hmap.get(Nutrient.PHOSPHORUS) * convertToOnePortion,
            user.hmap.get(Nutrient.IODINE) * convertToOnePortion,
            user.hmap.get(Nutrient.IRON) * convertToOnePortion,
            user.hmap.get(Nutrient.CALCIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.POTASSIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.MAGNESIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.SODIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.SELENIUM) * convertToOnePortion,
            user.hmap.get(Nutrient.ZINC) * convertToOnePortion
            );

        Ingredient perfectIngredient = new Ingredient(perfectFood, amount);
        ingredients.add(perfectIngredient);

        return new Recipe(user.firstName + "Recipe", 1, ingredients);
    }

    public static List<FoodItem> getFoodItemDummy() {

        List<FoodItem> foodList = new ArrayList<>();

        Fats fat = new Fats(1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        Vitamins vit = new Vitamins(1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F);
        Minerals min = new Minerals(1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F);
        Sugars sug = new Sugars(1f,1f,1f,1f);
        FoodItem food1 = new FoodItem("food1", "h", 1, "GH", 100F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F, sug, fat, vit, min);
        FoodItem food2 = new FoodItem("food2", "h", 1, "GH", 200F, 2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F, 2F, sug, fat, vit, min);
        FoodItem food3 = new FoodItem("food3", "h", 3, "GH", 300F, 3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F, 3F, sug, fat, vit, min);

        foodList.add(food1);
        foodList.add(food2);
        foodList.add(food3);

        return foodList;
    }

    public static FoodItem getDummyFoodItem(long nr){
        return FoodItem.find.byId(nr);
    }

}
