package models.recipe;

/**
 * Created by stefa on 2017-03-29.
 *
 * This ingredient is used by the recipeOptimizer
 * and works like a wrapper class with a ingredient
 * and a Interger that is the least amount.
 */
public class IngredientAmount {

    private Ingredient ingredient;
    private int amount;

    public IngredientAmount(Ingredient ingredient, int amount){
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

}
