package models.food;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

/**
 * Represents a general LanguaL term that every {@link FoodItem} can be associated with.
 * Usually contains a reference to a LanguaL code as decided by the LanguaL thesarus.
 * The code can also be blanc and that happens when Livsmedelsverket has put in their
 * own random data with no reference to the an actual Langual term.
 * (<a href="http://www.langual.org/langual_Thesaurus.asp">http://www.langual.org/langual_Thesaurus.asp</a>).
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "LangualTerms")
public class LangualTerm extends Model {

    @Id private long id;

    @Column(unique = true) private final String code;
    @NotNull private final String name;

    @Enumerated(EnumType.STRING) private final Type type;

    public LangualTerm(String code, String name, Type type) {
        if (code == null) {
            this.code = null;
        } else if (Pattern.matches("[A-Z]\\d{4}", code)) {
            this.code = code;
        } else {
            throw new IllegalArgumentException("LanguaL code must be on the form: [A-Z]\\d{4} or null");
        }

        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }

    public enum Type {
        PART_OF_PLANT_OR_ANIMAL, PHYSICAL_FORM, HEAT_TREATMENT, COOKING_METHOD, INDUSTRIAL_PROCESS,
        PRESERVATION_METHOD, PACKING_MEDIUM, PACKING_TYPE, PACKING_MATERIAL, LABEL_CLAIM,
        GEOGRAPHIC_SOURCE, DISTINCTIVE_FEATURES
    }
}
