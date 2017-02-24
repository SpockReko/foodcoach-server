package models.food;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items vitamins.
 *
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Vitamins {

    @Column(name = "retinol_ug") private final Float retinol;
    @Column(name = "beta_karoten_ug") private final Float betaKaroten;
    @Column(name = "vitamin_a_ug") private final Float vitaminA;
    @Column(name = "vitamin_b6_ug") private final Float vitaminB6;
    @Column(name = "vitamin_b12_ug") private final Float vitaminB12;
    @Column(name = "vitamin_c_mg") private final Float vitaminC;
    @Column(name = "vitamin_d_ug") private final Float vitaminD;
    @Column(name = "vitamin_e_mg") private final Float vitaminE;
    @Column(name = "vitamin_k_ug") private final Float vitaminK;
    @Column(name = "thiamine_mg") private final Float thiamine;
    @Column(name = "riboflavin_mg") private final Float riboflavin;
    @Column(name = "niacin_mg") private final Float niacin;
    @Column(name = "niacin_equivalents_mg") private final Float niacinEquivalents;

    public Vitamins(Float retinol, Float betaKaroten, Float vitaminA, Float vitaminB6,
        Float vitaminB12, Float vitaminC, Float vitaminD, Float vitaminE, Float vitaminK,
        Float thiamine, Float riboflavin, Float niacin, Float niacinEquivalents) {
        this.retinol = retinol;
        this.betaKaroten = betaKaroten;
        this.vitaminA = vitaminA;
        this.vitaminB6 = vitaminB6;
        this.vitaminB12 = vitaminB12;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.vitaminK = vitaminK;
        this.thiamine = thiamine;
        this.riboflavin = riboflavin;
        this.niacin = niacin;
        this.niacinEquivalents = niacinEquivalents;
    }
}
