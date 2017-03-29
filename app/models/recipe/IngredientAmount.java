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
    private int atLeastAmount;

    public IngredientAmount(Ingredient ingredient, int amount){
        this.ingredient = ingredient;
        this.atLeastAmount = amount;
    }

    public int getAmount() {
        return atLeastAmount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

}
