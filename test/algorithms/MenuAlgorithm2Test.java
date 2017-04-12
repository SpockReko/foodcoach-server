package algorithms;

import models.food.FoodItem;
import models.recipe.*;

import java.util.ArrayList;
import java.util.List;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by macbookl on 11/04/17.
 */
public class MenuAlgorithm2Test {
    public static void main(String[] args){
        FoodItem food1=new FoodItem("food1", 1);
        FoodItem food2=new FoodItem("food2", 2);
        FoodItem food3=new FoodItem("food3", 3);
        FoodItem food4=new FoodItem("food4", 4);
        FoodItem food5=new FoodItem("food5", 5);
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

        List<Recipe> recList =new ArrayList<Recipe>();
        recList.add(rec5);
        recList.add(rec2);
        recList.add(rec3);
        recList.add(rec4);

        List<FoodItem> foods=new ArrayList<FoodItem>();
        foods.add(food2);
        foods.add(food3);
        foods.add(food4);

        List<Amount> amountList=new ArrayList<>();
        amountList.add(new Amount(100, GRAM));
        amountList.add(new Amount(100, GRAM));
        amountList.add(new Amount(100, GRAM));

        MenuAlgorithms algorithm=new MenuAlgorithms(foods, amountList, recList);
        algorithm.setNrOfRecipes(3);
        //System.out.println(algorithm.calculateWeekMenuFromIngredientList(new ArrayList<Recipe>()).getRecipeList().size());
        Menu menu = algorithm.weekMenuFromIngredientList(new ArrayList<Recipe>());
        System.out.println("test "+foods.size());

        System.out.println("Menu: \n"+algorithm.recipeListToString(menu));

        ShoppingList shoppingList=new ShoppingList(menu, foods, amountList, true);
        System.out.println(shoppingList.toString(shoppingList));
    }
}
