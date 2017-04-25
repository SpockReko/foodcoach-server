package models.food;

/**
 * Represents a nutritional value that each {@link Food} has data for.
 * Used to represent a certain nutrient throughout the application.
 *
 * @author Fredrik Kindstrom
 */
public enum Nutrient {
    ENERGY_KCAL("Energi", Type.OTHER, Unit.OTHER),
    ENERGY_KJ("Energi", Type.OTHER, Unit.OTHER),
    CARBOHYDRATES("Kolhydrater", Type.SUGAR, Unit.GRAMS),
    PROTEIN("Protein", Type.PROTEIN, Unit.GRAMS),
    FAT("Fett", Type.FAT, Unit.GRAMS),
    FIBRE("Fibrer", Type.OTHER, Unit.GRAMS),
    SALT("Salt", Type.MINERAL, Unit.GRAMS),
    ALCOHOL("Alkohol", Type.OTHER, Unit.GRAMS),

    VITAMIN_A("Vitamin A", Type.VITAMIN, Unit.MICROGRAMS),
    VITAMIN_B6("Vitamin B6", Type.VITAMIN, Unit.MILLIGRAMS),
    VITAMIN_B12("Vitamin B12", Type.VITAMIN, Unit.MICROGRAMS),
    VITAMIN_C("Vitamin C", Type.VITAMIN, Unit.MILLIGRAMS),
    VITAMIN_D("Vitamin D", Type.VITAMIN, Unit.MICROGRAMS),
    VITAMIN_E("Vitamin E", Type.VITAMIN, Unit.MILLIGRAMS),
    VITAMIN_K("Vitamin K", Type.VITAMIN, Unit.MICROGRAMS),
    THIAMINE("Tiamin", Type.VITAMIN, Unit.MILLIGRAMS),
    RIBOFLAVIN("Riboflavin", Type.VITAMIN, Unit.MILLIGRAMS),
    NIACIN("Niacin", Type.VITAMIN, Unit.MILLIGRAMS),
    NIACIN_EQ("Niacinekvivalenter", Type.VITAMIN, Unit.MILLIGRAMS),

    FOLATE("Folat", Type.MINERAL, Unit.MICROGRAMS),
    PHOSPHORUS("Fosfor", Type.MINERAL, Unit.MILLIGRAMS),
    IODINE("Jod", Type.MINERAL, Unit.MICROGRAMS),
    IRON("Järn", Type.MINERAL, Unit.MILLIGRAMS),
    CALCIUM("Kalcium", Type.MINERAL, Unit.MILLIGRAMS),
    POTASSIUM("Kalium", Type.MINERAL, Unit.MILLIGRAMS),
    MAGNESIUM("Magnesium", Type.MINERAL, Unit.MILLIGRAMS),
    SODIUM("Natrium", Type.MINERAL, Unit.MILLIGRAMS),
    SELENIUM("Selenium", Type.MINERAL, Unit.MICROGRAMS),
    ZINC("Zink", Type.MINERAL, Unit.MILLIGRAMS);

    private final String name;
    private final Type type;
    private final Unit unit;

    Nutrient(String name, Type type, Unit unit) {
        this.name = name;
        this.type = type;
        this.unit = unit;
    }

    public enum Type { SUGAR, FAT, VITAMIN, MINERAL, PROTEIN, OTHER }
    public enum Unit { GRAMS, MILLIGRAMS, MICROGRAMS, OTHER }

    public String toString() {
        return this.name;
    }
}
