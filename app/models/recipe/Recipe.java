package models.recipe;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import java.util.function.Function;

@Entity
@Table(name = "Recipes")
public class Recipe extends Model {

    @Id private long id;

    @NotNull private final String title;
    public String description;
    public Duration cookingDuration;
    @NotNull private final int portions;

    public Set<Ingredient> ingredients;

    public String sourceUrl;

    public Recipe(String title, String description, int portions, Set<Ingredient> ingredients) {
        this.title = title;
        this.description = description;
        this.portions = portions;
        this.ingredients = ingredients;
    }

    public static Finder<Long, Recipe> find = new Finder<>(Recipe.class);

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
