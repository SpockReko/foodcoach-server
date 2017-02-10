package models.food;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items vitamins.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Vitamins {

	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "retinol_ug") public Float retinol;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "beta_karoten_ug") public Float betaKaroten;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_a_ug") public Float vitaminA;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_b6_ug") public Float vitaminB6;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_b12_ug") public Float vitaminB12;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "vitamin_c_mg") public Float vitaminC;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_d_ug") public Float vitaminD;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "vitamin_e_mg") public Float vitaminE;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_k_ug") public Float vitaminK;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "thiamine_mg") public Float thiamine;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "riboflavin_mg") public Float riboflavin;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "niacin_mg") public Float niacin;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "niacin_equivalents_mg") public Float niacinEquivalents;
}
