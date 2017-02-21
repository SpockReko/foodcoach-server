package models.recipe;

import models.food.FoodItem;

public class Ingredient {

    private FoodItem foodItem;
    private Amount amount;

    public Ingredient(FoodItem foodItem, Amount amount) {
        this.foodItem = foodItem;
        this.amount = amount;
    }
}
