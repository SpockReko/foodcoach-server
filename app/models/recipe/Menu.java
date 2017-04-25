package models.recipe;


import algorithms.MenuAlgorithms;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa on 2017-03-27.
 */
public class Menu {

    private final List<Recipe> recipeList;
    private List<String> commentList = new ArrayList<>();
    private double value = MenuAlgorithms.LARGE_DISTANCE;
    public Menu(List<Recipe> recipes){

        this.recipeList = recipes;
    }

    public List<Recipe> getRecipeList() {
        return new ArrayList<Recipe>(recipeList); //Return a copy so the class is unmodifiable
    }

    public List<String> getCommentList() {
        return new ArrayList<String>(commentList);
    }

    public void addComment(String comment) {
        commentList.add(comment);
    }

    public String recipeListToString(ShoppingList shop) {
        String text = "Meny:\n\n";
        for (Recipe r : this.getRecipeList()) {
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\nNäringsvärden i procent av användarens behov: (1 betyder att näringsbehovet är uppfyllt)\n\n";
        for (String comment : this.getCommentList()) {
            text = text + comment + "\n";
        }
        text = text + shop.toString();

        text = text + "\n\nTotalta koldioxidutsläppet för din inköpslista:\n\n";

        text = text + Precision.round(shop.getCO2(),3) + " kg";

        return text;
    }

    public String nutritionToString() {
        String text = "\n\nNäringsvärden i procent av användarens behov: (1 betyder att näringsbehovet är uppfyllt)\n\n";
        for (String comment : this.getCommentList()) {
            text = text + comment + "\n";
        }
        return text;
    }

    public void setValue(double value){
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
