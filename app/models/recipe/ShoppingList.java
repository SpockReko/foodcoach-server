package models.recipe;

import models.food.FoodItem;
import models.food.fineli.Food;
import org.jetbrains.annotations.NotNull;

import java.util.*;


/**
 * Created by stefa on 2017-03-31.
 */
public class ShoppingList {

    private Map<Ingredient, Boolean> map = new HashMap<>();
    private Double totalWaste = 0.0;
    private static List<Ingredient> ingredients;
    private List<Ingredient> leftovers;


    public ShoppingList() {
    }

    public ShoppingList(Menu menu) {
        List<Recipe> recipes = menu.getRecipeList();
        List<Ingredient> ingredients = new ArrayList<>();
        leftovers=new ArrayList<>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(recipe.getIngredients());
        }
        addList(ingredients, false);
    }

    public ShoppingList(List<Ingredient> list, Boolean check) {
        addList(list, check);
    }

    public ShoppingList(Menu menu, List<Food> food, List<Amount> amount){
        List<Recipe> recipes = menu.getRecipeList();
        List<Ingredient> ingredients = new ArrayList<>();
        leftovers=new ArrayList<Ingredient>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(recipe.getIngredients());
        }
        addList(ingredients, false);
        removeAmountOfIngredients(food, amount);
    }

    private void addList(List<Ingredient> list, Boolean check) {
        ingredients = list;

        for (Ingredient ingredient : list) {
            if (map.containsKey(ingredient)) {
                //totalWaste -= ingredient.getWaste();
                putTogetherIngredients(check, ingredient);
            } else {
                this.map.put(ingredient, check);
            }
            //totalWaste += ingredient.getWaste();
        }
    }

    private Ingredient getKey(Ingredient ingredient) {
        Ingredient[] ingredientList = new Ingredient[map.size()];
        ingredientList = map.keySet().toArray(ingredientList);
        for (Ingredient i : ingredientList) {
            if (i.getFood().name.equals(ingredient.getFood().name)) {
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
        Ingredient newIngredient = changeAmountOfIngredient(ingredient.getAmount().getAmount(), oldIngredient);
        map.put(newIngredient, check);
    }

    public void removeAmountOfIngredients(List<Food> foods, List<Amount> amount){
        for(int i=0; i<foods.size(); i++){
            removeAmountToIngredient(new Ingredient(foods.get(i), amount.get(i)), amount.get(i).getAmount());
        }
    }

    // remove amount from ingredients
    public void removeAmountToIngredient(Ingredient ingredient, double amount) {
        String name = ingredient.getFood().name;
        Ingredient[] ingredients = new Ingredient[map.size()];
        ingredients = map.keySet().toArray(ingredients);
        boolean inList=false;
        for (Ingredient i : ingredients) {
            if (i.getFood().name.equals(name)) {
                if (!map.remove(i)) {
                    inList=true;
                    if (i.getAmount().getAmount() > amount) {
                        double newValue = i.getAmount().getAmount() - amount;
                        Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                        Ingredient newIngredient = new Ingredient(i.getFood(), newAmount);
                        map.put(newIngredient, false);
                    } else if (i.getAmount().getAmount() < amount) {
                        double newValue =amount - i.getAmount().getAmount();
                        Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                        Ingredient newIngredient = new Ingredient(i.getFood(), newAmount);
                        leftovers.add(newIngredient);
                    }

                }
            }
        }
        if(!inList){
            leftovers.add(ingredient);
        }

    }

    // add amount to ingredients
    public void addAmountToIngredient(Ingredient ingredient, double amount) {
        String name = ingredient.getFood().name;
        Ingredient[] ingredients = new Ingredient[map.size()];
        ingredients = map.keySet().toArray(ingredients);
        for (Ingredient i : ingredients) {
            if (i.getFood().name.equals(name)) {
                if (!map.remove(i)) {
                    double newValue = i.getAmount().getAmount() + amount;
                    Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                    Ingredient newIngredient = new Ingredient(i.getFood(), newAmount);
                    map.put(newIngredient, false);
                }
            }
        }
    }



    // help method
    @NotNull
    private Ingredient changeAmountOfIngredient(double amount, Ingredient indexIngredient) {
        return new Ingredient(
                indexIngredient.getFood(),
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

    public List<Ingredient> getLeftovers(){
        return leftovers;
    }


    // check or uncheck a value.
    public boolean changeAndGetCheck(Ingredient ingredient) {
        map.put(ingredient, !map.get(ingredient));
        return map.get(ingredient);
    }

    @Override
    public String toString() {
        if (map.size() == 0) {
            return "Inköpslistan är tom just nu!\n";
        } else {
            Iterator iterator = map.entrySet().iterator();
            String text = "\n\n\nInköpslista:\n\n";
            while (iterator.hasNext()) {
                Map.Entry<Ingredient, Boolean> entry = (Map.Entry) iterator.next();
                boolean marked = entry.getValue();
                String amount = entry.getKey().getAmount().getAmount() + "";
                String unit = entry.getKey().getAmount().getUnit().toString();
                String foodItem = entry.getKey().getFood().name;
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

    public String leftoversToString(){
        if(leftovers.size()==0){
            return "Inga rester!\n";
        }
        else {
            String text = "Rester:\n";
            for(Ingredient ingredient:leftovers){
                String unit=ingredient.getAmount().getUnit().toString();
                text+=ingredient.getAmount().getAmount()+" "+unit+" "+ingredient.getFood().name+"\n";
            }
            return text + "\n";
        }
    }

}
