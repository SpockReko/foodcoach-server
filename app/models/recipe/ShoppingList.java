package models.recipe;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by stefa on 2017-03-31.
 */
public class ShoppingList {

    private Map<Ingredient,Boolean> map = new HashMap<>();
    private Double totalWaste = 0.0;


    public ShoppingList(List<Ingredient> list, Boolean check) {
        /*
        for (Ingredient ingredient: list ) {
            if(map.containsKey(ingredient)){
                totalWaste -= ingredient.getWaste();
                putTogetherIngredients(check, ingredient);
            }else {
                this.map.put(ingredient, check);
            }
            totalWaste += ingredient.getWaste();
        }
        */
    }




    private Ingredient getKey(Ingredient ingredient){
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            Ingredient indexIngredient = map.keySet().iterator().next();
            if(indexIngredient.getFood().equals(ingredient.getFood())){
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
            if(indexIngredient.getFood().equals(ingredient.getFood())){
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
            if(indexIngredient.getFood().equals(ingredient.getFood())){
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
                indexIngredient.getFood(),
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
        String text = "Shoppinglist!\n";
        while(iterator.hasNext()){
            Map.Entry<Ingredient,Boolean> entry = (Map.Entry) iterator.next();
            boolean marked = entry.getValue();
            String amount = entry.getKey().getAmount().getAmount() + "";
            String unit = entry.getKey().getAmount().getUnit().toString();
            String foodItem = entry.getKey().getFood().name;
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
