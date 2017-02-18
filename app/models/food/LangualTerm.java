package models.food;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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

    @Id public long id;

    @Column(unique = true) @Pattern(regexp = "[A-Z]\\d{4}") public String code;
    @Column(nullable = false) public String name;

    @Enumerated(EnumType.STRING) public Type type;

    public LangualTerm(String code, String name, Type type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public enum Type {
        PART_OF_PLANT_OR_ANIMAL, PHYSICAL_FORM, HEAT_TREATMENT, COOKING_METHOD, INDUSTRIAL_PROCESS,
        PRESERVATION_METHOD, PACKING_MEDIUM, PACKING_TYPE, PACKING_MATERIAL, LABEL_CLAIM,
        GEOGRAPHIC_SOURCE, DISTINCTIVE_FEATURES
    }
}
