package models.food;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items minerals.
 *
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Minerals {

    @Column(name = "folate_ug") private final Float folate;
    @Column(name = "phosphorus_mg") private final Float phosphorus;
    @Column(name = "iodine_ug") private final Float iodine;
    @Column(name = "iron_mg") private final Float iron;
    @Column(name = "calcium_mg") private final Float calcium;
    @Column(name = "potassium_mg") private final Float potassium;
    @Column(name = "magnesium_mg") private final Float magnesium;
    @Column(name = "sodium_mg") private final Float sodium;
    @Column(name = "salt_g") private final Float salt;
    @Column(name = "selenium_ug") private final Float selenium;
    @Column(name = "zink_mg") private final Float zink;

    public Minerals(Float folate, Float phosphorus, Float iodine, Float iron, Float calcium,
        Float potassium, Float magnesium, Float sodium, Float salt, Float selenium, Float zink) {
        this.folate = folate;
        this.phosphorus = phosphorus;
        this.iodine = iodine;
        this.iron = iron;
        this.calcium = calcium;
        this.potassium = potassium;
        this.magnesium = magnesium;
        this.sodium = sodium;
        this.salt = salt;
        this.selenium = selenium;
        this.zink = zink;
    }
}
