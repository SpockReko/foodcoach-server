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
@Table(name = "FoodGenerals")
public class FoodGeneral extends Model {

    @Id private long id;

    @NotNull public String name;

    @DbArray(length = 255) public List<String> searchTags = new ArrayList<>();

    @NotNull public Food defaultFood;

    @OneToMany public List<Food> foods = new ArrayList<>();

    public FoodGeneral(String name) {
        this.name = name;
    }
}