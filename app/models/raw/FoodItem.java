package models.raw;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by fredrikkindstrom on 2017-02-06.
 */
@Entity
@Table(name = "FoodItems")
public class FoodItem extends Model {

	@Id @Column(name = "_id") public Long id;

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
}
