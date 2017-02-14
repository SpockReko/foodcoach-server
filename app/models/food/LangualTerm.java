package models.food;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by fredrikkindstrom on 2017-02-14.
 */
@Entity
@Table(name = "LangualTerms")
public class LangualTerm extends Model {

	@Id @Pattern(regexp = "[A-Z]\\d{4}") public String code;
	@Column(nullable = false) public String name;

	@OneToMany public List<FoodItem> foodItems;

	public LangualTerm(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
