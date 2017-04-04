package models.food.fineli;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Represents a special diet that a food can belong to.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "SpecialDiets")
public class SpecialDiet extends Model {

    @Id private long id;

    @NotNull @Enumerated(EnumType.STRING) private final Diet diet;

    public SpecialDiet(Diet diet) {

        this.diet = diet;
    }

    public long getId() {
        return id;
    }
    public Diet getDiet() {
        return diet;
    }

    public enum Diet {
        CHOLESTEROL_FREE, EGG_FREE, FAT_FREE, GLUTEN_FREE, HIGH_FIBRE, STRONGLY_SALTED,
        LACTO_OVO_VEG, LACTO_VEG, LASTOSE_FREE, LOW_CHOLESTEROL, LOW_FAT, LOW_LACTOSE,
        LOW_PROTEIN, REDUCED_SALT, MILK_FREE, NATURALLY_GLUTEN_FREE, SALT_FREE, SOY_FREE,
        UNSWEETENED, VEGAN, ADDED_VITAMINS
    }
}
