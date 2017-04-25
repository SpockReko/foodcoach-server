package models.food;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Respresents a group which {@link Food}s belong to. Always has a default {@link Food}.
 * Search tags is used when parsing an ingredient string.
 * This group exist to easier clump alot of similar {@link Food}s
 * together and handle parsing in a more central way.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodGroups")
public class FoodGroup extends Model {

    @Id private long id;

    @NotNull public String name;
    @JsonManagedReference
    @OneToOne public Food defaultFood;
    @DbArray(length = 255) public List<String> searchTags = new ArrayList<>();
    @OneToMany(mappedBy = "group") public List<Food> foods = new ArrayList<>();

    /**
     * Creates a food group. Be sure to set other properties as well.
     * @param name The name of the group.
     */
    public FoodGroup(String name) {
        this.name = name;
    }

    public static Finder<Long, FoodGroup> find = new Finder<>(FoodGroup.class);

    /**
     * The unique id of the food group in the database.
     * @return The id as a long.
     */
    public long getId() {
        return id;
    }
}
