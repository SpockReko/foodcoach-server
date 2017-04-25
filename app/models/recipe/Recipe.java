package models.recipe;

import com.avaje.ebean.Model;
import models.food.Food;
import models.food.Nutrient;
import models.user.User;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A static recipe most likely parsed and matched with ingredients from the web.
 * Contains wrapper methods that returns the total nutrient data for all ingredients in a recipe.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "Recipes")
public class Recipe extends Model {

    @Id private long id;

    @NotNull private final String title;
    @NotNull private final int portions;
    public String description;
    public int cookingDurationMinutes;

    @ManyToMany(cascade = CascadeType.ALL) public List<Ingredient> ingredients;

    public String sourceUrl;

    public Recipe(String title, int portions, List<Ingredient> ingredients) {
        this.title = title;
        this.portions = portions;
        this.ingredients = ingredients;
    }

    public static Finder<Long, Recipe> find = new Finder<>(Recipe.class);

    public String getTitle() {
        return title;
    }
    public int getPortions() {
        return portions;
    }
    public List<Ingredient> getIngredients(){return ingredients;}

    public Double getCO2() {
        return ingredients.stream().mapToDouble(Ingredient::getCO2).sum();
    }

    public double getNutrient(Nutrient nutrient) {
        return ingredients.stream().mapToDouble(i -> i.getNutrient(nutrient)).sum();
    }

    public double getNutrientPerPortion(Nutrient nutrient) {
        return ingredients.stream().mapToDouble(i -> i.getNutrient(nutrient)).sum() / portions;
    }

    /*
    Extra calculations
     */
    public Double getEnergyPercentProtein() {
        return 4 * 100 * getNutrient(Nutrient.PROTEIN) / getNutrient(Nutrient.KCAL); // energi från protein per portion
    }

    public Double getEnergyPercentCarbohydrates() {
        return 4 * 100 * getNutrient(Nutrient.CARBOHYDRATES) / getNutrient(Nutrient.KCAL); // energi från kolhydrater per portion
    }

    public Double getEnergyPercentFat() {
        return 9 * 100 * getNutrient(Nutrient.FAT) / getNutrient(Nutrient.KCAL); // energi från fett per portion
    }

    public Double getEnergyPercentFibre() {
        return 2 * 100 * getNutrient(Nutrient.FIBRE) / getNutrient(Nutrient.KCAL);
    }

    public Recipe getOnePortionRecipe() {
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        for (Ingredient i : ingredients) {
            double onePortionValue = i.getAmount().getQuantity() / portions;
            Amount.Unit currentUnit = i.getAmount().getUnit();
            Amount newAmount = new Amount(onePortionValue, currentUnit);
            Food currentFood = i.getFood();
            Ingredient newIngredient = new Ingredient(currentFood, newAmount);
            newIngredients.add(newIngredient);
        }
        return new Recipe(this.getTitle(), 1, newIngredients);
    }

    public Recipe getUserRecipe(User user) {
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        double div = user.hmap.get(Nutrient.KCAL) / getNutrient(Nutrient.KCAL) * 0.3;
        for (Ingredient i : ingredients) {
            double onePortionValue = i.getAmount().getQuantity() * div;
            Amount.Unit currentUnit = i.getAmount().getUnit();
            Amount newAmount = new Amount(onePortionValue, currentUnit);
            Food currentFood = i.getFood();
            Ingredient newIngredient = new Ingredient(currentFood, newAmount);
            newIngredients.add(newIngredient);
        }
        return new Recipe(this.getTitle(), 1, newIngredients);
    }

    public String recipeToString(Recipe recipe) {
        String text = recipe.getTitle() + "\n\n";
        for (Ingredient i : recipe.ingredients) {
            text += i.getFood().name;
            int stringLength = i.getFood().name.length();
            if (stringLength < 8) {
                text += "\t\t\t\t\t";
            } else if (stringLength >= 8 && stringLength < 16) {
                text += "\t\t\t\t";
            } else if (stringLength >= 16 && stringLength < 24) {
                text += "\t\t\t";
            } else if (stringLength >= 24 && stringLength < 32) {
                text += "\t\t";
            } else {
                text += "\t";
            }
            text += Precision.round(i.getAmount().getQuantity(), 1) + " " + i.getAmount().getUnit()
                + "\n";
        }
        text = text + "\n\n";
        return text;
    }
}
