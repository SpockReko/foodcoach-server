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
        return multiplier(foodItem.getEnergyKcal());
    }
    public Double getEnergyKj() {
        return multiplier(foodItem.getEnergyKj());
    }
    public Double getCarbohydrates() {
        return multiplier(foodItem.getCarbohydrates());
    }
    public Double getProtein() {
        return multiplier(foodItem.getProtein());
    }
    public Double getFat() {
        return multiplier(foodItem.getFats().getFat());
    }
    public Double getFibre() {
        return multiplier(foodItem.getFibre());
    }
    public Double getWholeGrain() {
        return multiplier(foodItem.getWholeGrain());
    }
    public Double getCholesterol() {
        return multiplier(foodItem.getCholesterol());
    }
    public Double getWater() {
        return multiplier(foodItem.getWater());
    }
    public Double getAlcohol() {
        return multiplier(foodItem.getAlcohol());
    }
    public Double getAsh() {
        return multiplier(foodItem.getAsh());
    }

    /*
    Extra nutrition data
     */
    public Double getSugars() {
        return multiplier(foodItem.getSugars().getSugars());
    }
    public Double getVitaminA() {
        return multiplier(foodItem.getVitamins().getVitaminA());
    }
    public Double getVitaminB6() {
        return multiplier(foodItem.getVitamins().getVitaminB6());
    }
    public Double getVitaminB12() {
        return multiplier(foodItem.getVitamins().getVitaminB12());
    }
    public Double getVitaminC() {
        return multiplier(foodItem.getVitamins().getVitaminC());
    }
    public Double getVitaminD() {
        return multiplier(foodItem.getVitamins().getVitaminD());
    }
    public Double getVitaminE() {
        return multiplier(foodItem.getVitamins().getVitaminE());
    }
    public Double getThiamine() {
        return multiplier(foodItem.getVitamins().getThiamine());
    }
    public Double getRiboflavin() {
        return multiplier(foodItem.getVitamins().getRiboflavin());
    }
    public Double getNiacin() {
        return multiplier(foodItem.getVitamins().getNiacin());
    }
    public Double getNiacinEquivalents() {
        return multiplier(foodItem.getVitamins().getNiacinEquivalents());
    }
    public Double getFolate() {
        return multiplier(foodItem.getMinerals().getFolate());
    }
    public Double getPhosphorus() {
        return multiplier(foodItem.getMinerals().getPotassium());
    }
    
    // TODO ändra getFolate() till getIodine() och lösa kompileringsfelet
    public Double getIodine() {
        return multiplier(foodItem.getMinerals().getFolate());
    }
    public Double getIron() {
        return multiplier(foodItem.getMinerals().getIron());
    }
    public Double getCalcium() {
        return multiplier(foodItem.getMinerals().getCalcium());
    }
    public Double getPotassium() {
        return multiplier(foodItem.getMinerals().getPotassium());
    }
    public Double getMagnesium() {
        return multiplier(foodItem.getMinerals().getMagnesium());
    }
    public Double getSalt() {
        return multiplier(foodItem.getMinerals().getSalt());
    }
    public Double getSelenium() {
        return multiplier(foodItem.getMinerals().getSelenium());
    }
    public Double getZink() {
        return multiplier(foodItem.getMinerals().getZink());
    }

    private Double multiplier(Float value) {
        if (value == null) {
            return null;
        }
        double multiplier = amount.getUnit().getFraction() * amount.getAmount();
        if (amount.getUnit().getType() == Amount.Unit.Type.VOLUME) {
            // TODO fix some info message here for when there is no densityConstant
            if (foodItem.densityConstant != null) {
                multiplier *= foodItem.densityConstant;
            }
        }
        return value * multiplier;
    }
}
