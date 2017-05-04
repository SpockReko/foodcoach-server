package models.recipe;

import com.avaje.ebean.Model;
import models.food.Food;
import models.food.Nutrient;
import play.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Essentially a wrapper for a {@link Food} that also contains
 * information about the amount the Food. This is used in the {@link Recipe} since
 * all ingredients are a FoodItem but are also present in a given amount.
 * Contains wrapper methods for returning all nutrient data multiplied by the amount.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "Ingredients")
public class Ingredient extends Model {

    @Id @Column(columnDefinition = "bigint unsigned") private long id;

    @ManyToOne @NotNull private final Food food;
    @Embedded @NotNull private final Amount amount;
    public String comment;
    public String original;

    @ManyToMany public List<Ingredient> alternatives = new ArrayList<>();

    /**
     * Basic constructor for an ingredient. Needs a food and an amount.
     * @param food The food the ingredient consists of.
     * @param amount The amount the food is present in.
     */
    public Ingredient(Food food, Amount amount) {
        this.food = food;
        this.amount = amount;
    }

    /**
     * Gives the attached food that the ingredient consists of.
     * @return The food the ingredient consists of.
     */
    public Food getFood() {
        return food;
    }

    /**
     * Gives the amount the ingredient is present in.
     * @return The amount the ingredient is present in.
     */
    public Amount getAmount() {
        return amount;
    }

    /**
     * Returns a total nutrient value for the specified nutrient.
     * This is multiplied with the amount.
     * @param nutrient The nutrient which you want information for. See {@link Nutrient}.
     * @return The nutrient value as a double, 0.0 if no value is present.
     */
    public double getNutrient(Nutrient nutrient) {
        return multiplier(food.getNutrient(nutrient));
    }

    /**
     * Returns estimated total CO2 emission for the ingredient in kilograms.
     * This is multiplied with the amount.
     * @return The CO2 emission value as a double, 0.0 if no value is present.
     */
    public double getCO2() {
        return multiplier(food.getCO2());
    }

    private double multiplier(double value) {
        double multiplier = amount.getUnit().getFraction() * amount.getQuantity();
        switch (amount.getUnit().getType()) {
            case VOLUME:
                if (food.densityConstant != null) {
                    multiplier *= food.densityConstant;
                } else {
                   // Logger.warn("No density constant for \"" + food.name + "\" defaulting to 1.0");
                }
                break;
            case SINGLE:
                if (food.pieceWeightGrams != null) {
                    multiplier = amount.getQuantity() * (food.pieceWeightGrams * 0.01);
                } else {
                   // Logger.warn("No piece weight for \"" + food.name + "\" defaulting to 100g");
                    multiplier = amount.getQuantity();
                }
        }
        return value * multiplier;
    }
}
