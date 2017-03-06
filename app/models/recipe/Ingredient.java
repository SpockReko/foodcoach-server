package models.recipe;

import com.avaje.ebean.Model;
import models.food.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Essentially a wrapper for a {@link FoodItem} that also contains
 * information about the amount the FoodItem. This is used in the {@link Recipe} since
 * all ingredients are a FoodItem but are also present in a given amount.
 * Contains wrapper methods for returning all nutrient data multiplied by the amount.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "Ingredients")
public class Ingredient extends Model {

    @Id @Column(columnDefinition = "bigint unsigned") private long id;

    @ManyToOne @NotNull private final FoodItem foodItem;
    @Embedded @NotNull private final Amount amount;

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL) public List<Recipe> recipes;

    public Ingredient(FoodItem foodItem, Amount amount) {
        this.foodItem = foodItem;
        this.amount = amount;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }
    public Amount getAmount() {
        return amount;
    }

    public Double getEnergyKcal() {
        return foodItem.getEnergyKcal() * multiplier();
    }
    public Double getEnergyKj() {
        return foodItem.getEnergyKj() * multiplier();
    }
    public Double getCarbohydrates() {
        return foodItem.getCarbohydrates() * multiplier();
    }
    public Double getProtein() {
        return foodItem.getProtein() * multiplier();
    }
    public Double getFibre() {
        return foodItem.getFibre() * multiplier();
    }
    public Double getWholeGrain() {
        return foodItem.getWholeGrain() * multiplier();
    }
    public Double getCholesterol() {
        return foodItem.getCholesterol() * multiplier();
    }
    public Double getWater() {
        return foodItem.getWater() * multiplier();
    }
    public Double getAlcohol() {
        return foodItem.getAlcohol() * multiplier();
    }
    public Double getAsh() {
        return foodItem.getAsh() * multiplier();
    }
    public Double getWaste() {
        return foodItem.getWaste() * multiplier();
    }

    private double multiplier() {
        double multiplier = amount.getUnit().getFraction() * amount.getAmount();
        if (amount.getUnit().getType() == Amount.Unit.Type.VOLUME) {
            multiplier *= foodItem.densityConstant;
        }
        return multiplier;
    }
}
