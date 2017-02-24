package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;
import java.util.regex.Pattern;

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

    @Id private long id;

    @Column(nullable = false) private final String name;
    @Column(unique = true) private final String langualCode;

    @ManyToOne(cascade = CascadeType.PERSIST) @JsonBackReference public FoodSource parent;
    @ManyToMany(mappedBy = "sources", cascade = CascadeType.ALL) @JsonBackReference
    public Set<FoodItem> foodItems;

    public FoodSource(String name, String langualCode) {
        this.name = name;

        if (langualCode == null) {
            this.langualCode = null;
        } else if (Pattern.matches("[A-Z]\\d{4}", langualCode)) {
            this.langualCode = langualCode;
        } else {
            throw new IllegalArgumentException("LanguaL code must be on the form: [A-Z]\\d{4}");
        }
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLangualCode() {
        return langualCode;
    }

    public static Finder<Long, FoodSource> find = new Finder<>(FoodSource.class);
}
