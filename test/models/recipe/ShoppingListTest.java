package models.recipe;

import models.GlobalDummyModels;
import models.food.DataSource;
import models.food.Food;
import models.user.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by stefa on 2017-04-05.
 */
public class ShoppingListTest {

    static ShoppingList emptyList;
    static ShoppingList shoppingListWithIngredients;
    static ShoppingList shoppingListWithMenu;
    static Ingredient newIngredient;
    static List<Recipe> recipes;
    static List<Ingredient> ingredients;
    static Menu menu;

    @BeforeClass
    public static void init(){
        // empty list test
        emptyList = new ShoppingList();

        Recipe r = GlobalDummyModels.createOptimalRecipeForSpecificUser(new User());
        Recipe r2 = GlobalDummyModels.createOptimalRecipeForSpecificUser(new User("Stefan"));

        recipes = new ArrayList<>();
        System.out.println(r.getTitle() + r2.getTitle());
        recipes.add(r);
        recipes.add(r2);

        menu = new Menu(recipes);

        Ingredient i = r.ingredients.get(0);
        Ingredient i2 = r2.ingredients.get(0);

        ingredients = new ArrayList<>();
        ingredients.add(i);
        ingredients.add(i2);

        // Constructure with ingredient as parameters
        shoppingListWithIngredients = new ShoppingList(ingredients, false);

        // Constructor with Menu as parameter
        shoppingListWithMenu = new ShoppingList(menu);

        newIngredient = new Ingredient(new Amount(100, Amount.Unit.GRAM),
            new Food("newfood", 0, DataSource.SLV));
    }

    @Test
    public void emptyListSizeTest(){
        assertEquals(emptyList.size(), 0);
   }

    @Test
    public void emptyListWasteTest(){
        assertTrue(emptyList.getCO2() == 0.0);
    }

    @Test
    public void ingredientListSizeTest(){

        assertTrue(shoppingListWithIngredients.size() == 2);
    }

    @Test
    public void menuListSizeTest(){
        assertTrue(shoppingListWithIngredients.size() == 2);
    }

    @Test
    public void addAndRemoveIngredientTest() {
        ShoppingList newList = new ShoppingList();
        newList.addIngredient(ingredients.get(0));
        assertTrue("AddIngredientsTest", newList.size() == 1);
        newList.addIngredient(ingredients.get(0));
        assertTrue("Adding second Ingredients", newList.size() == 1);
        newList.removeIngredient(ingredients.get(1));
        assertTrue("Removing nonExixting ingredient", newList.size() == 1);
        newList.removeIngredient(ingredients.get(0));
        assertTrue("Removing ingredient", newList.size() == 0);
    }

    @Test
    public void addAndRemoveAmountTest(){
        System.out.println("\n\naddAndRemoveIngredentText()");
        ShoppingList list = new ShoppingList();
        list.addIngredient(ingredients.get(1));
        String string = list.toString() ;
        list.addAmountToIngredient(ingredients.get(1),22.22);
        String newString = list.toString();
        System.out.println("\nAdding amount 22.22 \n<NotEqual> " + string + newString + "</notEqual>");
        assertNotEquals(string,newString);

        list.removeAmountOfIngredient(ingredients.get(1), 122.21);
        String almostEmpty = list.toString();
        list.removeAmountOfIngredient(ingredients.get(1), 1.0);
        String empty = list.toString();
        System.out.println("\nRemove almost all and then all \n<NotEqual> " + almostEmpty + empty + "</notEqual>");
        assertNotEquals(almostEmpty,empty);

    }

    @Test
    public void changeAndGetCheckTest(){
        String first = shoppingListWithMenu.toString();
        shoppingListWithMenu.changeAndGetCheck(ingredients.get(0));
        String second = shoppingListWithMenu.toString();
        assertNotEquals(first,second);
        System.out.println("ChangeAndGetCheckTest: Mark nr 1\n\n<NotEqual>" + first + second + "</NotEqual>");
        shoppingListWithMenu.changeAndGetCheck(ingredients.get(0));
        String third = shoppingListWithMenu.toString();
        assertEquals(first,third);
        System.out.println("ChangeAndGetCheckTest: UnMark nr 1\n\n<NotEqual>" + second + third + "</NotEqual>");

    }

}
