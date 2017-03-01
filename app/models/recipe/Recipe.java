package models.recipe;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

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
    public String description;
    public int cookingDurationMinutes;
    @NotNull private final int portions;

    @ManyToMany(cascade = CascadeType.ALL) public List<Ingredient> ingredients;

    public String sourceUrl;

    public Recipe(String title, String description, int portions, List<Ingredient> ingredients) {
        this.title = title;
        this.description = description;
        this.portions = portions;
        this.ingredients = ingredients;
    }

    public static Finder<Long, Recipe> find = new Finder<>(Recipe.class);

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getCookingDurationMinutes() {
        return cookingDurationMinutes;
    }
    public int getPortions() {
        return portions;
    }
    public String getSourceUrl() {
        return sourceUrl;
    }

    public Double getEnergyKcal() {
        return ingredients.stream().mapToDouble(Ingredient::getEnergyKcal).sum();
    }
    public Double getEnergyKj() {
        return ingredients.stream().mapToDouble(Ingredient::getEnergyKj).sum();
    }
    public Double getCarbohydrates() {
        return ingredients.stream().mapToDouble(Ingredient::getCarbohydrates).sum();
    }
    public Double getProtein() {
        return ingredients.stream().mapToDouble(Ingredient::getProtein).sum();
    }
    public Double getFibre() {
        return ingredients.stream().mapToDouble(Ingredient::getFibre).sum();
    }
    public Double getWholeGrain() {
        return ingredients.stream().mapToDouble(Ingredient::getWholeGrain).sum();
    }
    public Double getCholesterol() {
        return ingredients.stream().mapToDouble(Ingredient::getCholesterol).sum();
    }
    public Double getWater() {
        return ingredients.stream().mapToDouble(Ingredient::getWater).sum();
    }
    public Double getAlcohol() {
        return ingredients.stream().mapToDouble(Ingredient::getAlcohol).sum();
    }
    public Double getAsh() {
        return ingredients.stream().mapToDouble(Ingredient::getAsh).sum();
    }
    public Double getWaste() {
        return ingredients.stream().mapToDouble(Ingredient::getWaste).sum();
    }
}
