package models.food;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Gets embedded into the {@link FoodItem} class and its corresponding database table.
 * Contains more meta information about a food items sugars.
 *
 * @author Fredrik Kindstrom
 */
@Embeddable
public class Sugars {

    @Column(name = "sugars_g") private final Float sugars;
    @Column(name = "monosaccharides_g") private final Float monosaccharides;
    @Column(name = "disaccharides_g") private final Float disaccharides;
    @Column(name = "sucrose_g") private final Float sucrose;

    public Sugars(Float sugars, Float monosaccharides, Float disaccharides, Float sucrose) {
        this.sugars = sugars;
        this.monosaccharides = monosaccharides;
        this.disaccharides = disaccharides;
        this.sucrose = sucrose;
    }

    public Float getSugars() {
        return sugars;
    }
    public Float getMonosaccharides() {
        return monosaccharides;
    }
    public Float getDisaccharides() {
        return disaccharides;
    }
    public Float getSucrose() {
        return sucrose;
    }
}
