package models.recipe;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * Represents an quantity of an {@link Ingredient}. Can be of any unit either of mass or volume.
 *
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Amount {

    @NotNull private final double quantity;
    @Enumerated(EnumType.STRING) @NotNull private final Unit unit;

    public Amount(double quantity, Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * Returns the quantity. "2" liters or "3.5" kilograms for example.
     * @return The quantity of the amount.
     */
    public double getQuantity() {
        return quantity;
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
        TEKOPP(0.4, Type.VOLUME, new String[] { "tkp", "tekopp" }),
        KAFFEKOPP(0.67, Type.VOLUME, new String[] { "kkp", "kaffekopp" }),
        // Packages and other special units
        // TODO refactor this to a separate db table instead
        CAN(1.0, Type.SINGLE, new String[] { "burk", "burkar" }),
        PACKAGE(1.0, Type.SINGLE, new String[] { "paket" }),
        // Single piece unit
        STYCK(1.0, Type.SINGLE, new String[] { "st", "stycken" }),
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
         * Returns the fraction between how much each unit is in
         * the standard 100 grams used for all {@link models.food.Food}.
         * If the unit is kilogram this will return 10 for example.
         * If the type of the unit is volume the {@link models.food.Food#densityConstant}
         * property will be used.
         * @return The fraction between the unit and 100 grams.
         */
        public double getFraction() {
            return fraction;
        }

        /**
         * Returns the {@link Type} of the unit.
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
         * The type of the unit.
         * Either {@link Type#MASS}, {@link Type#VOLUME} or {@link Type#SINGLE}.
         */
        public enum Type { MASS, VOLUME, SINGLE, EMPTY }

        @Override
        public String toString(){
            return this.getIdentifiers()[0];
        }
    }


}
