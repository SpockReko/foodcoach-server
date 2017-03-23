package models.recipe;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "NotLinkedRecipes")
public class NotLinkedRecipe extends Model {

    @Id private long id;

    @NotNull private final String title;
    @NotNull private final int portions;

    @DbArray(length = 800) public List<String> ingredients;

    public String sourceUrl;

    public NotLinkedRecipe(String title, int portions, List<String> ingredients) {
        this.title = title;
        this.portions = portions;
        this.ingredients = ingredients;
    }

    public static Finder<Long, NotLinkedRecipe> find = new Finder<>(NotLinkedRecipe.class);

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getPortions() {
        return portions;
    }
}
