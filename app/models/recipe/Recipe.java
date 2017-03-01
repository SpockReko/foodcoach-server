package models.recipe;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;

@Entity
@Table(name = "Recipes")
public class Recipe extends Model {

    @Id private long id;

    @NotNull private final String title;
    public String description;
    public Duration cookingDuration;
    @NotNull private final int portions;

    public Set<Ingredient> ingredients;

    @Column private String sourceUrl;

    public Recipe(String title, String description, int portions, Set<Ingredient> ingredients) {
        this.title = title;
        this.description = description;
        this.portions = portions;
        this.ingredients = ingredients;
    }

    public static Finder<Long, Recipe> find = new Finder<>(Recipe.class);
}
