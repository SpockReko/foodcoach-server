package models.recipe;

import models.food.DataSource;
import models.food.Food;
import models.food.Nutrient;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class IngredientConvertTest {

    private static final double delta = 1e-2;

    private static Food foodItem;

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
        foodItem =
            new Food("test", 100, DataSource.SLV, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
                10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D, 10D,
                10D);
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
        assertThat(kilo.getFood(), is(foodItem));
    }

    @Test
    public void getEnergyKj() throws Exception {
        assertEquals(100, kilo.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(100, hekto.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(100, gram.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(80, liter.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(80, deci.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(80, centi.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(80, milli.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(80, krydd.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(80, tesked.getNutrient(Nutrient.ENERGY_KJ), delta);
        assertEquals(120, matsked.getNutrient(Nutrient.ENERGY_KJ), delta);
    }

    @Test
    public void getCarbohydrates() throws Exception {
        assertEquals(100, kilo.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(100, hekto.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(100, gram.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(80, liter.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(80, deci.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(80, centi.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(80, milli.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(80, krydd.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(80, tesked.getNutrient(Nutrient.CARBOHYDRATES), delta);
        assertEquals(120, matsked.getNutrient(Nutrient.CARBOHYDRATES), delta);
    }

    @Test
    public void getProtein() throws Exception {
        assertEquals(100, kilo.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(100, hekto.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(100, gram.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(80, liter.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(80, deci.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(80, centi.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(80, milli.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(80, krydd.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(80, tesked.getNutrient(Nutrient.PROTEIN), delta);
        assertEquals(120, matsked.getNutrient(Nutrient.PROTEIN), delta);
    }

    @Test
    public void getFibre() throws Exception {
        assertEquals(100, kilo.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(100, hekto.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(100, gram.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(80, liter.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(80, deci.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(80, centi.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(80, milli.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(80, krydd.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(80, tesked.getNutrient(Nutrient.FIBRE), delta);
        assertEquals(120, matsked.getNutrient(Nutrient.FIBRE), delta);
    }

    @Test
    public void getAlcohol() throws Exception {
        assertEquals(100, kilo.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(100, hekto.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(100, gram.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(80, liter.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(80, deci.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(80, centi.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(80, milli.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(80, krydd.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(80, tesked.getNutrient(Nutrient.ALCOHOL), delta);
        assertEquals(120, matsked.getNutrient(Nutrient.ALCOHOL), delta);
    }

}
