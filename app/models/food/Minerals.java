package models.food;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items minerals.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Minerals {

	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "folate_ug") public Nutrient folate;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "phosphorus_mg") public Nutrient phosphorus;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "iodine_ug") public Nutrient iodine;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "iron_mg") public Nutrient iron;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "calcium_mg") public Nutrient calcium;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "potassium_mg") public Nutrient potassium;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "magnesium_mg") public Nutrient magnesium;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "sodium_mg") public Nutrient sodium;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "salt_g") public Nutrient salt;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "selenium_ug") public Nutrient selenium;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "zink_mg") public Nutrient zink;
}
