package models.recipe;

import models.food.FoodItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static models.recipe.Amount.Unit.GRAM;

/**
 * Created by stefa on 2017-03-31.
 */
public class ShoppingList {

    private Map<Ingredient,Boolean> map = new HashMap<>();
    private Double totalWaste = 0.0;
    private static List<Ingredient> ingredients;

    public ShoppingList(Menu menu){
        List<Recipe> recipes=menu.getRecipeList();
        List<Ingredient> ingredients= new ArrayList<>();
        for(Recipe recipe: recipes){
            ingredients.addAll(recipe.getIngredients());
        }
        new ShoppingList(ingredients, false);
    }

    public ShoppingList(List<Ingredient> list, Boolean check){
        ingredients=list;
        System.out.println("ingredients: "+ingredients.size());
        for (Ingredient ingredient: list ) {
            if(map.containsKey(ingredient)){
                totalWaste -= ingredient.getWaste();
                putTogetherIngredients(check, ingredient);
                System.out.println(":)");
            }else {
                System.out.println(":)");
                this.map.put(ingredient, check);
            }
            totalWaste += ingredient.getWaste();
        }
        System.out.println(map.entrySet().size());
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

    private Ingredient getKey(Ingredient ingredient){
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            Ingredient indexIngredient = map.keySet().iterator().next();
            if(indexIngredient.getFoodItem().equals(ingredient.getFoodItem())){
                return indexIngredient;
            }
        }
        return null;
    }

    // Functions.

    // remove ingredients
    public void removeIngredient(Ingredient ingredient){
        map.remove(ingredient);
    }

    // Add ingredients
    public void addIngredient(Ingredient ingredient){
        map.put(ingredient,false);
    }

    // put together ingredients
    private void putTogetherIngredients(Boolean check, Ingredient ingredient) {
        Ingredient oldIngredient = getKey(ingredient);
        map.remove(oldIngredient);
        Ingredient newIngredient = ChangeAmountOfIngredient( ingredient.getAmount().getAmount(), oldIngredient);
        map.put(newIngredient,check);
    }


    // remover amount from ingredients
    public void removeAmountToIngredient(Ingredient ingredient, double amount){
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            Ingredient indexIngredient = map.keySet().iterator().next();
            if(indexIngredient.getFoodItem().equals(ingredient.getFoodItem())){
                map.remove(indexIngredient);
                Ingredient newIngredient;
                if(indexIngredient.getAmount().getAmount() > amount) {
                    newIngredient = ChangeAmountOfIngredient(-amount, indexIngredient);
                }else{
                        newIngredient = ChangeAmountOfIngredient(0.0D, indexIngredient);

                    }
                map.put(newIngredient,map.get(indexIngredient));
            }
        }
    }

    // add amount to ingredients
    public void addAmountToIngredient(Ingredient ingredient, double amount){
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            Ingredient indexIngredient = map.keySet().iterator().next();
            if(indexIngredient.getFoodItem().equals(ingredient.getFoodItem())){
                map.remove(indexIngredient);
                Ingredient newIngredient = ChangeAmountOfIngredient(amount, indexIngredient);
                map.put(newIngredient,map.get(indexIngredient));
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
    public int size(){
        return map.size();
    }

    // get total waste
    public Double getTotalWaste(){
        return totalWaste;
    }


    // check or uncheck a value.
    public boolean changeAndGetCheck(Ingredient ingredient){
        map.put(ingredient,!map.get(ingredient));
        return map.get(ingredient);
    }


    // Print out the shoppinglist
    public static String toString(ShoppingList shoppingList){
        Iterator iterator = shoppingList.map.entrySet().iterator();
        System.out.println(shoppingList.map.entrySet().size());
        String text = "Shoppinglist!\n";
        while(iterator.hasNext()){
            Map.Entry<Ingredient,Boolean> entry = (Map.Entry) iterator.next();
            boolean marked = entry.getValue();
            String amount = entry.getKey().getAmount().getAmount() + "";
            String unit = entry.getKey().getAmount().getUnit().toString();
            String foodItem = entry.getKey().getFoodItem().getName();
            if(marked){
                text += "[x] ";
            }else{
                text += "[ ] ";
            }
            text += amount + " " + unit + " " + foodItem + "\n";
        }
        return text;
    }

    //getDummyFoodItem(long nr);
}