package models.recipe;

import models.GlobalDummyModels;
import models.user.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by stefa on 2017-04-05.
 */
public class ShoppingListTest {

    static ShoppingList emptyList;
    ShoppingList shoppingList;

    @BeforeClass
    public static void init(){
        List<Ingredient> ingredients = new ArrayList<>();
        emptyList = new ShoppingList(ingredients,false);
        Recipe r = GlobalDummyModels.createOptimalRecipeForSpecificUser(new User());
        Ingredient i = r.ingredients.get(0);
        Recipe r2 = GlobalDummyModels.createOptimalRecipeForSpecificUser(new User(1));
        Ingredient i2 = r.ingredients.get(0);
        ingredients.add(i);
        ingredients.add(i2);
        ShoppingList shoppingList=new ShoppingList(ingredients, false);
        System.out.println(shoppingList.toString(shoppingList));
        System.out.println(shoppingList.size());

    }

    @Test
    public void emptyListSizeTest(){
        assertEquals(emptyList.size(), 0);
   }

    @Test
    public void emptyListWasteTest(){
        assertTrue(emptyList.getTotalWaste() == 0.0);
    }
}
