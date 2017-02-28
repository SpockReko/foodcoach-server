package models.recipe;

import models.food.*;

public class Ingredient {

    private final FoodItem foodItem;
    private final Amount amount;

    public Ingredient(FoodItem foodItem, Amount amount) {
        this.foodItem = foodItem;
        this.amount = amount;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }
    public Amount getAmount() {
        return amount;
    }
//
//    public Float getEnergyKcal() {
//        return energyKcal;
//    }
//    public Float getEnergyKj() {
//        return energyKj;
//    }
//    public Float getCarbohydrates() {
//        return carbohydrates;
//    }
//    public Float getProtein() {
//        return protein;
//    }
//    public Float getFibre() {
//        return fibre;
//    }
//    public Float getWholeGrain() {
//        return wholeGrain;
//    }
//    public Float getCholesterol() {
//        return cholesterol;
//    }
//    public Float getWater() {
//        return water;
//    }
//    public Float getAlcohol() {
//        return alcohol;
//    }
//    public Float getAsh() {
//        return ash;
//    }
//    public Float getWaste() {
//        return waste;
//    }
//    public Sugars getSugars() {
//        return sugars;
//    }
//    public Fats getFats() {
//        return fats;
//    }
//    public Vitamins getVitamins() {
//        return vitamins;
//    }
//    public Minerals getMinerals() {
//        return minerals;
//    }
}
