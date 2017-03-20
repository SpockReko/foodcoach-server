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
    public Double getFat() {
        return foodItem.getFats().getFat() * multiplier();
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

    /*
    Extra nutrition data
     */
    public Double getSugars() {
        return foodItem.getSugars().getSugars() * multiplier();
    }
    public Double getVitaminA() {
        return foodItem.getVitamins().getVitaminA() * multiplier();
    }
    public Double getVitaminB6() {
        return foodItem.getVitamins().getVitaminB6() * multiplier();
    }
    public Double getVitaminB12() {
        return foodItem.getVitamins().getVitaminB12() * multiplier();
    }
    public Double getVitaminC() {
        return foodItem.getVitamins().getVitaminC() * multiplier();
    }
    public Double getVitaminD() {
        return foodItem.getVitamins().getVitaminD() * multiplier();
    }
    public Double getVitaminE() {
        return foodItem.getVitamins().getVitaminE() * multiplier();
    }
    public Double getThiamine() {
        return foodItem.getVitamins().getThiamine() * multiplier();
    }
    public Double getRiboflavin() {
        return foodItem.getVitamins().getRiboflavin() * multiplier();
    }
    public Double getNiacin() {
        return foodItem.getVitamins().getNiacin() * multiplier();
    }
    public Double getNiacinEquivalents() {
        return foodItem.getVitamins().getNiacinEquivalents() * multiplier();
    }
    public Double getFolate() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getPhosphorus() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getIodine() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getIron() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getCalcium() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getPotassium() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getMagnesium() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getSalt() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getSelenium() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }
    public Double getZink() {
        return foodItem.getMinerals().getFolate() * multiplier();
    }

    private double multiplier() {
        double multiplier = amount.getUnit().getFraction() * amount.getAmount();
        if (amount.getUnit().getType() == Amount.Unit.Type.VOLUME) {
            multiplier *= foodItem.densityConstant;
        }
        return multiplier;
    }
}
