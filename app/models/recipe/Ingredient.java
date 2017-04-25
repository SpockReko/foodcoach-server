package models.recipe;

import com.avaje.ebean.Model;
import models.food.Food;
import models.food.Nutrient;
import play.Logger;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL) public List<Recipe> recipes;

    public Ingredient(Food food, Amount amount) {
        this.food = food;
        this.amount = amount;
    }

    public Ingredient(Food food, Amount amount, String comment) {
        this.food = food;
        this.amount = amount;
        this.comment = comment;
    }

    public Food getFood() {
        return food;
    }
    public Amount getAmount() {
        return amount;
    }

    public double getNutrient(Nutrient nutrient) {
        return multiplier(food.getNutrient(nutrient));
    }

    public Double getCO2() {
        return multiplier(food.getCO2());
    }

    private double multiplier(double value) {
        double multiplier = amount.getUnit().getFraction() * amount.getQuantity();
        switch (amount.getUnit().getType()) {
            case VOLUME:
                if (food.densityConstant != null) {
                    multiplier *= food.densityConstant;
                } else {
                    Logger.warn("No density constant for \"" + food.name + "\" defaulting to 1.0");
                }
                break;
            case SINGLE:
                if (food.pieceWeightGrams != null) {
                    multiplier = amount.getQuantity() * (food.pieceWeightGrams * 0.01);
                } else {
                    Logger.warn("No piece weight for \"" + food.name + "\" defaulting to 100g");
                    multiplier = amount.getQuantity();
                }
        }
        return value * multiplier;
    }
}
