package models.raw;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by fredrikkindstrom on 2017-02-06.
 */
@Embeddable
public class Fats extends Model {

	@Column(name = "fat_g") public Float fat;
	@Column(name = "sum_saturated_fats_g") public Float sumSaturatedFats;
	@Column(name = "fatty_acid_40_100_g") public Float fattyAcid40100;
	@Column(name = "fatty_acid_120_g") public Float fattyAcid120;
	@Column(name = "fatty_acid_140_g") public Float fattyAcid140;
	@Column(name = "fatty_acid_160_g") public Float fattyAcid160;
	@Column(name = "fatty_acid_180_g") public Float fattyAcid180;
	@Column(name = "fatty_acid_200_g") public Float fattyAcid200;
	@Column(name = "sum_monounsaturated_fats_g") public Float sumMonounsaturatedFats;
	@Column(name = "fatty_acid_161_g") public Float fattyAcid161;
	@Column(name = "fatty_acid_181_g") public Float fattyAcid181;
	@Column(name = "sum_polyunsaturated_fats_g") public Float sumPolyunsaturatedFats;
	@Column(name = "fatty_acid_182_g") public Float fattyAcid182;
	@Column(name = "fatty_acid_183_g") public Float fattyAcid183;
	@Column(name = "fatty_acid_204_g") public Float fattyAcid204;
	@Column(name = "epa_fatty_acid_205_g") public Float epaFattyAcid205;
	@Column(name = "dpa_fatty_acid_225_g") public Float dpaFattyAcid225;
	@Column(name = "dha_fatty_acid_226_g") public Float dhaFattyAcid226;
}
