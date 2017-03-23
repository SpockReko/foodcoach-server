package models.recipe;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-03-23.
 */
public class NotLinkedRecipe extends Model {

    @Id private long id;

    @NotNull private final String title;
    @NotNull private final int portions;
    public String description;
    public int cookingDurationMinutes;

    @DbArray(length = 800) public List<String> ingredients;

    public String sourceUrl;

    public NotLinkedRecipe(String title, int portions, List<String> ingredients) {
        this.title = title;
        this.portions = portions;
        this.ingredients = ingredients;
    }
}
