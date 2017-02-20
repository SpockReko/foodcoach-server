package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Represents a food group that every {@link FoodItem} can be associated with.
 * Usually contains a reference to a LanguaL code as decided by EuroFIR Food Classification System
 * (<a href="http://www.eurofir.org/">http://www.eurofir.org/</a>).
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodGroups")
public class FoodGroup extends Model {

    @Id public long id;

    @Column(nullable = false) public String name;
    @Column(unique = true) private String langualCode;

    @ManyToOne @JsonBackReference public FoodGroup parent;
    @ManyToMany(mappedBy = "groups") @JsonManagedReference public Set<FoodItem> foodItems;

    public FoodGroup(String name, String langualCode) {
        this.name = name;
        setLangualCode(langualCode);
    }

    public void setLangualCode(String langualCode) {
        if (Pattern.matches("[A-Z]\\d{4}", langualCode)) {
            this.langualCode = langualCode;
        } else {
            throw new IllegalArgumentException("LanguaL code must be on the form: [A-Z]\\d{4}");
        }
    }

    public static Finder<Long, FoodGroup> find = new Finder<>(FoodGroup.class);
}
