package models.raw;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items minerals.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Minerals {

	@Column(name = "folate_ug") public Float folate;
	@Column(name = "phosphorus_mg") public Float phosphorus;
	@Column(name = "iodine_ug") public Float iodine;
	@Column(name = "iron_mg") public Float iron;
	@Column(name = "calcium_mg") public Float calcium;
	@Column(name = "potassium_mg") public Float potassium;
	@Column(name = "magnesium_mg") public Float magnesium;
	@Column(name = "sodium_mg") public Float sodium;
	@Column(name = "salt_g") public Float salt;
	@Column(name = "selenium_ug") public Float selenium;
	@Column(name = "zink_mg") public Float zink;
}
