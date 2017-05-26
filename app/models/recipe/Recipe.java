package models.recipe;

import algorithms.NutritionAlgorithms;
import com.avaje.ebean.Model;
import models.food.Food;
import models.food.Nutrient;
import models.user.User;
import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
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

    public double getCO2() {
        return ingredients.stream().mapToDouble(Ingredient::getCO2).sum();
    }

    public double getCO2PerPortion() {
        return ingredients.stream().mapToDouble(Ingredient::getCO2).sum() / portions;
    }

    public double getNutrient(Nutrient nutrient) {
        return ingredients.stream().mapToDouble(i -> i.getNutrient(nutrient)).sum();
    }

    public double getNutrientPerPortion(Nutrient nutrient) {
        return ingredients.stream().mapToDouble(i -> i.getNutrient(nutrient)).sum() / portions;
    }

    public HashMap<Nutrient,Double> getNutrientsContent(){
        List<Recipe> r = new ArrayList<>();
        r.add(this);
        Menu m = new Menu(r);
        return NutritionAlgorithms.nutrientsContent(m);
    }

    /*
    Extra calculations
     */

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

    public Recipe getRecipeInGram() {
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        for (Ingredient i : ingredients) {
            Ingredient newIngredient = i.getIngredientInGrams();
            newIngredients.add(newIngredient);
        }
        return new Recipe(this.title,this.portions,newIngredients);
    }

    public Recipe getUserRecipe(User user) {
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        double div = user.hmap.get(Nutrient.ENERGY_KCAL) / getNutrient(Nutrient.ENERGY_KCAL) * 0.3;
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

    public Recipe getInSameUnit(Recipe originalRecipe){
        List<Ingredient> ingredients1 = getIngredients();
        List<Ingredient> ingredients2 = originalRecipe.getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        for (int i=0; i<ingredients1.size(); i++) {
            Ingredient newIngredient = ingredients1.get(i).getInUnit(ingredients2.get(i).getAmount().getUnit());
            newIngredients.add(newIngredient);
        }
        return new Recipe(this.title,this.portions,newIngredients);
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
                + "\t\t => "+ Precision.round(i.getInGrams(), 1) + " grams" + "\n";
        }
        text = text + "\n\n";
        return text;
    }


}
