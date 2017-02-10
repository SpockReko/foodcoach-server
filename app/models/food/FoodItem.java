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
	@Convert(converter = GramConverter.class)
	@Column(name = "carbohydrates_g") public Nutrient carbohydrates;
	@Convert(converter = GramConverter.class)
	@Column(name = "protein_g") public Float protein;
	@Convert(converter = GramConverter.class)
	@Column(name = "fibre_g") public Float fibre;
	@Convert(converter = GramConverter.class)
	@Column(name = "whole_grain_g") public Float wholeGrain;
	@Convert(converter = MilligramsConverter.class)
	@Column(name = "cholesterol_mg") public Float cholesterol;
	@Convert(converter = GramConverter.class)
	@Column(name = "water_g") public Float water;
	@Convert(converter = GramConverter.class)
	@Column(name = "alcohol_g") public Float alcohol;
	@Convert(converter = GramConverter.class)
	@Column(name = "ash_g") public Float ash;
	@Convert(converter = PercentConverter.class)
	@Column(name = "waste_percent") public Float waste;

	@Column(name = "sugars") @Embedded public Sugars sugars;
	@Column(name = "fats") @Embedded public Fats fats;
	@Column(name = "vitamins") @Embedded public Vitamins vitamins;
	@Column(name = "minerals") @Embedded public Minerals minerals;

	@Converter
	public static class GramConverter implements AttributeConverter<Nutrient, Float> {

		@Override public Float convertToDatabaseColumn(Nutrient attribute) {
			return attribute.getValue();
		}

		@Override public Nutrient convertToEntityAttribute(Float dbData) {
			return new Nutrient(dbData, Nutrient.Unit.GRAMS);
		}
	}

	@Converter
	public static class MilligramsConverter implements AttributeConverter<Nutrient, Float> {

		@Override public Float convertToDatabaseColumn(Nutrient attribute) {
			return attribute.getValue();
		}

		@Override public Nutrient convertToEntityAttribute(Float dbData) {
			return new Nutrient(dbData, Nutrient.Unit.MILLIGRAMS);
		}
	}

	@Converter
	public static class MicrogramsConverter implements AttributeConverter<Nutrient, Float> {

		@Override public Float convertToDatabaseColumn(Nutrient attribute) {
			return attribute.getValue();
		}

		@Override public Nutrient convertToEntityAttribute(Float dbData) {
			return new Nutrient(dbData, Nutrient.Unit.MICROGRAMS);
		}
	}

	@Converter
	public static class PercentConverter implements AttributeConverter<Nutrient, Float> {

		@Override public Float convertToDatabaseColumn(Nutrient attribute) {
			return attribute.getValue();
		}

		@Override public Nutrient convertToEntityAttribute(Float dbData) {
			return new Nutrient(dbData, Nutrient.Unit.PERCENT);
		}
	}
}
