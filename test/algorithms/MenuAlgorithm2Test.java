package algorithms;

import models.food.DataSource;
import models.food.Food;
import models.recipe.*;

import java.util.ArrayList;
import java.util.List;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by macbookl on 11/04/17.
 */
public class MenuAlgorithm2Test {
    private static List<Recipe> recList;
    private static List<Ingredient> ingredients;

    public static void main(String[] args) {
        build();
        test1();
    }

    private static void build(){
        Food food1=new Food("food1", 1, DataSource.SLV);
        Food food2=new Food("food2", 2, DataSource.SLV);
        Food food3=new Food("food3", 3, DataSource.SLV);
        Food food4=new Food("food4", 4, DataSource.SLV);
        Food food5=new Food("food5", 5, DataSource.SLV);
        List<Ingredient> iList1=new ArrayList<Ingredient>();
        List<Ingredient> iList2=new ArrayList<Ingredient>();
        List<Ingredient> iList3=new ArrayList<Ingredient>();
        List<Ingredient> iList4=new ArrayList<Ingredient>();
        List<Ingredient> iList5=new ArrayList<Ingredient>();
        iList1.add(new Ingredient(food1, new Amount(100, GRAM)));
        iList1.add(new Ingredient(food2, new Amount(100, GRAM)));
        iList1.add(new Ingredient(food3, new Amount(100, GRAM)));

        iList2.add(new Ingredient(food2, new Amount(100, GRAM)));
        iList3.add(new Ingredient(food3, new Amount(100, GRAM)));
        iList4.add(new Ingredient(food4, new Amount(100, GRAM)));
        iList5.add(new Ingredient(food5, new Amount(100, GRAM)));

        Recipe rec2=new Recipe("rec2", 1, iList2);
        Recipe rec3=new Recipe("rec3", 1, iList3);
        Recipe rec4=new Recipe("rec4", 1, iList4);
        Recipe rec5=new Recipe("rec5", 1, iList5);

        recList =new ArrayList<Recipe>();
        //recList.add(rec5);
        recList.add(rec2);
        recList.add(rec3);
        recList.add(rec4);

        ingredients=new ArrayList<Ingredient>();
        //foods.add(food2);
        ingredients.add(new Ingredient(food3,new Amount(200, GRAM)));
        ingredients.add(new Ingredient(food4,new Amount(100, GRAM)));
        ingredients.add(new Ingredient(food5,new Amount(100, GRAM)));
    }


    private static void test1() {
        MenuAlgorithms algorithm=new MenuAlgorithms(recList, new ArrayList<Recipe>(), 3);
        Menu menu = algorithm.calculateWeekMenu(ingredients);
        System.out.println("Menu: \n"+algorithm.recipeListToString(menu));
        ShoppingList shoppingList=new ShoppingList(menu, ingredients);
        System.out.println(shoppingList.toString());
        System.out.println(shoppingList.leftoversToString());

        menu = algorithm.CalculateWeekMenuMinimalShoppingList(ingredients);
        System.out.println("Menu: \n"+algorithm.recipeListToString(menu));
        shoppingList=new ShoppingList(menu, ingredients);
        System.out.println(shoppingList.toString());
        System.out.println(shoppingList.leftoversToString());
    }
}
