package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Represents a food group that every {@link FoodItem} can be associated with.
 * Usually contains a reference to a LanguaL code as decided by EuroFIR Food Classification System
 * (<a href="http://www.eurofir.org/">http://www.eurofir.org/</a>).
 *
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodGroups")
public class FoodGroup extends Model {

	@Id public long id;

	@Column(nullable = false) public String name;
	@Column(unique = true) @Pattern(regexp = "[A-Z]\\d{4}") public String langualCode;

	@ManyToOne @JsonBackReference public FoodGroup parent;
	@ManyToMany(mappedBy = "groups") @JsonManagedReference public Set<FoodItem> foodItems;

	public FoodGroup(String name, String langualCode) {
		this.name = name;
		this.langualCode = langualCode;
	}

	public static Finder<Long, FoodGroup> find = new Finder<>(FoodGroup.class);
}
