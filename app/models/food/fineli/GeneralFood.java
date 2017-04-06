package models.food.fineli;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.DbArray;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-04-04.
 */
@Entity
@Table(name = "GeneralFoods")
public class GeneralFood extends Model {

    @Id private long id;

    @NotNull public String name;

    @DbArray(length = 255) public List<String> searchTags = new ArrayList<>();

    @NotNull public SpecificFood defaultSpecificFood;

    @OneToMany public List<SpecificFood> specificFoods = new ArrayList<>();

    public GeneralFood(String name) {
        this.name = name;
    }
}
