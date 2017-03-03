package models.food;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items fats.
 *
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Fats {

    @Column(name = "fat_g") private final Float fat;
    @Column(name = "sum_saturated_fats_g") private final Float sumSaturatedFats;
    @Column(name = "fatty_acid_40_100_g") private final Float fattyAcid40100;
    @Column(name = "fatty_acid_120_g") private final Float fattyAcid120;
    @Column(name = "fatty_acid_140_g") private final Float fattyAcid140;
    @Column(name = "fatty_acid_160_g") private final Float fattyAcid160;
    @Column(name = "fatty_acid_180_g") private final Float fattyAcid180;
    @Column(name = "fatty_acid_200_g") private final Float fattyAcid200;
    @Column(name = "sum_monounsaturated_fats_g") private final Float sumMonounsaturatedFats;
    @Column(name = "fatty_acid_161_g") private final Float fattyAcid161;
    @Column(name = "fatty_acid_181_g") private final Float fattyAcid181;
    @Column(name = "sum_polyunsaturated_fats_g") private final Float sumPolyunsaturatedFats;
    @Column(name = "fatty_acid_182_g") private final Float fattyAcid182;
    @Column(name = "fatty_acid_183_g") private final Float fattyAcid183;
    @Column(name = "fatty_acid_204_g") private final Float fattyAcid204;
    @Column(name = "epa_fatty_acid_205_g") private final Float epaFattyAcid205;
    @Column(name = "dpa_fatty_acid_225_g") private final Float dpaFattyAcid225;
    @Column(name = "dha_fatty_acid_226_g") private final Float dhaFattyAcid226;

    public Fats(Float fat, Float sumSaturatedFats, Float fattyAcid40100, Float fattyAcid120,
        Float fattyAcid140, Float fattyAcid160, Float fattyAcid180, Float fattyAcid200,
        Float sumMonounsaturatedFats, Float fattyAcid161, Float fattyAcid181,
        Float sumPolyunsaturatedFats, Float fattyAcid182, Float fattyAcid183, Float fattyAcid204,
        Float epaFattyAcid205, Float dpaFattyAcid225, Float dhaFattyAcid226) {
        this.fat = fat;
        this.sumSaturatedFats = sumSaturatedFats;
        this.fattyAcid40100 = fattyAcid40100;
        this.fattyAcid120 = fattyAcid120;
        this.fattyAcid140 = fattyAcid140;
        this.fattyAcid160 = fattyAcid160;
        this.fattyAcid180 = fattyAcid180;
        this.fattyAcid200 = fattyAcid200;
        this.sumMonounsaturatedFats = sumMonounsaturatedFats;
        this.fattyAcid161 = fattyAcid161;
        this.fattyAcid181 = fattyAcid181;
        this.sumPolyunsaturatedFats = sumPolyunsaturatedFats;
        this.fattyAcid182 = fattyAcid182;
        this.fattyAcid183 = fattyAcid183;
        this.fattyAcid204 = fattyAcid204;
        this.epaFattyAcid205 = epaFattyAcid205;
        this.dpaFattyAcid225 = dpaFattyAcid225;
        this.dhaFattyAcid226 = dhaFattyAcid226;
    }

    public Float getFat() {
        return fat;
    }
    public Float getSumSaturatedFats() {
        return sumSaturatedFats;
    }
    public Float getFattyAcid40100() {
        return fattyAcid40100;
    }
    public Float getFattyAcid120() {
        return fattyAcid120;
    }
    public Float getFattyAcid140() {
        return fattyAcid140;
    }
    public Float getFattyAcid160() {
        return fattyAcid160;
    }
    public Float getFattyAcid180() {
        return fattyAcid180;
    }
    public Float getFattyAcid200() {
        return fattyAcid200;
    }
    public Float getSumMonounsaturatedFats() {
        return sumMonounsaturatedFats;
    }
    public Float getFattyAcid161() {
        return fattyAcid161;
    }
    public Float getFattyAcid181() {
        return fattyAcid181;
    }
    public Float getSumPolyunsaturatedFats() {
        return sumPolyunsaturatedFats;
    }
    public Float getFattyAcid182() {
        return fattyAcid182;
    }
    public Float getFattyAcid183() {
        return fattyAcid183;
    }
    public Float getFattyAcid204() {
        return fattyAcid204;
    }
    public Float getEpaFattyAcid205() {
        return epaFattyAcid205;
    }
    public Float getDpaFattyAcid225() {
        return dpaFattyAcid225;
    }
    public Float getDhaFattyAcid226() {
        return dhaFattyAcid226;
    }
}
