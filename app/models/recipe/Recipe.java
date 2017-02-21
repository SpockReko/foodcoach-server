package models.recipe;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Set;

/**
 * Created by fredrikkindstrom on 2017-02-17.
 */
public class Recipe extends Model {

    @Id public long id;

    @Column(nullable = false) public String title;
    public String description;
    public Duration cookingDuration;
    @Column(nullable = false) public int portions;

    public Set<Ingredient> ingredients;

    @Column(nullable = false) public String sourceUrl;

    public Recipe(long id, String title, Set<Ingredient> ingredients, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.sourceUrl = sourceUrl;
    }
}
