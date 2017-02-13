package models.food;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a food item that is present in Livsmedelsdatabasen by Livsmedelsverket.
 * Contains embeddable classes that represents more nutritional information
 * like sugars, fats etc. This is just to make the code less cluttered with too much methods.
 * The underlying database tables contain all these fields as columns in a single table.
 * All data is measured with 100 grams of the food item.
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodItems")
public class FoodItem extends Model {

	@Id public long id;

	@Column(nullable = false) public String name;
	public String scientificName;
	@Column(unique = true) public Long lmvFoodNumber;
	public String lmvProject;

	public Float energyKcal;
	public Float energyKj;
	@Column(name = "carbohydrates_g") public Float carbohydrates;
	@Column(name = "protein_g") public Float protein;
	@Column(name = "fibre_g") public Float fibre;
	@Column(name = "whole_grain_g") public Float wholeGrain;
	@Column(name = "cholesterol_mg") public Float cholesterol;
	@Column(name = "water_g") public Float water;
	@Column(name = "alcohol_g") public Float alcohol;
	@Column(name = "ash_g") public Float ash;
	@Column(name = "waste_percent") public Float waste;

	@Embedded public Sugars sugars;
	@Embedded public Fats fats;
	@Embedded public Vitamins vitamins;
	@Embedded public Minerals minerals;

	@ManyToMany @JsonManagedReference public List<FoodGroup> groups;
	@ManyToMany @JsonManagedReference public List<Part> parts;

	public static Finder<Long, FoodItem> find = new Finder<>(FoodItem.class);
}
