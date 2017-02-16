package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-16.
 */
@Entity
@Table(name = "FoodSources")
public class FoodSource {

	@Id public long id;

	@Column(nullable = false) public String name;
	@Pattern(regexp = "[A-Z]\\d{4}") public String langualCode;

	@ManyToMany @JsonBackReference public FoodGroup parents;
	@ManyToMany(mappedBy = "sources") @JsonBackReference public List<FoodItem> foodItems;

	public FoodSource(String name, String langualCode) {
		this.name = name;
		this.langualCode = langualCode;
	}

	public static Model.Finder<Long, FoodSource> find = new Model.Finder<>(FoodSource.class);
}
