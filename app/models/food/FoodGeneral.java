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
@Table(name = "FoodGenerals")
public class FoodGeneral extends Model {

    @Id private long id;

    @NotNull public String name;

    @DbArray(length = 255) public List<String> searchTags = new ArrayList<>();

    @OneToOne @JsonManagedReference public Food defaultFood;

    @OneToMany(mappedBy = "general") public List<Food> foods = new ArrayList<>();

    public FoodGeneral(String name) {
        this.name = name;
    }

    public static Finder<Long, FoodGeneral> find = new Finder<>(FoodGeneral.class);
}
