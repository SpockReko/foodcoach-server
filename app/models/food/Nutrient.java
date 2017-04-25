package models.food;

/**
 * Represents a nutritional value that each {@link Food} has data for.
 * Used to represent a certain nutrient throughout the application.
 *
 * @author Fredrik Kindstrom
 */
public enum Nutrient {
    KCAL(Type.OTHER, Unit.OTHER),
    KJ(Type.OTHER, Unit.OTHER),
    CARBOHYDRATES(Type.SUGAR, Unit.GRAMS),
    PROTEIN(Type.PROTEIN, Unit.GRAMS),
    FAT(Type.FAT, Unit.GRAMS),
    FIBRE(Type.OTHER, Unit.GRAMS),
    SALT(Type.MINERAL, Unit.GRAMS),
    ALCOHOL(Type.OTHER, Unit.GRAMS),

    VITAMIN_A(Type.VITAMIN, Unit.MICROGRAMS),
    VITAMIN_B6(Type.VITAMIN, Unit.MILLIGRAMS),
    VITAMIN_B12(Type.VITAMIN, Unit.MICROGRAMS),
    VITAMIN_C(Type.VITAMIN, Unit.MILLIGRAMS),
    VITAMIN_D(Type.VITAMIN, Unit.MICROGRAMS),
    VITAMIN_E(Type.VITAMIN, Unit.MILLIGRAMS),
    VITAMIN_K(Type.VITAMIN, Unit.MICROGRAMS),
    THIAMINE(Type.VITAMIN, Unit.MILLIGRAMS),
    RIBOFLAVIN(Type.VITAMIN, Unit.MILLIGRAMS),
    NIACIN(Type.VITAMIN, Unit.MILLIGRAMS),
    NIACIN_EQ(Type.VITAMIN, Unit.MILLIGRAMS),

    FOLATE(Type.MINERAL, Unit.MICROGRAMS),
    PHOSPHORUS(Type.MINERAL, Unit.MILLIGRAMS),
    IODINE(Type.MINERAL, Unit.MICROGRAMS),
    IRON(Type.MINERAL, Unit.MILLIGRAMS),
    CALCIUM(Type.MINERAL, Unit.MILLIGRAMS),
    POTASSIUM(Type.MINERAL, Unit.MILLIGRAMS),
    MAGNESIUM(Type.MINERAL, Unit.MILLIGRAMS),
    SODIUM(Type.MINERAL, Unit.MILLIGRAMS),
    SELENIUM(Type.MINERAL, Unit.MICROGRAMS),
    ZINC(Type.MINERAL, Unit.MILLIGRAMS);

    private final Type type;
    private final Unit unit;

    Nutrient(Type type, Unit unit) {
        this.type = type;
        this.unit = unit;
    }

    public enum Type { SUGAR, FAT, VITAMIN, MINERAL, PROTEIN, OTHER }
    public enum Unit { GRAMS, MILLIGRAMS, MICROGRAMS, OTHER }
}
