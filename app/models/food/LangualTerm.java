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

	@Enumerated(EnumType.STRING) public Type type;

	public LangualTerm(String code, String name, Type type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public enum Type {
		PART_OF_PLANT_OR_ANIMAL, PHYSICAL_FORM, HEAT_TREATMENT, COOKING_METHOD,
		INDUSTRIAL_PROCESS, PRESERVATION_METHOD, PACKING_MEDIUM, PACKING_TYPE,
		PACKING_MATERIAL, LABEL_CLAIM, GEOGRAPHIC_SOURCE, DISTINCTIVE_FEATURES
	}
}
