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
	@Column(name = "retinol_ug") public Nutrient retinol;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "beta_karoten_ug") public Nutrient betaKaroten;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_a_ug") public Nutrient vitaminA;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_b6_ug") public Nutrient vitaminB6;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_b12_ug") public Nutrient vitaminB12;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "vitamin_c_mg") public Nutrient vitaminC;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_d_ug") public Nutrient vitaminD;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "vitamin_e_mg") public Nutrient vitaminE;
	@Convert(converter = FoodItem.MicrogramsConverter.class)
	@Column(name = "vitamin_k_ug") public Nutrient vitaminK;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "thiamine_mg") public Nutrient thiamine;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "riboflavin_mg") public Nutrient riboflavin;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "niacin_mg") public Nutrient niacin;
	@Convert(converter = FoodItem.MilligramsConverter.class)
	@Column(name = "niacin_equivalents_mg") public Nutrient niacinEquivalents;
}
