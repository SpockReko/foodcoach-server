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
    @Column(unique = true) @Pattern(regexp = "[A-Z]\\d{4}") public String langualCode;

    @ManyToOne @JsonBackReference public FoodSource parents;
    @ManyToMany(mappedBy = "sources") @JsonManagedReference public Set<FoodItem> foodItems;

    public FoodSource(String name, String langualCode) {
        this.name = name;
        this.langualCode = langualCode;
    }

    public static Finder<Long, FoodSource> find = new Finder<>(FoodSource.class);
}
