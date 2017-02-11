package models.food;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items sugars.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Sugars {

	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "sugars_g") public Nutrient sugars;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "monosaccharides_g") public Nutrient monosaccharides;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "disaccharides_g") public Nutrient disaccharides;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "sucrose_g") public Nutrient sucrose;
}
