package models.food;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * Represents a food group that every {@link FoodItem} can be associated with.
 * Usually contains a reference to a LanguaL code as decided by EuroFIR Food Classification System
 * (<a href="http://www.eurofir.org/">http://www.eurofir.org/</a>).
 */
@Entity
@Table(name = "FoodGroups")
public class FoodGroup extends Model {

	@Id public long id;

	@Column(nullable = false) public String name;
	@Pattern(regexp = "[A-Z]\\d{4}") public String langualCode;

	@OneToOne public FoodGroup parent;
}
