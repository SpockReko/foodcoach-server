package models.recipe;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.Duration;

/**
 * Created by fredrikkindstrom on 2017-02-17.
 */
public class Recipe extends Model {

	@Id public long id;

	@Column(nullable = false) public String title;
	public String description;
	public Duration cookingDuration;
	@Column(nullable = false) public int portions;

	@Column(nullable = false) public String sourceUrl;
}
