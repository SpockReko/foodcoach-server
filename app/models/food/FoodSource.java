package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Represents a food source that every {@link FoodItem} can be associated with.
 * Usually contains a reference to a LanguaL code as decided by the LanguaL thesarus.
 * (<a href="http://www.langual.org/langual_Thesaurus.asp">http://www.langual.org/langual_Thesaurus.asp</a>).
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodSources")
public class FoodSource extends Model {

    @Id public long id;

    @Column(nullable = false) public String name;
    @Column(unique = true) private String langualCode;

    @ManyToOne @JsonBackReference public FoodSource parents;
    @ManyToMany(mappedBy = "sources") @JsonManagedReference public Set<FoodItem> foodItems;

    public FoodSource(String name, String langualCode) {
        this.name = name;
        setLangualCode(langualCode);
    }

    public void setLangualCode(String langualCode) {
        if (java.util.regex.Pattern.matches("[A-Z]\\d{4}", langualCode)) {
            this.langualCode = langualCode;
        } else {
            throw new IllegalArgumentException("LanguaL code must be on the form: [A-Z]\\d{4}");
        }
    }

    public static Finder<Long, FoodSource> find = new Finder<>(FoodSource.class);
}
