package models.recipe;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * Represents an amount of an {@link Ingredient}. Can be of any unit either of mass or volume.
 *
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Amount {

    @NotNull private final double amount;
    @Enumerated(EnumType.STRING) @NotNull private final Unit unit;

    public Amount(double amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Returns the amount. "2" liters or "3.5" kilograms for example.
     * @return The amount of the unit.
     */
    public double getAmount() {
        return amount;
    }


    /**
     * Returns the unit. 2 "liters" or 3.5 "kilograms" for example.
     * @return The unit of the amount.
     */
    public Unit getUnit() {
        return unit;
    }

    public enum Unit {
        // Standard SI-Volume
        LITER(10, Type.VOLUME, new String[] { "l", "liter" }),
        DECILITER(1, Type.VOLUME, new String[] { "dl", "deciliter", "deci liter" }),
        CENTILITER(0.1, Type.VOLUME, new String[] { "cl", "centiliter", "centi liter" }),
        MILLILITER(0.01, Type.VOLUME, new String[] { "ml", "milliliter", "milli liter" }),
        // Standard SI-Mass
        KILOGRAM(10, Type.MASS, new String[] { "kg", "kilo", "kilogram", "kilo gram" }),
        HECTOGRAM(1, Type.MASS, new String[] { "hg", "hekto", "hektogram", "hekto gram" }),
        GRAM(0.01, Type.MASS, new String[] { "g", "gram" }),
        // Swedish cookbook units
        KRYDDMATT(0.01, Type.VOLUME, new String[] { "krm", "kryddmått" }),
        TESKED(0.05, Type.VOLUME, new String[] { "tsk", "tesked" }),
        MATSKED(0.15, Type.VOLUME, new String[] { "msk", "matsked" }),
        // Single piece unit
        STYCK(0.0, Type.SINGLE, new String[] { "st", "stycken" } ),
        // Empty unit
        EMPTY(0.0, Type.EMPTY, new String[] {""} );

        private final double fraction;
        private final Type type;
        private final String[] identifiers;

        Unit(double fraction, Type type, String[] identifiers) {
            this.fraction = fraction;
            this.type = type;
            this.identifiers = identifiers;
        }

        /**
         * Returns the fraction between how much each unit is in the standard unit 100 grams
         * used for all {@link models.food.FoodItem}.
         * If the unit is liter this will return 10 for example.
         * If the type of the unit is volume the densityConstant property will be used.
         * @return The fraction between the unit and 100 grams.
         */
        public double getFraction() {
            return fraction;
        }

        /**
         * Returns the type of the unit. Either mass or volume.
         * @return The type of the unit.
         */
        public Type getType() {
            return type;
        }

        /**
         * Returns what the units can be called in a web recipe.
         * "krm" for kryddmått or "kg" for kilogram for example.
         * @return The identifiers for a unit as a string array.
         */
        public String[] getIdentifiers() {
            return identifiers;
        }

        /**
         * The type of the unit. Either mass or volume.
         */
        public enum Type { MASS, VOLUME, SINGLE, EMPTY }

        @Override
        public String toString(){
            return this.getIdentifiers()[0];
        }
    }


}
