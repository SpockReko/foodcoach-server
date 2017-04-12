package models.recipe;

import models.food.FoodItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by stefa on 2017-03-31.
 */
public class ShoppingList {

    private Map<Ingredient, Boolean> map = new HashMap<>();
    private Double totalWaste = 0.0;
    private static List<Ingredient> ingredients;

    public ShoppingList() {
    }


    public ShoppingList(Menu menu) {
        List<Recipe> recipes = menu.getRecipeList();
        List<Ingredient> ingredients = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(recipe.getIngredients());
        }
        addList(ingredients, false);
    }

    public ShoppingList(List<Ingredient> list, Boolean check) {
        addList(list, check);
    }

    private void addList(List<Ingredient> list, Boolean check) {
        ingredients = list;
        for (Ingredient ingredient : list) {
            if (map.containsKey(ingredient)) {
                totalWaste -= ingredient.getWaste();
                putTogetherIngredients(check, ingredient);
                System.out.println(":)");
            } else {
                System.out.println(":)");
                this.map.put(ingredient, check);
            }
            totalWaste += ingredient.getWaste();
        }
        System.out.println("map: " + map.entrySet().size());
    }
/*
    public ShoppingList(Menu menu, List<FoodItem> foodItemList, List<Amount> amountList, Boolean check){
        //System.out.println("foodItemList: "+foodItemList.size());
        List<Recipe> recipes=menu.getRecipeList();
        List<Ingredient> ingredients= new ArrayList<>();
        for(Recipe recipe: recipes){
            ingredients.addAll(recipe.getIngredients());
        }
        for (Ingredient ingredient: ingredients ) {
            if(map.containsKey(ingredient)){
                totalWaste -= ingredient.getWaste();
                putTogetherIngredients(check, ingredient);
            }else {
                this.map.put(ingredient, check);
            }
            totalWaste += ingredient.getWaste();
        }
        System.out.println("ingredients: "+ingredients.size());
        System.out.println("foodItemList: "+foodItemList.size());
        for(int i=0; i<foodItemList.size(); i++){

            System.out.println("foodItem  "+foodItemList.get(i).getName()+" "+amountList.get(i).getAmount());
            removeAmountToIngredient(new Ingredient(foodItemList.get(i), amountList.get(i)), amountList.get(i).getAmount());
            System.out.println("ingredients: "+ingredients.size());

        }
        System.out.println("ingredients: "+ingredients.size());
    }
    */

    private Ingredient getKey(Ingredient ingredient) {
        Ingredient[] ingredientList = new Ingredient[map.size()];
        ingredientList = map.keySet().toArray(ingredientList);
        for (Ingredient i : ingredientList) {
            if (i.getFoodItem().getName().equals(ingredient.getFoodItem().getName())) {
                return i;
            }
        }
        return null;
    }

    // Functions.

    // remove ingredients
    public void removeIngredient(Ingredient ingredient) {
        map.remove(ingredient);
    }

    // Add ingredients
    public void addIngredient(Ingredient ingredient) {
        map.put(ingredient, false);
    }

    // put together ingredients
    private void putTogetherIngredients(Boolean check, Ingredient ingredient) {
        Ingredient oldIngredient = getKey(ingredient);
        map.remove(oldIngredient);
        Ingredient newIngredient = ChangeAmountOfIngredient(ingredient.getAmount().getAmount(), oldIngredient);
        map.put(newIngredient, check);
    }


    // remover amount from ingredients
    public void removeAmountToIngredient(Ingredient ingredient, double amount) {
        String name = ingredient.getFoodItem().getName();
        Ingredient[] ingredients = new Ingredient[map.size()];
        ingredients = map.keySet().toArray(ingredients);
        for (Ingredient i : ingredients) {
            if (i.getFoodItem().getName().equals(name)) {
                if (!map.remove(i)) {
                    if (i.getAmount().getAmount() > amount) {
                        double newValue = i.getAmount().getAmount() - amount;
                        Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                        Ingredient newIngredient = new Ingredient(i.getFoodItem(), newAmount);
                        map.put(newIngredient, false);
                    } else {

                    }

                }
            }
        }

    }

    // add amount to ingredients
    public void addAmountToIngredient(Ingredient ingredient, double amount) {
        String name = ingredient.getFoodItem().getName();
        Ingredient[] ingredients = new Ingredient[map.size()];
        ingredients = map.keySet().toArray(ingredients);
        for (Ingredient i : ingredients) {
            if (i.getFoodItem().getName().equals(name)) {
                if (!map.remove(i)) {
                    double newValue = i.getAmount().getAmount() + amount;
                    Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                    Ingredient newIngredient = new Ingredient(i.getFoodItem(), newAmount);
                    map.put(newIngredient, false);
                }
            }
        }
    }



    // help method
    @NotNull
    private Ingredient ChangeAmountOfIngredient(double amount, Ingredient indexIngredient) {
        return new Ingredient(
                indexIngredient.getFoodItem(),
                new Amount(
                        indexIngredient.getAmount().getAmount() + amount,
                        indexIngredient.getAmount().getUnit()
                ));
    }

    // size of the shoppingList.
    public int size() {
        return map.size();
    }

    // get total waste
    public Double getTotalWaste() {
        return totalWaste;
    }


    // check or uncheck a value.
    public boolean changeAndGetCheck(Ingredient ingredient) {
        map.put(ingredient, !map.get(ingredient));
        return map.get(ingredient);
    }


    @Override
    public String toString() {
        if (map.size() == 0) {
            return "Nothing in the Shoppinglist yet!\n";
        } else {
            Iterator iterator = map.entrySet().iterator();
            String text = "Shoppinglist!\n";
            while (iterator.hasNext()) {
                Map.Entry<Ingredient, Boolean> entry = (Map.Entry) iterator.next();
                boolean marked = entry.getValue();
                String amount = entry.getKey().getAmount().getAmount() + "";
                String unit = entry.getKey().getAmount().getUnit().toString();
                String foodItem = entry.getKey().getFoodItem().getName();
                if (marked) {
                    text += "[x] ";
                } else {
                    text += "[ ] ";
                }
                text += amount + " " + unit + " " + foodItem + "\n";
            }
            return text + "\n";
        }
    }
}