package models.food;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * This class represents a food item that is present in Livsmedelsdatabasen by Livsmedelsverket.
 * This contains embeddable classes that represents more nutritional
 * information (like sugars, fats etc). This is just to make the code less cluttered with
 * too much methods. The underlying database tables contain all
 * these fields as columns in a single table.
 * All data is measured with 100 grams of the food item.
 * @author Fredrik Kindstrom
 */
@Entity
@Table(name = "FoodItems")
public class FoodItem extends Model {

	@Id
	@Column(name = "_id") public long id;

	@Column(name = "name", nullable = false) public String name;
	@Column(name = "scientific_name") public String scientificName;
	@Column(name = "lmv_food_number", unique = true) public Long lmvFoodNumber;
	@Column(name = "lmv_project") public String lmvProject;

	@Column(name = "energy_kcal") public Float energyKcal;
	@Column(name = "energy_kj") public Float energyKj;
	@Column(name = "carbohydrates_g") public Float carbohydrates;
	@Column(name = "protein_g") public Float protein;
	@Column(name = "fibre_g") public Float fibre;
	@Column(name = "whole_grain_g") public Float wholeGrain;
	@Column(name = "cholesterol_mg") public Float cholesterol;
	@Column(name = "water_g") public Float water;
	@Column(name = "alcohol_g") public Float alcohol;
	@Column(name = "ash_g") public Float ash;
	@Column(name = "waste_percent") public Float waste;

	@Column(name = "sugars") @Embedded public Sugars sugars;
	@Column(name = "fats") @Embedded public Fats fats;
	@Column(name = "vitamins") @Embedded public Vitamins vitamins;
	@Column(name = "minerals") @Embedded public Minerals minerals;

	public static Finder<Long, FoodItem> find = new Finder<>(FoodItem.class);
}
