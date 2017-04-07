package models.food.fineli;

import javax.persistence.*;

/**
 * Represents a special diet that a food can belong to.
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "Diets")
public class Diet {

    @Id private long id;
    @Enumerated(EnumType.STRING) public Type type;

    public Diet(Type type) {
        this.type = type;
    }

    public enum Type {
        CHOLESTEROL_FREE, EGG_FREE, FAT_FREE, GLUTEN_FREE, HIGH_FIBRE, STRONGLY_SALTED,
        LACTO_OVO_VEG, LACTO_VEG, LACTOSE_FREE, LOW_CHOLESTEROL, LOW_FAT, LOW_LACTOSE,
        LOW_PROTEIN, REDUCED_SALT, MILK_FREE, NATURALLY_GLUTEN_FREE, SALT_FREE, SOY_FREE,
        UNSWEETENED, VEGAN, ADDED_VITAMINS
    }
}
