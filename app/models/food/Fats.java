package models.food;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

/**
 * This class gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items fats.
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Fats {

	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fat_g") public Nutrient fat;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "sum_saturated_fats_g") public Nutrient sumSaturatedFats;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_40_100_g") public Nutrient fattyAcid40100;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_120_g") public Nutrient fattyAcid120;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_140_g") public Nutrient fattyAcid140;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_160_g") public Nutrient fattyAcid160;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_180_g") public Nutrient fattyAcid180;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_200_g") public Nutrient fattyAcid200;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "sum_monounsaturated_fats_g") public Nutrient sumMonounsaturatedFats;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_161_g") public Nutrient fattyAcid161;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_181_g") public Nutrient fattyAcid181;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "sum_polyunsaturated_fats_g") public Nutrient sumPolyunsaturatedFats;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_182_g") public Nutrient fattyAcid182;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_183_g") public Nutrient fattyAcid183;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "fatty_acid_204_g") public Nutrient fattyAcid204;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "epa_fatty_acid_205_g") public Nutrient epaFattyAcid205;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "dpa_fatty_acid_225_g") public Nutrient dpaFattyAcid225;
	@Convert(converter = FoodItem.GramConverter.class)
	@Column(name = "dha_fatty_acid_226_g") public Nutrient dhaFattyAcid226;
}
