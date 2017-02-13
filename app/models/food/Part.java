package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Represents a food group that every {@link FoodItem} can be associated with.
 * Usually contains a reference to a LanguaL code as decided by EuroFIR Food Classification System
 * (<a href="http://www.eurofir.org/">http://www.eurofir.org/</a>).
 */
@Entity
@Table(name = "Parts")
public class Part extends Model {

	@Id public long id;

	@Column(nullable = false) public String name;
	@Pattern(regexp = "[A-Z]\\d{4}") public String langualCode;

	@ManyToMany(mappedBy = "parts") @JsonBackReference public List<FoodItem> foodItems;

	public static Finder<Long, Part> find = new Finder<>(Part.class);
}
