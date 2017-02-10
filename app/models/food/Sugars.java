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
	@Column(name = "sugars_g") public Float sugars;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "monosaccharides_g") public Float monosaccharides;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "disaccharides_g") public Float disaccharides;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "sucrose_g") public Float sucrose;
}
