package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * Created by fredrikkindstrom on 2017-02-16.
 */
@Entity
@Table(name = "FoodSources")
public class FoodSource extends Model {

	@Id public long id;

	@Column(nullable = false) public String name;
	@Pattern(regexp = "[A-Z]\\d{4}") public String langualCode;

	@ManyToOne @JsonBackReference public FoodSource parents;
	@ManyToMany(mappedBy = "sources") @JsonBackReference public Set<FoodItem> foodItems;

	public FoodSource(String name, String langualCode) {
		this.name = name;
		this.langualCode = langualCode;
	}

	public static Finder<Long, FoodSource> find = new Finder<>(FoodSource.class);
}
