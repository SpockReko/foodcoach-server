package models.recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa on 2017-03-27.
 */
public class Menu {

    private final List<Recipe> recipeList;
    private List<String> commentList = new ArrayList<>();

    public Menu(List<Recipe> recipes){
        this.recipeList = recipes;
    }

    public List<Recipe> getRecipeList(){
        return new ArrayList<Recipe>(recipeList); //Return a copy so the class is unmodifiable
    }

    public List<String> getCommentList(){
        return new ArrayList<String>(commentList);
    }

    public void addComment(String comment){
        commentList.add(comment);
    }

    public String recipeListToString(){
        String text = "Meny:\n\n";
        for(Recipe r : this.getRecipeList()){
            text = text + r.getTitle() + "\n";
        }
        text = text + "\n\nNäringsvärden i procent av användarens behov: (1 betyder näringsbehovet är uppfyllt)\n\n";
        for(String comment : this.getCommentList()){
            text = text + comment + "\n";
        }
        ShoppingList shop = new ShoppingList(this);
        text = text + shop.toString();

        text = text + "\n\nTotalta matsvinnet för din meny: (ska ändras till koldioxidutsläpp)\n\n";

        text = text + shop.getCO2() + " gram";

        return text;
    }

}
