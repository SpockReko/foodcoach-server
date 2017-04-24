package models.food;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-04-04.
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

    public FoodGroup(String name) {
        this.name = name;
    }

    public static Finder<Long, FoodGroup> find = new Finder<>(FoodGroup.class);

    public long getId() {
        return id;
    }
}
