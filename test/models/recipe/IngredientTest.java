package models.recipe;

import models.food.FoodItem;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Created by fredrikkindstrom on 2017-02-28.
 */
public class IngredientTest {

    private static final double delta = 1e-2;

    private static FoodItem foodItem;

    private static Ingredient kilo;
    private static Ingredient hekto;
    private static Ingredient gram;

    private static Ingredient liter;
    private static Ingredient deci;
    private static Ingredient centi;
    private static Ingredient milli;

    private static Ingredient krydd;
    private static Ingredient tesked;
    private static Ingredient matsked;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        foodItem = new FoodItem(null, null, 100, null,
                10F, 10F, 10F, 10F, 10F,
                10F, 10F, 10F, 10F, 10F, 10F,
                null, null, null, null);
        foodItem.densityConstant = 0.8;
        kilo = new Ingredient(foodItem, new Amount(1, Amount.Unit.KILOGRAM));
        hekto = new Ingredient(foodItem, new Amount(10, Amount.Unit.HECTOGRAM));
        gram = new Ingredient(foodItem, new Amount(1000, Amount.Unit.GRAM));
        liter = new Ingredient(foodItem, new Amount(1, Amount.Unit.LITER));
        deci = new Ingredient(foodItem, new Amount(10, Amount.Unit.DECILITER));
        centi = new Ingredient(foodItem, new Amount(100, Amount.Unit.CENTILITER));
        milli = new Ingredient(foodItem, new Amount(1000, Amount.Unit.MILLILITER));
        krydd = new Ingredient(foodItem, new Amount(1000, Amount.Unit.KRYDDMATT));
        tesked = new Ingredient(foodItem, new Amount(200, Amount.Unit.TESKED));
        matsked = new Ingredient(foodItem, new Amount(100, Amount.Unit.MATSKED));
    }

    @Test
    public void getFoodItem() throws Exception {
        assertThat(kilo.getFoodItem(), is(foodItem));
    }

    @Test
    public void getEnergyKcal() throws Exception {
        assertEquals(100, kilo.getEnergyKcal(), delta);
        assertEquals(100, hekto.getEnergyKcal(), delta);
        assertEquals(100, gram.getEnergyKcal(), delta);
        assertEquals(80, liter.getEnergyKcal(), delta);
        assertEquals(80, deci.getEnergyKcal(), delta);
        assertEquals(80, centi.getEnergyKcal(), delta);
        assertEquals(80, milli.getEnergyKcal(), delta);
        assertEquals(80, krydd.getEnergyKcal(), delta);
        assertEquals(80, tesked.getEnergyKcal(), delta);
        assertEquals(120, matsked.getEnergyKcal(), delta);
    }

    @Test
    public void getEnergyKj() throws Exception {
        assertEquals(100, kilo.getEnergyKj(), delta);
        assertEquals(100, hekto.getEnergyKj(), delta);
        assertEquals(100, gram.getEnergyKj(), delta);
        assertEquals(80, liter.getEnergyKj(), delta);
        assertEquals(80, deci.getEnergyKj(), delta);
        assertEquals(80, centi.getEnergyKj(), delta);
        assertEquals(80, milli.getEnergyKj(), delta);
        assertEquals(80, krydd.getEnergyKj(), delta);
        assertEquals(80, tesked.getEnergyKj(), delta);
        assertEquals(120, matsked.getEnergyKj(), delta);
    }

    @Test
    public void getCarbohydrates() throws Exception {
        assertEquals(100, kilo.getCarbohydrates(), delta);
        assertEquals(100, hekto.getCarbohydrates(), delta);
        assertEquals(100, gram.getCarbohydrates(), delta);
        assertEquals(80, liter.getCarbohydrates(), delta);
        assertEquals(80, deci.getCarbohydrates(), delta);
        assertEquals(80, centi.getCarbohydrates(), delta);
        assertEquals(80, milli.getCarbohydrates(), delta);
        assertEquals(80, krydd.getCarbohydrates(), delta);
        assertEquals(80, tesked.getCarbohydrates(), delta);
        assertEquals(120, matsked.getCarbohydrates(), delta);
    }

    @Test
    public void getProtein() throws Exception {
        assertEquals(100, kilo.getProtein(), delta);
        assertEquals(100, hekto.getProtein(), delta);
        assertEquals(100, gram.getProtein(), delta);
        assertEquals(80, liter.getProtein(), delta);
        assertEquals(80, deci.getProtein(), delta);
        assertEquals(80, centi.getProtein(), delta);
        assertEquals(80, milli.getProtein(), delta);
        assertEquals(80, krydd.getProtein(), delta);
        assertEquals(80, tesked.getProtein(), delta);
        assertEquals(120, matsked.getProtein(), delta);
    }

    @Test
    public void getFibre() throws Exception {
        assertEquals(100, kilo.getFibre(), delta);
        assertEquals(100, hekto.getFibre(), delta);
        assertEquals(100, gram.getFibre(), delta);
        assertEquals(80, liter.getFibre(), delta);
        assertEquals(80, deci.getFibre(), delta);
        assertEquals(80, centi.getFibre(), delta);
        assertEquals(80, milli.getFibre(), delta);
        assertEquals(80, krydd.getFibre(), delta);
        assertEquals(80, tesked.getFibre(), delta);
        assertEquals(120, matsked.getFibre(), delta);
    }

    @Test
    public void getWholeGrain() throws Exception {
        assertEquals(100, kilo.getWholeGrain(), delta);
        assertEquals(100, hekto.getWholeGrain(), delta);
        assertEquals(100, gram.getWholeGrain(), delta);
        assertEquals(80, liter.getWholeGrain(), delta);
        assertEquals(80, deci.getWholeGrain(), delta);
        assertEquals(80, centi.getWholeGrain(), delta);
        assertEquals(80, milli.getWholeGrain(), delta);
        assertEquals(80, krydd.getWholeGrain(), delta);
        assertEquals(80, tesked.getWholeGrain(), delta);
        assertEquals(120, matsked.getWholeGrain(), delta);
    }

    @Test
    public void getCholesterol() throws Exception {
        assertEquals(100, kilo.getCholesterol(), delta);
        assertEquals(100, hekto.getCholesterol(), delta);
        assertEquals(100, gram.getCholesterol(), delta);
        assertEquals(80, liter.getCholesterol(), delta);
        assertEquals(80, deci.getCholesterol(), delta);
        assertEquals(80, centi.getCholesterol(), delta);
        assertEquals(80, milli.getCholesterol(), delta);
        assertEquals(80, krydd.getCholesterol(), delta);
        assertEquals(80, tesked.getCholesterol(), delta);
        assertEquals(120, matsked.getCholesterol(), delta);
    }

    @Test
    public void getWater() throws Exception {
        assertEquals(100, kilo.getWater(), delta);
        assertEquals(100, hekto.getWater(), delta);
        assertEquals(100, gram.getWater(), delta);
        assertEquals(80, liter.getWater(), delta);
        assertEquals(80, deci.getWater(), delta);
        assertEquals(80, centi.getWater(), delta);
        assertEquals(80, milli.getWater(), delta);
        assertEquals(80, krydd.getWater(), delta);
        assertEquals(80, tesked.getWater(), delta);
        assertEquals(120, matsked.getWater(), delta);
    }

    @Test
    public void getAlcohol() throws Exception {
        assertEquals(100, kilo.getAlcohol(), delta);
        assertEquals(100, hekto.getAlcohol(), delta);
        assertEquals(100, gram.getAlcohol(), delta);
        assertEquals(80, liter.getAlcohol(), delta);
        assertEquals(80, deci.getAlcohol(), delta);
        assertEquals(80, centi.getAlcohol(), delta);
        assertEquals(80, milli.getAlcohol(), delta);
        assertEquals(80, krydd.getAlcohol(), delta);
        assertEquals(80, tesked.getAlcohol(), delta);
        assertEquals(120, matsked.getAlcohol(), delta);
    }

    @Test
    public void getAsh() throws Exception {
        assertEquals(100, kilo.getAsh(), delta);
        assertEquals(100, hekto.getAsh(), delta);
        assertEquals(100, gram.getAsh(), delta);
        assertEquals(80, liter.getAsh(), delta);
        assertEquals(80, deci.getAsh(), delta);
        assertEquals(80, centi.getAsh(), delta);
        assertEquals(80, milli.getAsh(), delta);
        assertEquals(80, krydd.getAsh(), delta);
        assertEquals(80, tesked.getAsh(), delta);
        assertEquals(120, matsked.getAsh(), delta);
    }

    @Test
    public void getWaste() throws Exception {
        assertEquals(100, kilo.getWaste(), delta);
        assertEquals(100, hekto.getWaste(), delta);
        assertEquals(100, gram.getWaste(), delta);
        assertEquals(80, liter.getWaste(), delta);
        assertEquals(80, deci.getWaste(), delta);
        assertEquals(80, centi.getWaste(), delta);
        assertEquals(80, milli.getWaste(), delta);
        assertEquals(80, krydd.getWaste(), delta);
        assertEquals(80, tesked.getWaste(), delta);
        assertEquals(120, matsked.getWaste(), delta);
    }

}
