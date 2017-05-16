package models.recipe;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.apache.commons.math3.util.Precision.round;


/**
 * Created by stefa on 2017-03-31.
 */
public class ShoppingList {

    private Map<Ingredient, Boolean> shoppingList = new HashMap<>();
    private Double CO2 = 0.0; // carbon dioxide emission for shopping list
    private static List<Ingredient> ingredients;
    private List<Ingredient> leftovers = new ArrayList<>();
    private List<Ingredient> ingredientsAtHome = new ArrayList<>();
    private double leftoverSize=0.0;

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

    public ShoppingList(Menu menu, List<Ingredient> ingredientsAtHome){
        List<Recipe> recipes = menu.getRecipeList();
        List<Ingredient> ingredients = new ArrayList<>();
        this.ingredientsAtHome = ingredientsAtHome;

        for (Recipe recipe : recipes) {
            ingredients.addAll(recipe.getIngredients());
        }
        addList(ingredients, false);
        removeListOfIngredients(ingredientsAtHome);
    }




    private void addList(List<Ingredient> list, Boolean check) {
        ingredients = list;
        for (Ingredient ingredient : list) {
            if (getKey(ingredient) != null) {
                CO2 -= ingredient.getCO2();
                putTogetherIngredients(check, ingredient);
            } else {
                this.shoppingList.put(ingredient, check);
            }
            CO2 += ingredient.getCO2();
        }
    }


    private Ingredient getKey(Ingredient ingredient) {
        Ingredient[] ingredientList = new Ingredient[shoppingList.size()];
        ingredientList = shoppingList.keySet().toArray(ingredientList);
        for (Ingredient i : ingredientList) {
            if (i.getFood().name.equals(ingredient.getFood().name)) {
                return i;
            }
        }
        return null;
    }

    // remove ingredient from shopping list
    public void removeIngredient(Ingredient ingredient) {
        shoppingList.remove(ingredient);
    }

    // Add ingredient to shopping list
    public void addIngredient(Ingredient ingredient) {
        shoppingList.put(ingredient, false);
    }

    public static List<Ingredient> getIngredients() {
        return ingredients;
    }

    private void putTogetherIngredients(Boolean check, Ingredient ingredient) {
        Ingredient oldIngredient = getKey(ingredient);
        shoppingList.remove(oldIngredient);
        Ingredient newIngredient = changeAmountOfIngredient(ingredient.getAmount().getQuantity(), oldIngredient);
        shoppingList.put(newIngredient, check);
    }


    public void removeListOfIngredients(List<Ingredient> ingredientList){
        for(int i=0; i<ingredientList.size(); i++){
            removeAmountOfIngredient(ingredientList.get(i), ingredientList.get(i).getAmount().getQuantity());
        }
    }


    public void removeAmountOfIngredient(Ingredient ingredient, double amount) {
        String name = ingredient.getFood().name;
        Ingredient[] ingredients = new Ingredient[shoppingList.size()];
        ingredients = shoppingList.keySet().toArray(ingredients);
        boolean inList=false;
        for (Ingredient i : ingredients) {
            if (i.getFood().name.compareTo(name) == 0) {
                if (!shoppingList.remove(i)) {
                    inList = true;
                    if (i.getAmount().getQuantity() > amount) { // if needed amount is greater than amount at home
                        double newValue = i.getAmount().getQuantity() - amount; // amount needed to be bought
                        Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                        Ingredient newIngredient = new Ingredient(i.getFood(), newAmount);
                        shoppingList.put(newIngredient, false);
                        if (leftovers.contains(ingredient)) {
                            leftovers.remove(ingredient);
                            leftoverSize -= amount;
                        }
                    } else if (i.getAmount().getQuantity() < amount) { // if amount at home is greater than the needed amount
                        double newValue = amount - i.getAmount().getQuantity(); // amount of ingredient which is left after cooking
                        Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                        Ingredient newIngredient = new Ingredient(i.getFood(), newAmount);
                        leftovers.add(newIngredient);
                        leftoverSize += newValue;
                    }

                }
            }
        }
        if (!inList) {
            leftovers.add(ingredient);
            leftoverSize += ingredient.getAmount().getQuantity();
        }
    }


    public void addAmountToIngredient(Ingredient ingredient, double amount) {
        String name = ingredient.getFood().name;
        Ingredient[] ingredients = new Ingredient[shoppingList.size()];
        ingredients = shoppingList.keySet().toArray(ingredients);
        for (Ingredient i : ingredients) {
            if (i.getFood().name.equals(name)) {
                if (!shoppingList.remove(i)) {
                    double newValue = i.getAmount().getQuantity() + amount;
                    Amount newAmount = new Amount(newValue, i.getAmount().getUnit());
                    Ingredient newIngredient = new Ingredient(i.getFood(), newAmount);
                    shoppingList.put(newIngredient, false);
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
                        indexIngredient.getAmount().getQuantity() + amount,
                        indexIngredient.getAmount().getUnit()
                ));
    }

    // size of the shoppingList
    public int size() {
        return shoppingList.size();
    }

    // get total amount carbondioxide emission in kg / 100 grams
    public Double getCO2() {
        return CO2;
    }

    public List<Ingredient> getLeftovers(){
        return leftovers;
    }

    public double getLeftoverSize(){
        return leftoverSize;
    }

    // check or uncheck a value
    public boolean changeAndGetCheck(Ingredient ingredient) {
        shoppingList.put(ingredient, !shoppingList.get(ingredient));
        return shoppingList.get(ingredient);
    }

    @Override
    public String toString() {
        String text = "";
        if(ingredientsAtHome.size() != 0) {
            text+="\n\n\nIngredienser hemma: \n\n";
            for (int i=0; i<ingredientsAtHome.size(); i++) {
                text += ingredientsAtHome.get(i).getFood().name + " " + ingredientsAtHome.get(i).getAmount().getQuantity()
                        + " " + ingredientsAtHome.get(i).getAmount().getUnit().toString() + "\n";
            }
        }

        if (shoppingList.size() == 0) {
            return "Inköpslistan är tom just nu!\n";
        } else {
            Iterator iterator = shoppingList.entrySet().iterator();
            text += "\n\nInköpslista:\n\n";
            while (iterator.hasNext()) {
                Map.Entry<Ingredient, Boolean> entry = (Map.Entry) iterator.next();
                boolean marked = entry.getValue();
                String amount = round(entry.getKey().getAmount().getQuantity(),2) + "";
                String unit = entry.getKey().getAmount().getUnit().toString();
                String foodItem = entry.getKey().getFood().name;
                if (marked) {
                    text += "[x] ";
                } else {
                    text += "[ ] ";
                }
                text += amount + " " + unit + " " + foodItem + "\n";
            }
        }

        text+="\nRester:\n \n";
        if(leftovers.size() > 0 ){
            for (Ingredient i: leftovers) {
                text += i.getFood().name + " " + round(i.getAmount().getQuantity(),2) + " " + i.getAmount().getUnit() + "\n";
            }
        }

        return text;
    }

    public String leftoversToString(){
        if(leftovers.size()==0){
            return "Inga rester!\n";
        }
        else {
            String text = "Rester:\n";
            for(Ingredient ingredient:leftovers){
                String unit=ingredient.getAmount().getUnit().toString();
                text+=ingredient.getAmount().getQuantity()+" "+unit+" "+ingredient.getFood().name+"\n";
            }
            return text + "\n";
        }
    }

}
