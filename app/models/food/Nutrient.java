package models.food;

/**
 * This class is a Java representation of the nutrient data on each {@link FoodItem}.
 * In the database all this data is stored as a single float value and in the column name
 * there is information which unit the value is recorded in. This class hold both the float
 * data and the unit. An AttributeConverter is used to convert between these
 * representations when persisting to the database.
 * @author Fredrik Kindstrom
 */
public class Nutrient {

	private Float value;
	private Unit unit;

	public Nutrient(Float value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}

	public Float getValue() {
		return value;
	}

	public Unit getUnit() {
		return unit;
	}

	public enum Unit { GRAMS, MILLIGRAMS, MICROGRAMS, PERCENT }
}
