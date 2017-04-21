package models.recipe;

import com.avaje.ebean.Model;
import models.food.Food;
import models.food.Nutrient;

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
    public Double getEnergyKcal() {
        return multiplier(food.getNutrient(Nutrient.KCAL));
    }
    public Double getCO2() {
        return multiplier(food.getCO2());
    }

    public Double getEnergyKj() {
        return multiplier(food.getNutrient(Nutrient.KJ));
    }
    public Double getCarbohydrates() {
        return multiplier(food.getNutrient(Nutrient.CARBOHYDRATES));
    }
    public Double getProtein() {
        return multiplier(food.getNutrient(Nutrient.PROTEIN));
    }
    public Double getFat() {
        return multiplier(food.getNutrient(Nutrient.FAT));
    }
    public Double getFibre() {
        return multiplier(food.getNutrient(Nutrient.FIBRE));
    }
    public Double getAlcohol() {
        return multiplier(food.getNutrient(Nutrient.ALCOHOL));
    }

    /*
    Extra nutrition data
     */
    public Double getVitaminA() {
        return multiplier(food.getNutrient(Nutrient.VITAMIN_A));
    }
    public Double getVitaminB6() {
        return multiplier(food.getNutrient(Nutrient.VITAMIN_B6));
    }
    public Double getVitaminB12() {
        return multiplier(food.getNutrient(Nutrient.VITAMIN_B12));
    }
    public Double getVitaminC() {
        return multiplier(food.getNutrient(Nutrient.VITAMIN_C));
    }
    public Double getVitaminD() {
        return multiplier(food.getNutrient(Nutrient.VITAMIN_D));
    }
    public Double getVitaminE() {
        return multiplier(food.getNutrient(Nutrient.VITAMIN_E));
    }
    public Double getThiamine() {
        return multiplier(food.getNutrient(Nutrient.THIAMINE));
    }
    public Double getRiboflavin() {
        return multiplier(food.getNutrient(Nutrient.RIBOFLAVIN));
    }
    public Double getNiacin() {
        return multiplier(food.getNutrient(Nutrient.NIACIN));
    }
    public Double getNiacinEquivalents() {
        return multiplier(food.getNutrient(Nutrient.NIACIN_EQ));
    }
    public Double getFolate() {
        return multiplier(food.getNutrient(Nutrient.FOLATE));
    }
    public Double getPhosphorus() {
        return multiplier(food.getNutrient(Nutrient.PHOSPHORUS));
    }
    public Double getIodine() {
        return multiplier(food.getNutrient(Nutrient.IODINE));
    }
    public Double getIron() {
        return multiplier(food.getNutrient(Nutrient.IRON));
    }
    public Double getCalcium() {
        return multiplier(food.getNutrient(Nutrient.CALCIUM));
    }
    public Double getPotassium() {
        return multiplier(food.getNutrient(Nutrient.POTASSIUM));
    }
    public Double getMagnesium() {
        return multiplier(food.getNutrient(Nutrient.MAGNESIUM));
    }
    public Double getSalt() {
        return multiplier(food.getNutrient(Nutrient.SALT));
    }
    public Double getSelenium() {
        return multiplier(food.getNutrient(Nutrient.SELENIUM));
    }
    public Double getZink() {
        return multiplier(food.getNutrient(Nutrient.ZINC));
    }

    private Double multiplier(Double value) {
        if (value == null) {
            // TODO return 0.0 for now, would be nice to know if there is no data present instead
            return 0.0;
        }
        double multiplier = amount.getUnit().getFraction() * amount.getAmount();
        if (amount.getUnit().getType() == Amount.Unit.Type.VOLUME) {
            // TODO fix some info message here for when there is no densityConstant
            if (food.densityConstant != null) {
                multiplier *= food.densityConstant;
            }
        }
        return value * multiplier;
    }
}
