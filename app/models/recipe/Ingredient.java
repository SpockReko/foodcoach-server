package models.recipe;

import models.food.*;

public class Ingredient {

    private final FoodItem item;
    private final Amount amount;

    public Ingredient(FoodItem foodItem, Amount amount) {
        this.item = foodItem;
        this.amount = amount;
    }

    public FoodItem getFoodItem() {
        return item;
    }
    public Amount getAmount() {
        return amount;
    }

    public Double getEnergyKcal() {
        return item.getEnergyKcal() * multiplier();
    }
    public Double getEnergyKj() {
        return item.getEnergyKj() * multiplier();
    }
    public Double getCarbohydrates() {
        return item.getCarbohydrates() * multiplier();
    }
    public Double getProtein() {
        return item.getProtein() * multiplier();
    }
    public Double getFibre() {
        return item.getFibre() * multiplier();
    }
    public Double getWholeGrain() {
        return item.getWholeGrain() * multiplier();
    }
    public Double getCholesterol() {
        return item.getCholesterol() * multiplier();
    }
    public Double getWater() {
        return item.getWater() * multiplier();
    }
    public Double getAlcohol() {
        return item.getAlcohol() * multiplier();
    }
    public Double getAsh() {
        return item.getAsh() * multiplier();
    }
    public Double getWaste() {
        return item.getWaste() * multiplier();
    }

    private double multiplier() {
        if (amount.getUnit().getType() == Amount.Unit.Type.VOLUME) {
            return item.densityConstant * amount.getUnit().getFraction() * amount.getAmount();
        } else {
            return amount.getUnit().getFraction() * amount.getAmount();
        }
    }
}
