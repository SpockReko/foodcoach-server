package models.recipe;

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

    @BeforeClass
    public static void init(){
        List<Ingredient> ingrediens = new ArrayList<>();
        emptyList = new ShoppingList(ingrediens,false);



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
