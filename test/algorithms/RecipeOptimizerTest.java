package algorithms;

import models.recipe.Ingredient;
import models.recipe.Recipe;
import models.user.Nutrient;
import models.user.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static algorithms.TestVariables.ingrList1;
import static models.GlobalDummyModels.createOptimalRecipeForSpecificUser;
import static org.junit.Assert.*;

/**
 * Created by stefa on 2017-04-04.
 */
public class RecipeOptimizerTest {
    static Recipe optimized;

    @BeforeClass
    public static void init(){
        /**
        User user = new User();
        Recipe recipe = createOptimalRecipeForSpecificUser(user);
        System.out.println(recipe.getEnergyKcal());
        RecipeOptimizer optimizer=new RecipeOptimizer(recipe, user);
        optimized=optimizer.optimizeRecipe();
        System.out.println(optimized.getEnergyKcal());
        List<Ingredient> ingredients = optimized.ingredients;
            for( int i=0; i<ingredients.size(); i++ ) {
                Ingredient ingredient = ingredients.get(i);
                System.out.println(ingredient.getAmount().getAmount()+"  "+ingredient.getFoodItem().getName());
         }
        HashMap<Nutrient, Double> nutReq1=TestVariables.user1.hmap;
        System.out.println(nutReq1.get(Nutrient.CaloriKcal));
        Ingredient ingredient1 = TestVariables.ingredient1;
        ingrList1.add(ingredient1);
        System.out.println(ingrList1.get(0).getFoodItem().getName());
         */


        //testa om constraintsen blir rätt med dummy

    }

    @Before
    public void beforeTest(){

    }

    @Test
    public void isItTrue(){
        assertTrue(true);
        assertFalse(false);
        assertEquals(1,1);
        assertNull(null);
    }
}
