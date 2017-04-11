package models;

import algorithms.QuicksortFoodItem;
import models.food.*;
import models.recipe.Amount;
import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.Nutrient;
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
                new Fats(Float.parseFloat(user.hmap.get(Nutrient.Fat) + "") * convertToOnePortion,
                        0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F);

        Vitamins vitamins = new Vitamins(0F, 0F,
                Float.parseFloat(user.hmap.get(Nutrient.VitaminAUG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VitaminB6MG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VitaminB12UG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VitaminCMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VitaminDUG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.VitaminEMG) + "") * convertToOnePortion, 0F,
                Float.parseFloat(user.hmap.get(Nutrient.ThiamineMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.RiboflavinMG) + "") * convertToOnePortion,
                Float.parseFloat(user.hmap.get(Nutrient.NiacinMG) + "") * convertToOnePortion, 0F);

        Minerals minerals =
                new Minerals(Float.parseFloat(user.hmap.get(Nutrient.FolateUG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.PhosphorusMG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.IodineUG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.IronMG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.CalciumMG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.PotassiumMG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.MagnesiumMG) + "") * convertToOnePortion, 0F, 0F,
                        Float.parseFloat(user.hmap.get(Nutrient.SeleniumUG) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.ZinkMG) + "") * convertToOnePortion);

        Amount amount = new Amount(100, Amount.Unit.GRAM);

        Ingredient perfectIngredient = new Ingredient(
                new FoodItem("perfectFoodFor" + user.firstName, "perfektus foodus", 9999, "perfekt",
                        Float.parseFloat(user.hmap.get(Nutrient.CaloriKcal) + "") * convertToOnePortion, 0F,
                        Float.parseFloat(user.hmap.get(Nutrient.Carbohydrates) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.Protein) + "") * convertToOnePortion,
                        Float.parseFloat(user.hmap.get(Nutrient.Fibre) + "")* convertToOnePortion, 0F, 0F,
                        0F, 0F, 0F, 0F, sugars, fats, vitamins, minerals), amount);
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
