package models.raw;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by fredrikkindstrom on 2017-02-06.
 */
@Embeddable
public class Sugars extends Model {

	@Column(name = "sugars_g") public Float sugars;
	@Column(name = "monosaccharides_g") public Float monosaccharides;
	@Column(name = "disaccharides_g") public Float disaccharides;
	@Column(name = "sucrose_g") public Float sucrose;
}
