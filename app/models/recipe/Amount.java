package models.recipe;

/**
 * Created by fredrikkindstrom on 2017-02-21.
 */
public class Amount {

    private final int amount;
    private final Unit unit;

    public Amount(int amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }
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
        MATSKED(0.15, Type.VOLUME, new String[] { "msk", "matsked" });

        private final double fraction;
        private final Type type;
        private final String[] identifiers;

        Unit(double fraction, Type type, String[] identifiers) {
            this.fraction = fraction;
            this.type = type;
            this.identifiers = identifiers;
        }

        public double getFraction() {
            return fraction;
        }

        public Type getType() {
            return type;
        }

        public String[] getIdentifiers() {
            return identifiers;
        }

        public enum Type {MASS, VOLUME}
    }
}
