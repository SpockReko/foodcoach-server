package models.raw;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items vitamins.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Vitamins {

	@Column(name = "retinol_ug") public Float retinol;
	@Column(name = "beta_karoten_ug") public Float betaKaroten;
	@Column(name = "vitamin_a_ug") public Float vitaminA;
	@Column(name = "vitamin_b6_ug") public Float vitaminB6;
	@Column(name = "vitamin_b12_ug") public Float vitaminB12;
	@Column(name = "vitamin_c_mg") public Float vitaminC;
	@Column(name = "vitamin_d_ug") public Float vitaminD;
	@Column(name = "vitamin_e_mg") public Float vitaminE;
	@Column(name = "vitamin_k_ug") public Float vitaminK;
	@Column(name = "thiamine_mg") public Float thiamine;
	@Column(name = "riboflavin_mg") public Float riboflavin;
	@Column(name = "niacin_mg") public Float niacin;
	@Column(name = "niacin_equivalents_mg") public Float niacinEquivalents;
}
