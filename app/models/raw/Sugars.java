package models.raw;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items sugars.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Sugars extends Model {

	@Column(name = "sugars_g") public Float sugars;
	@Column(name = "monosaccharides_g") public Float monosaccharides;
	@Column(name = "disaccharides_g") public Float disaccharides;
	@Column(name = "sucrose_g") public Float sucrose;
}
